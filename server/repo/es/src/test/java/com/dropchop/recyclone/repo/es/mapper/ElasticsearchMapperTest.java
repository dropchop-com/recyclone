package com.dropchop.recyclone.repo.es.mapper;

import com.dropchop.recyclone.model.api.query.Condition;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONException;
import org.junit.Test;

import static com.dropchop.recyclone.model.api.query.Condition.*;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.eq;
import static com.dropchop.recyclone.model.api.query.ConditionOperator.in;
import static com.dropchop.recyclone.repo.es.ElasticQueryMapper.mapCondition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.skyscreamer.jsonassert.JSONAssert;

public class ElasticsearchMapperTest {

  @Test
  public void testConditionMapper() throws JsonProcessingException, JSONException {
    Condition c = and(
      or(
        not(
          field(
            "updated",
            in(3, 2, 1)
          )
        ),
        field(
          "title",
          in(1, 2, 3)
        )
      ),
      field(
        "lol",
        eq("drone")
      )
    );

    String correctJson =
      """
        {
           "must": [
             {
               "bool": {
                 "should": [
                   {
                     "bool": {
                       "must_not":
                         {
                           "terms": {
                             "updated": [3, 2, 1]
                           }
                         }
                     }
                   },
                   {
                     "terms": {
                       "title": [1, 2, 3]
                     }
                   }
                 ],
                 "minimum_should_match": 1
               }
             },
             {
               "term": {
                 "lol": "drone"
               }
             }
           ]
         }
      """;

    QueryNodeObject ob = mapCondition(c, null);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    String json = objectMapper.writeValueAsString(ob);

    JSONAssert.assertEquals(correctJson, json, true);
  }


}
