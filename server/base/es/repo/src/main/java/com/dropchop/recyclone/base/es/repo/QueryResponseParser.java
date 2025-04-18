package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.es.repo.listener.AggregationResultListener;
import com.dropchop.recyclone.base.es.repo.listener.QueryResultListener;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.dropchop.recyclone.base.es.repo.listener.QueryResultListener.Progress;

/**
 * Use single instance per response.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4/18/25.
 */
@SuppressWarnings("unused")
public class QueryResponseParser {

  @Getter
  @Setter
  public static class SearchResultMetadata {
    private long took = -1;
    private long totalHits = 0;
    private int hits = 0;
    private List<?> lastSortValues = new ArrayList<>();
  }

  private final ObjectMapper mapper;
  private final boolean searchAfterMode;

  public QueryResponseParser(ObjectMapper objectMapper, boolean searchAfterMode, int maxSourceDepth) {
    if (maxSourceDepth > 0) {
      ObjectMapper limitedDepthMapper = objectMapper.copy();
      limitedDepthMapper.getFactory().setStreamReadConstraints(
          StreamReadConstraints.builder().maxNestingDepth(maxSourceDepth + 5).build()
      );
      this.mapper = limitedDepthMapper;
    } else {
      this.mapper = objectMapper;
    }
    this.searchAfterMode = searchAfterMode;
  }

  public QueryResponseParser(ObjectMapper objectMapper, boolean searchAfterMode) {
    this(objectMapper, searchAfterMode, -1);
  }

  public QueryResponseParser(ObjectMapper objectMapper) {
    this(objectMapper, false);
  }

  private <T> void parseHitsArray(SearchResultMetadata metaData, JsonParser parser, Class<T> sourceType,
                                  List<QueryResultListener<T>> hitListeners) throws IOException {
    parser.nextToken(); // START_ARRAY
    boolean continueParsing = hitListeners != null && !hitListeners.isEmpty();
    int hitCount = 0;
    while (parser.nextToken() == JsonToken.START_OBJECT) {
      hitCount++;
      while (parser.nextToken() != JsonToken.END_OBJECT) {
        String hitField = parser.currentName();
        parser.nextToken();
        switch (hitField) {
          case "_source":
            if (continueParsing) {
              T source = mapper.readValue(parser, sourceType);
              if (source != null) {
                boolean stop = true;
                for (QueryResultListener<T> listener : hitListeners) {
                  if (listener.onResult(source) != Progress.STOP) {
                    stop = false;
                  }
                }
                continueParsing = !stop;
              }
            }
            break;

          case "sort":
            if (this.searchAfterMode) {
              List<?> sortValues = mapper.readValue(parser, new TypeReference<>() {});
              metaData.setLastSortValues(Objects.requireNonNullElse(sortValues, new ArrayList<>()));
            }
            break;

          default:
            parser.skipChildren();
        }
      }
    }
    metaData.setTotalHits(hitCount);
  }

  public <T> SearchResultMetadata parse(InputStream responseStream, Class<T> sourceType,
                                        List<QueryResultListener<T>> hitListeners,
                                        List<AggregationResultListener> aggListeners)
      throws IOException {
    SearchResultMetadata metadata = new SearchResultMetadata();
    try (JsonParser parser = mapper.getFactory().createParser(responseStream)) {
      // Parse root level fields
      while (parser.nextToken() != JsonToken.END_OBJECT) {
        String field = parser.currentName();
        if (field == null) continue;

        switch (field) {
          case "took":
            parser.nextToken();
            metadata.setTook(parser.getLongValue());
            break;

          case "hits":
            parser.nextToken(); // START_OBJECT
            while (parser.nextToken() != JsonToken.END_OBJECT) {
              if ("total".equals(parser.currentName())) {
                parser.nextToken(); // START_OBJECT
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                  if ("value".equals(parser.currentName())) {
                    parser.nextToken();
                    metadata.setTotalHits(parser.getLongValue());
                  } else {
                    parser.skipChildren();
                  }
                }
              } else if ("hits".equals(parser.currentName())) {
                parseHitsArray(metadata, parser, sourceType, hitListeners);
              } else {
                parser.skipChildren();
              }
            }
            break;

          case "aggs":
            parser.nextToken();
            mapper.readValue(parser, new TypeReference<Map<String, Object>>() {});

            break;

          default:
            parser.skipChildren();
        }
      }
    }

    return metadata;
  }
}
