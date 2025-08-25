package com.dropchop.recyclone.base.es.repo;

import com.dropchop.recyclone.base.api.model.invoke.Constants;
import com.dropchop.recyclone.base.dto.model.invoke.Params;
import com.dropchop.recyclone.base.dto.model.rest.AggregationResult;
import com.dropchop.recyclone.base.es.repo.listener.AggregationResultConsumer;
import com.dropchop.recyclone.base.es.repo.listener.MapResultConsumer;
import com.dropchop.recyclone.base.es.repo.listener.QueryResultConsumer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.dropchop.recyclone.base.es.repo.listener.QueryResultConsumer.Progress;

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

  private static final class ListTypeRef extends TypeReference<List<?>> {}
  private static final class MapTypeRef extends TypeReference<Map<String, ?>> {}

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

  private <T> boolean invokeListeners(List<QueryResultConsumer<T>> listeners, T source) {
    if (source == null) {
      return false;
    }
    boolean stop = true;
    for (QueryResultConsumer<T> listener : listeners) {
      if (listener.accept(source) != Progress.STOP) {
        stop = false;
      }
    }
    return !stop;
  }

  private <T> void parseHitsArray(SearchResultMetadata metaData,
                                  Params params,
                                  JsonParser parser,
                                  Class<T> sourceType,
                                  List<QueryResultConsumer<T>> objectListeners,
                                  List<QueryResultConsumer<Map<String, ?>>> mapListeners) throws IOException {
    parser.nextToken(); // START_ARRAY
    boolean continueParsing = !objectListeners.isEmpty() || !mapListeners.isEmpty();
    int hitCount = 0;
    while (parser.nextToken() == JsonToken.START_OBJECT) {
      String prevField = null;
      while (parser.nextToken() != JsonToken.END_OBJECT) {
        String hitField = parser.currentName();
        if (hitField == null) {
          continue;
        }
        if (prevField != null && prevField.equals(hitField)) {
          continue;
        }
        parser.nextToken();
        switch (hitField) {
          case "_source":
            if (continueParsing) {
              boolean stop = true;
              if (!mapListeners.isEmpty()) {
                ObjectReader reader = mapper.readerFor(new MapTypeRef())
                    .withAttribute(Constants.InternalContextVariables.RECYCLONE_PARAMS, params);
                Map<String, ?> source = reader.readValue(parser);
                continueParsing = invokeListeners(mapListeners, source);
              }
              if (!objectListeners.isEmpty()) {
                ObjectReader reader = mapper.readerFor(sourceType)
                    .withAttribute(Constants.InternalContextVariables.RECYCLONE_PARAMS, params);
                T source = reader.readValue(parser);
                continueParsing = invokeListeners(objectListeners, source);
              }
              hitCount++;
              break;
            }
            break;

          case "sort":
            if (this.searchAfterMode) {
              List<?> sortValues = mapper.readValue(parser, new ListTypeRef());
              metaData.setLastSortValues(Objects.requireNonNullElse(sortValues, new ArrayList<>()));
            }
            break;

          default:
            parser.skipChildren();
        }
        prevField = hitField;
      }
    }
    metaData.setHits(hitCount);
  }

  private Map<String, Object> parseAggregationContents(JsonParser parser) throws IOException {
    Map<String, Object> aggMap = new LinkedHashMap<>();
    parser.nextToken(); // START_OBJECT

    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String field = parser.currentName();
      parser.nextToken();

      if (parser.currentToken().isStructStart()) {
        aggMap.put(field, mapper.readValue(parser, new TypeReference<Map<String, Object>>() {}));
      } else {
        aggMap.put(field, mapper.readValue(parser, Object.class));
      }
    }

    return aggMap;
  }

  private void parseAggregations(JsonParser parser,
                                 Params params,
                                 List<AggregationResultConsumer> aggListeners) throws IOException {
    if (aggListeners == null || aggListeners.isEmpty()) {
      return;
    }
    parser.nextToken(); // Move to START_OBJECT

    while (parser.nextToken() == JsonToken.FIELD_NAME) {
      //String aggName = parser.currentName();
      ObjectReader reader = mapper.readerFor(new MapTypeRef())
          .withAttribute(Constants.InternalContextVariables.RECYCLONE_PARAMS, params);

      Map<String, AggregationResult> aggData = reader.readValue(parser, new TypeReference<>() {});
      parser.nextToken(); // START_OBJECT
      if (aggData != null && !aggData.isEmpty()) {
        for (Map.Entry<String, AggregationResult> entry : aggData.entrySet()) {
          String aggName = entry.getKey();
          AggregationResult aggValue = entry.getValue();

          if (aggName == null || aggValue == null) {
            continue;
          }

          for (AggregationResultConsumer listener : aggListeners) {
            listener.accept(aggName, aggValue);
          }
        }
      }
    }
  }

  public <T> SearchResultMetadata parse(InputStream responseStream,
                                        Params params,
                                        Class<T> sourceType,
                                        List<QueryResultConsumer<T>> hitListeners,
                                        List<AggregationResultConsumer> aggListeners)
      throws IOException {
    List<QueryResultConsumer<T>> objectListeners = new ArrayList<>();
    List<QueryResultConsumer<Map<String, ?>>> mapListeners = new ArrayList<>();
    for (QueryResultConsumer<T> listener : hitListeners) {
      if (listener instanceof MapResultConsumer) {
        mapListeners.add((MapResultConsumer) listener);
      } else {
        objectListeners.add(listener);
      }
    }
    SearchResultMetadata metadata = new SearchResultMetadata();
    try (JsonParser parser = mapper.getFactory().createParser(responseStream)) {
      // Parse root level fields
      do {
        JsonToken token = parser.nextToken();
        if (token == null) {
          break;
        }
        if (token == JsonToken.END_OBJECT) {
          continue;
        }
        String field = parser.currentName();
        if (field == null) {
          continue;
        }

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
                parseHitsArray(metadata, params, parser, sourceType, objectListeners, mapListeners);
              } else {
                parser.skipChildren();
              }
            }
            break;

          case "aggregations":
            parseAggregations(parser, params, aggListeners);
            break;

          default:
            parser.skipChildren();
        }
      } while (true);
    }

    return metadata;
  }
}
