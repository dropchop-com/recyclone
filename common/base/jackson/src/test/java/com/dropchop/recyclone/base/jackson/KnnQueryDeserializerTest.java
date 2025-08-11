package com.dropchop.recyclone.base.jackson;

import com.dropchop.recyclone.base.api.model.query.knn.KnnQuery;
import com.dropchop.recyclone.base.api.model.query.knn.KnnQueryVectorBuilder;
import com.dropchop.recyclone.base.api.model.query.knn.KnnRescoreVector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import static com.dropchop.recyclone.base.api.model.query.Condition.*;
import static com.dropchop.recyclone.base.api.model.query.ConditionOperator.*;

public class KnnQueryDeserializerTest {

  @Test
  public void testSimpleKnnQuerySerialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("embedding_field")
      .queryVector(new float[]{0.1f, 0.2f, 0.3f, 0.4f, 0.5f})
      .k(10)
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput = mapper.writeValueAsString(knnQuery);
    String expected = """
      {
        "field": "embedding_field",
        "query_vector": [0.1, 0.2, 0.3, 0.4, 0.5],
        "k": 10,
        "boost": 1.0
      }""";

    JSONAssert.assertEquals(expected, jsonOutput, true);
  }

  @Test
  public void testKnnQueryWithAllParametersSerialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("product_embedding")
      .queryVector(new float[]{0.5f, 0.3f, 0.8f})
      .k(5)
      .numCandidates(50)
      .filter(field("category", eq("electronics")))
      .similarity(0.8f)
      .boost(2.0f)
      .name("product_similarity")
      .rescoreVector(KnnRescoreVector.of(1.5f))
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput = mapper.writeValueAsString(knnQuery);
    String expected = """
      {
        "field": "product_embedding",
        "query_vector": [0.5, 0.3, 0.8],
        "k": 5,
        "num_candidates": 50,
        "filter": {
          "category": {"$eq": "electronics"}
        },
        "similarity": 0.8,
        "boost": 2.0,
        "name": "product_similarity",
        "rescore_vector": {
          "oversample": 1.5
        }
      }""";

    JSONAssert.assertEquals(expected, jsonOutput, true);
  }

  @Test
  public void testKnnQueryWithQueryVectorBuilderSerialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("semantic_field")
      .queryVectorBuilder(KnnQueryVectorBuilder.builder()
        .textEmbedding(KnnQueryVectorBuilder.TextEmbedding.builder()
          .modelText("find similar products")
          .modelId("sentence-transformers__all-minilm-l6-v2")
          .build())
        .build())
      .k(10)
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput = mapper.writeValueAsString(knnQuery);
    String expected = """
      {
        "field": "semantic_field",
        "query_vector_builder": {
          "text_embedding": {
            "model_text": "find similar products",
            "model_id": "sentence-transformers__all-minilm-l6-v2"
          }
        },
        "k": 10,
        "boost": 1.0
      }""";

    JSONAssert.assertEquals(expected, jsonOutput, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void testSimpleKnnQueryDeserialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("embedding_field")
      .queryVector(new float[]{0.1f, 0.2f, 0.3f})
      .k(10)
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(knnQuery);
    KnnQuery deserializedQuery = mapper.readValue(jsonOutput1, KnnQuery.class);

    String jsonOutput2 = mapper.writeValueAsString(deserializedQuery);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void testComplexKnnQueryDeserialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("product_embedding")
      .queryVector(new float[]{0.5f, 0.3f, 0.8f, 0.1f})
      .k(15)
      .numCandidates(100)
      .filter(and(
        field("category", eq("electronics")),
        field("price", gte(100.0))
      ))
      .similarity(0.75f)
      .boost(1.5f)
      .name("complex_product_search")
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(knnQuery);
    KnnQuery deserializedQuery = mapper.readValue(jsonOutput1, KnnQuery.class);

    String jsonOutput2 = mapper.writeValueAsString(deserializedQuery);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void testKnnQueryWithRescoreVectorDeserialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("vector_field")
      .queryVector(new float[]{1.0f, 2.0f, 3.0f})
      .k(20)
      .rescoreVector(KnnRescoreVector.of(2.0f))
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(knnQuery);
    KnnQuery deserializedQuery = mapper.readValue(jsonOutput1, KnnQuery.class);

    String jsonOutput2 = mapper.writeValueAsString(deserializedQuery);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void testKnnQueryWithQueryVectorBuilderDeserialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("semantic_field")
      .queryVectorBuilder(KnnQueryVectorBuilder.builder()
        .textEmbedding(KnnQueryVectorBuilder.TextEmbedding.builder()
          .modelText("search for similar documents")
          .build())
        .build())
      .k(5)
      .numCandidates(25)
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(knnQuery);
    KnnQuery deserializedQuery = mapper.readValue(jsonOutput1, KnnQuery.class);

    String jsonOutput2 = mapper.writeValueAsString(deserializedQuery);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void testKnnQueryWithComplexFilterDeserialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("document_embedding")
      .queryVector(new float[]{0.2f, 0.4f, 0.6f, 0.8f, 1.0f})
      .k(12)
      .numCandidates(60)
      .filter(or(
        and(
          field("status", eq("published")),
          field("views", gte(1000))
        ),
        field("featured", eq(true))
      ))
      .boost(1.2f)
      .name("document_similarity_search")
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(knnQuery);
    KnnQuery deserializedQuery = mapper.readValue(jsonOutput1, KnnQuery.class);

    String jsonOutput2 = mapper.writeValueAsString(deserializedQuery);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }

  @Test
  @SuppressWarnings("unused")
  public void testKnnQueryWithDisabledRescoreDeserialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("embedding_field")
      .queryVector(new float[]{0.1f, 0.5f, 0.9f})
      .k(8)
      .rescoreVector(KnnRescoreVector.disabled())
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(knnQuery);
    KnnQuery deserializedQuery = mapper.readValue(jsonOutput1, KnnQuery.class);

    String jsonOutput2 = mapper.writeValueAsString(deserializedQuery);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }

  @Test
  public void testMinimalKnnQueryDeserialization() throws Exception {
    KnnQuery knnQuery = KnnQuery.builder()
      .field("minimal_field")
      .queryVector(new float[]{1.0f})
      .build();

    ObjectMapperFactory mapperFactory = new ObjectMapperFactory();
    ObjectMapper mapper = mapperFactory.createObjectMapper();

    String jsonOutput1 = mapper.writeValueAsString(knnQuery);
    KnnQuery deserializedQuery = mapper.readValue(jsonOutput1, KnnQuery.class);

    String jsonOutput2 = mapper.writeValueAsString(deserializedQuery);
    JSONAssert.assertEquals(jsonOutput1, jsonOutput2, true);
  }
}
