package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.query.Condition;
import com.dropchop.recyclone.base.api.model.query.knn.KnnQuery;
import com.dropchop.recyclone.base.api.model.query.knn.KnnQueryVectorBuilder;
import com.dropchop.recyclone.base.api.model.query.knn.KnnRescoreVector;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KnnQueryDeserializer extends JsonDeserializer<KnnQuery> {

  private float[] parseQueryVector(JsonNode vectorNode) {
    if (!vectorNode.isArray()) {
      throw new IllegalArgumentException("query_vector must be an array of numbers");
    }

    List<Float> values = new ArrayList<>();
    for (int i = 0; i < vectorNode.size(); i++) {
      JsonNode element = vectorNode.get(i);
      if (!element.isNumber()) {
        throw new IllegalArgumentException("query_vector elements must be numbers");
      }
      values.add(element.floatValue());
    }

    float[] result = new float[values.size()];
    for (int i = 0; i < values.size(); i++) {
      result[i] = values.get(i);
    }
    return result;
  }

  private KnnQueryVectorBuilder parseQueryVectorBuilder(JsonNode builderNode) {
    if (!builderNode.isObject()) {
      throw new IllegalArgumentException("query_vector_builder must be an object");
    }

    KnnQueryVectorBuilder.KnnQueryVectorBuilderBuilder<?, ?> builder = KnnQueryVectorBuilder.builder();


    JsonNode textEmbeddingNode = builderNode.get("text_embedding") != null ?
      builderNode.get("text_embedding") : builderNode.get("textEmbedding");

    if (textEmbeddingNode.isObject()) {
      KnnQueryVectorBuilder.TextEmbedding.TextEmbeddingBuilder<?, ?> textBuilder =
        KnnQueryVectorBuilder.TextEmbedding.builder();

      JsonNode modelTextNode = textEmbeddingNode.get("model_text") != null ?
        textEmbeddingNode.get("model_text") : textEmbeddingNode.get("modelText");
      if (modelTextNode != null && modelTextNode.isTextual()) {
        textBuilder.modelText(modelTextNode.asText());
      } else {
        throw new IllegalArgumentException("text_embedding.model_text is required and must be a string");
      }

      JsonNode modelIdNode = textEmbeddingNode.get("model_id") != null ?
        textEmbeddingNode.get("model_id") : textEmbeddingNode.get("modelId");
      if (modelIdNode != null && modelIdNode.isTextual()) {
        textBuilder.modelId(modelIdNode.asText());
      }

      builder.textEmbedding(textBuilder.build());
    }

    return builder.build();
  }

  private KnnRescoreVector parseRescoreVector(JsonNode rescoreNode) {
    if (!rescoreNode.isObject()) {
      throw new IllegalArgumentException("rescore_vector must be an object");
    }

    JsonNode oversampleNode = rescoreNode.get("oversample");
    if (oversampleNode != null && oversampleNode.isNumber()) {
      return KnnRescoreVector.of(oversampleNode.floatValue());
    }

    throw new IllegalArgumentException("rescore_vector.oversample is required and must be a number");
  }

  private Condition parseFilter(JsonNode filterNode, ObjectCodec codec) throws IOException {
    if (filterNode == null || filterNode.isNull()) {
      return null;
    }

    JsonParser filterParser = codec.treeAsTokens(filterNode);
    ConditionDeserializer conditionDeserializer = new ConditionDeserializer();
    return conditionDeserializer.deserialize(filterParser, null);
  }

  @Override
  public KnnQuery deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
    ObjectCodec oc = jp.getCodec();
    JsonNode node = oc.readTree(jp);

    KnnQuery.KnnQueryBuilder<?, ?> builder = KnnQuery.builder();

    JsonNode fieldNode = node.get("field");
    if (fieldNode == null || !fieldNode.isTextual()) {
      throw new IllegalArgumentException("knn.field is required and must be a string");
    }
    builder.field(fieldNode.asText());

    JsonNode queryVectorNode = node.get("query_vector");
    JsonNode queryVectorBuilderNode = node.get("query_vector_builder");

    boolean hasQueryVector = queryVectorNode != null && !queryVectorNode.isNull() &&
      queryVectorNode.isArray() && !queryVectorNode.isEmpty();
    boolean hasQueryVectorBuilder = queryVectorBuilderNode != null && !queryVectorBuilderNode.isNull() &&
      !queryVectorBuilderNode.isEmpty();

    if (hasQueryVector && hasQueryVectorBuilder) {
      throw new IllegalArgumentException("knn query cannot have both query_vector and query_vector_builder");
    }

    if (!hasQueryVector && !hasQueryVectorBuilder) {
      throw new IllegalArgumentException("knn query must have either query_vector or query_vector_builder");
    }

    if (hasQueryVector) {
      builder.queryVector(parseQueryVector(queryVectorNode));
    }

    if (hasQueryVectorBuilder) {
      builder.queryVectorBuilder(parseQueryVectorBuilder(queryVectorBuilderNode));
    }

    // FIX: Add the missing k field parsing
    JsonNode kNode = node.get("k");
    if (kNode != null && kNode.isNumber()) {
      builder.k(kNode.asInt());
    }

    JsonNode numCandidatesNode = node.get("num_candidates");
    if (numCandidatesNode != null && numCandidatesNode.isNumber()) {
      int numCandidates = numCandidatesNode.asInt();
      if (numCandidates > 10000) {
        throw new IllegalArgumentException("num_candidates cannot exceed 10,000");
      }
      builder.numCandidates(numCandidates);
    }

    JsonNode filterNode = node.get("filter");
    if (filterNode != null) {
      try {
        Condition filter = parseFilter(filterNode, oc);
        builder.filter(filter);
      } catch (IOException e) {
        throw new IllegalArgumentException("Invalid filter in knn query: " + e.getMessage(), e);
      }
    }

    JsonNode similarityNode = node.get("similarity");
    if (similarityNode != null && similarityNode.isNumber()) {
      builder.similarity(similarityNode.floatValue());
    }

    JsonNode boostNode = node.get("boost");
    if (boostNode != null && boostNode.isNumber()) {
      builder.boost(boostNode.floatValue());
    }

    JsonNode nameNode = node.get("name");
    if (nameNode != null && nameNode.isTextual()) {
      builder.name(nameNode.asText());
    }

    JsonNode rescoreVectorNode = node.get("rescore_vector");
    if (rescoreVectorNode != null && !rescoreVectorNode.isNull()) {
      try {
        builder.rescoreVector(parseRescoreVector(rescoreVectorNode));
      } catch (IllegalArgumentException e) {
        System.out.println("DEBUG: rescoreVectorNode type: " + rescoreVectorNode.getNodeType());
        System.out.println("DEBUG: rescoreVectorNode value: " + rescoreVectorNode);
        throw e;
      }
    }

    return builder.build();
  }

}
