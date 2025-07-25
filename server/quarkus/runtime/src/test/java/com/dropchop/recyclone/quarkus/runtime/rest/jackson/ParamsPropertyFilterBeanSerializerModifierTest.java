package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.dropchop.recyclone.base.api.model.attr.AttributeBool;
import com.dropchop.recyclone.base.api.model.attr.AttributeDate;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.attr.AttributeValueList;
import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.localization.Language;
import com.dropchop.recyclone.base.dto.model.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.base.dto.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.rest.ResultStatus;
import com.dropchop.recyclone.base.dto.model.security.Action;
import com.dropchop.recyclone.base.dto.model.security.Domain;
import com.dropchop.recyclone.base.dto.model.security.Permission;
import com.dropchop.recyclone.base.dto.model.tagging.LanguageGroup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

class ParamsPropertyFilterBeanSerializerModifierTest {

  //private static final Logger log = LoggerFactory.getLogger(ParamsPropertyFilterSerializerModifierTest.class);

  @SuppressWarnings("FieldCanBeLocal")
  private LanguageGroup southSlavic, westGermanic, slavic, germanic, baltoSlav, indoEu;
  @SuppressWarnings("FieldCanBeLocal")
  private Language sl, en;

  @BeforeEach
  void setUp() {
    indoEu = new LanguageGroup("indo_european");
    indoEu.setTitle("Indo-European");
    indoEu.setLang("en");
    indoEu.setTags(new ArrayList<>());
    indoEu.setAttributes(new LinkedHashSet<>());
    indoEu.addTranslation(new TitleDescriptionTranslation("sl", "Indoevropski"));
    indoEu.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    indoEu.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));

    baltoSlav = new LanguageGroup("balto_slavic");
    baltoSlav.setTitle("Balto-Slavic");
    baltoSlav.setLang("en");
    baltoSlav.addTag(indoEu);
    baltoSlav.setAttributes(new LinkedHashSet<>());
    baltoSlav.addTranslation(new TitleDescriptionTranslation("sl", "Baltoslovanski"));
    baltoSlav.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    baltoSlav.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));

    slavic = new LanguageGroup("slavic");
    slavic.setTitle("Slavic");
    slavic.setLang("en");
    slavic.addTag(baltoSlav);
    slavic.setAttributes(new LinkedHashSet<>());
    slavic.addTranslation(new TitleDescriptionTranslation("sl", "Slovanski"));
    slavic.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    slavic.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));

    germanic = new LanguageGroup("germanic");
    germanic.setTitle("Germanic");
    germanic.setLang("en");
    germanic.addTag(indoEu);
    germanic.setAttributes(new LinkedHashSet<>());
    germanic.addTranslation(new TitleDescriptionTranslation("sl", "Germanski"));
    germanic.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    germanic.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));

    southSlavic = new LanguageGroup("south_slavic");
    southSlavic.setTitle("South Slavic");
    southSlavic.setLang("en");
    southSlavic.addTag(slavic);
    southSlavic.setAttributes(new LinkedHashSet<>());
    southSlavic.addTranslation(new TitleDescriptionTranslation("sl", "Južno slovanski"));
    southSlavic.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    southSlavic.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    southSlavic.addAttribute(AttributeString.builder()
        .name("prop1").value("prop1 value").build());
    southSlavic.addAttribute(AttributeBool.builder()
        .name("prop2").value(Boolean.FALSE).build());
    southSlavic.addAttribute(AttributeDate.builder()
        .name("prop3").value(Iso8601.fromIso("2022-08-01")).build());
    southSlavic.addAttribute(AttributeValueList.builder()
        .name("prop4").value(List.of("item1", "item2", "item3")).build());

    westGermanic = new LanguageGroup("west_germanic");
    westGermanic.setTitle("West Germanic");
    westGermanic.setLang("en");
    westGermanic.addTag(germanic);
    westGermanic.setAttributes(new LinkedHashSet<>());
    westGermanic.addTranslation(new TitleDescriptionTranslation("sl", "Zahodno Germanski"));
    westGermanic.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    westGermanic.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    westGermanic.addAttribute(AttributeString.builder()
        .name("prop1").value("prop1 value").build());
    westGermanic.addAttribute(AttributeBool.builder()
        .name("prop2").value(Boolean.FALSE).build());
    westGermanic.addAttribute(AttributeDate.builder()
        .name("prop3").value(Iso8601.fromIso("2022-08-01")).build());
    westGermanic.addAttribute(AttributeValueList.builder()
        .name("prop4").value(List.of(
            Iso8601.fromIso("2022-08-01"),
            Iso8601.fromIso("2022-08-02"),
            Iso8601.fromIso("2022-08-03"))).build());

    sl = new Language("sl");
    sl.addTranslation(new TitleTranslation("sl", "Slovenski"));
    sl.addTranslation(new TitleTranslation("sr", "Slovenački"));
    sl.setTitle("en", "Slovene");
    sl.addTag(indoEu);
    sl.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    sl.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));

    en = new Language("en");
    en.addTranslation(new TitleTranslation("sl", "Angleški"));
    en.setTitle("en", "English");
    en.addTag(westGermanic);
    en.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    en.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));
  }

  @Test
  void serializeTestTreeLevel1() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content().treeLevel(1);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    String expected = """
      [
         {
            "code":"sl",
            "title":"Slovene",
            "lang":"en",
            "created":"2022-08-27T00:00:00.000Z",
            "modified":"2022-08-27T00:00:00.000Z"
         }
      ]
      """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestTreeLevel2() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content().treeLevel(2);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    String expected = """
      [
         {
            "code":"sl",
            "title":"Slovene",
            "lang":"en",
            "translations":[
               {
                  "lang":"sl",
                  "title":"Slovenski"
               },
               {
                  "lang":"sr",
                  "title":"Slovenački"
               }
            ],
            "tags":[
               {
                  "id":"4f544a62-5156-353a-9f18-17489a29c3b2",
                  "type":"LanguageGroup",
                  "title":"Indo-European",
                  "lang":"en",
                  "created":"2022-08-27T00:00:00.000Z",
                  "modified":"2022-08-27T00:00:00.000Z",
                  "name":"indo_european"
               }
            ],
            "created":"2022-08-27T00:00:00.000Z",
            "modified":"2022-08-27T00:00:00.000Z"
         }
      ]
      """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestTreeLevel3() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content().treeLevel(3);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    String expected = """
      [
         {
            "code":"sl",
            "title":"Slovene",
            "lang":"en",
            "translations":[
               {
                  "lang":"sl",
                  "title":"Slovenski"
               },
               {
                  "lang":"sr",
                  "title":"Slovenački"
               }
            ],
            "tags":[
               {
                  "id":"4f544a62-5156-353a-9f18-17489a29c3b2",
                  "type":"LanguageGroup",
                  "title":"Indo-European",
                  "lang":"en",
                  "translations":[
                     {
                        "lang":"sl",
                        "title":"Indoevropski"
                     }
                  ],
                  "created":"2022-08-27T00:00:00.000Z",
                  "modified":"2022-08-27T00:00:00.000Z",
                  "name":"indo_european"
               }
            ],
            "created":"2022-08-27T00:00:00.000Z",
            "modified":"2022-08-27T00:00:00.000Z"
         }
      ]
      """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestTreeLevel3_2() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content().treeLevel(3);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    Domain domain = new Domain();
    domain.setCode(Constants.Domains.Localization.LANGUAGE);
    Action action = new Action();
    action.setCode(Constants.Actions.ALL);
    Permission permission = new Permission();
    permission.setUuid("28c9e87d-befe-4c70-b582-c176653d917c");
    permission.setAction(action);
    permission.setDomain(domain);
    permission.setTitle("Permit all actions on Language");
    permission.setLang("en");
    permission.addTranslation(new TitleDescriptionTranslation("sl", "Dovoli vse akcije na jezikih."));

    String json = mapper.writeValueAsString(List.of(permission));
    String expected = """
     [
        {
           "id":"28c9e87d-befe-4c70-b582-c176653d917c",
           "domain":{
              "code":"localization.language"
           },
           "action":{
              "code":"*"
           },
           "title":"Permit all actions on Language",
           "lang":"en",
           "translations":[
              {
                 "lang":"sl",
                 "title":"Dovoli vse akcije na jezikih."
              }
           ],
           "wildcardString": "localization.language:*"
        }
     ]
     """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestContentDetailAllIdCode() throws Exception {
    CodeParams params = new CodeParams();
    params.filter()
        .content()
        .treeLevel(2)
        .detailLevel(com.dropchop.recyclone.base.api.model.rest.Constants.ContentDetail.ALL_OBJS_IDCODE);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    String expected = """
     [
        {
           "code":"sl",
           "tags":[
              {
                 "id":"4f544a62-5156-353a-9f18-17489a29c3b2",
                 "type": "LanguageGroup"
              }
           ]
        }
     ]
     """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestContentDetailAllIdCodeTitle() throws Exception {
    CodeParams params = new CodeParams();
    params.filter()
        .content()
        .treeLevel(2)
        .detailLevel(com.dropchop.recyclone.base.api.model.rest.Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE);

  ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    String expected = """
      [
         {
            "code":"sl",
            "title":"Slovene",
            "lang":"en",
            "tags":[
               {
                  "id":"4f544a62-5156-353a-9f18-17489a29c3b2",
                  "type": "LanguageGroup",
                  "title":"Indo-European",
                  "lang":"en",
                  "name":"indo_european"
               }
            ]
         }
      ]
      """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestContentDetailAllIdCodeTitleTranslations() throws Exception {
    CodeParams params = new CodeParams();
    params.filter()
        .content()
        .treeLevel(2)
        .detailLevel(com.dropchop.recyclone.base.api.model.rest.Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE_TRANS);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    String expected = """
      [
         {
            "code":"sl",
            "title":"Slovene",
            "lang":"en",
            "translations":[
               {
                  "lang":"sl",
                  "title":"Slovenski"
               },
               {
                  "lang":"sr",
                  "title":"Slovenački"
               }
            ],
            "tags":[
               {
                  "id":"4f544a62-5156-353a-9f18-17489a29c3b2",
                  "type": "LanguageGroup",
                  "title":"Indo-European",
                  "lang":"en",
                  "translations":[
                     {
                        "lang":"sl",
                        "title":"Indoevropski"
                     }
                  ],
                  "name":"indo_european"
               }
            ]
         }
      ]
      """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestContentDetailNestedIdCode() throws Exception {
    CodeParams params = new CodeParams();
    params.filter()
        .content()
        .treeLevel(1)
        .detailLevel(com.dropchop.recyclone.base.api.model.rest.Constants.ContentDetail.NESTED_OBJS_IDCODE);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    String expected = """
      [
         {
            "code":"sl",
            "title":"Slovene",
            "lang":"en",
            "translations":[
               {
                  "lang":"sl",
                  "title":"Slovenski"
               },
               {
                  "lang":"sr",
                  "title":"Slovenački"
               }
            ],
            "tags":[
               {
                  "id":"4f544a62-5156-353a-9f18-17489a29c3b2",
                  "type": "LanguageGroup"
               }
            ],
            "created":"2022-08-27T00:00:00.000Z",
            "modified":"2022-08-27T00:00:00.000Z"
         }
      ]
      """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestContentDetailNestedIdCodeTitle() throws Exception {
    CodeParams params = new CodeParams();
    params.filter()
        .content()
        .treeLevel(1)
        .detailLevel(com.dropchop.recyclone.base.api.model.rest.Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    String expected = """
      [
         {
            "code":"sl",
            "title":"Slovene",
            "lang":"en",
            "translations":[
               {
                  "lang":"sl",
                  "title":"Slovenski"
               },
               {
                  "lang":"sr",
                  "title":"Slovenački"
               }
            ],
            "tags":[
               {
                  "id":"4f544a62-5156-353a-9f18-17489a29c3b2",
                  "type": "LanguageGroup",
                  "title":"Indo-European",
                  "lang":"en",
                  "name":"indo_european"
               }
            ],
            "created":"2022-08-27T00:00:00.000Z",
            "modified":"2022-08-27T00:00:00.000Z"
         }
      ]
      """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestContentDetailNestedIdCodeTitleTrans() throws Exception {
    CodeParams params = new CodeParams();
    params.filter()
        .content()
        .treeLevel(1)
        .detailLevel(com.dropchop.recyclone.base.api.model.rest.Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE_TRANS);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    String expected = """
      [
        {
          "code":"sl",
          "title":"Slovene",
          "lang":"en",
          "translations":[
             {
                "lang":"sl",
                "title":"Slovenski"
             },
             {
                "lang":"sr",
                "title":"Slovenački"
             }
          ],
          "tags":[
             {
                "id":"4f544a62-5156-353a-9f18-17489a29c3b2",
                "type": "LanguageGroup",
                "title":"Indo-European",
                "lang":"en",
                "translations":[
                   {
                      "lang":"sl",
                      "title":"Indoevropski"
                   }
                ],
                "name":"indo_european"
             }
          ],
          "created":"2022-08-27T00:00:00.000Z",
          "modified":"2022-08-27T00:00:00.000Z"
        }
      ]
      """;
    JSONAssert.assertEquals(expected, json, true);
  }

  @Test
  void serializeTestContentDetailNestedIdCodeResult() throws Exception {
    CodeParams params = new CodeParams();
    params.filter()
        .content()
        .treeLevel(1)
        .detailLevel(com.dropchop.recyclone.base.api.model.rest.Constants.ContentDetail.NESTED_OBJS_IDCODE);

    ObjectMapperFactory producer = new ObjectMapperFactory(new ParamsPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createFilteringObjectMapper();

    ResultStatus status = new ResultStatus();
    status.setCode(ResultCode.success);
    status.setTime(100);
    status.setTotal(1);

    Result<Language> result = new Result<>();
    result.setId("c0991011-4b5d-4b85-9870-6d7a19356926");
    result.setStatus(status);
    result.setData(List.of(sl));

    String json = mapper.writeValueAsString(result);
    String expected = """
     {
        "id":"c0991011-4b5d-4b85-9870-6d7a19356926",
        "status":{
           "code":"success",
           "time":100,
           "total":1
        },
        "data":[
           {
              "code":"sl",
              "title":"Slovene",
              "lang":"en",
              "translations":[
                 {
                    "lang":"sl",
                    "title":"Slovenski"
                 },
                 {
                    "lang":"sr",
                    "title":"Slovenački"
                 }
              ],
              "tags":[
                 {
                    "id":"4f544a62-5156-353a-9f18-17489a29c3b2",
                    "type": "LanguageGroup"
                 }
              ],
              "created":"2022-08-27T00:00:00.000Z",
              "modified":"2022-08-27T00:00:00.000Z"
           }
        ],
        "aggregations" : { }
     }
     """;
    JSONAssert.assertEquals(expected, json, true);
  }
}