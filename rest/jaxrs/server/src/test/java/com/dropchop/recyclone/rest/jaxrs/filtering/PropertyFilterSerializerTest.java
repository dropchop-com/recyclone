package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.api.attr.AttributeBool;
import com.dropchop.recyclone.model.api.attr.AttributeDate;
import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.attr.AttributeValueList;
import com.dropchop.recyclone.model.api.utils.Iso8601;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.rest.jaxrs.serialization.ObjectMapperFactory;
import com.dropchop.recyclone.service.api.mapping.DefaultPolymorphicRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 09. 22.
 */
class PropertyFilterSerializerTest {

  private static final Logger log = LoggerFactory.getLogger(PropertyFilterSerializerTest.class);

  private LanguageGroup southSlavic, westGermanic, slavic, germanic, baltoSlav, indoEu;
  private Language sl, en;

  @BeforeEach
  void setUp() throws Exception {
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

    sl = Language.builder()
      .translation(new TitleTranslation("sl", "Slovenski"))
      .translation(new TitleTranslation("sr", "Slovenački"))
      .code("sl")
      .title("Slovene")
      .lang("en")
      .tag(indoEu)
      .created(Iso8601.fromIso("2022-08-27T00:00:00Z"))
      .modified(Iso8601.fromIso("2022-08-27T00:00:00Z"))
      .build();

    en = Language.builder()
      .translation(new TitleTranslation("sl", "Angleški"))
      .code("en")
      .title("English")
      .lang("en")
      .tag(westGermanic)
      .created(Iso8601.fromIso("2022-08-27T00:00:00Z"))
      .modified(Iso8601.fromIso("2022-08-27T00:00:00Z"))
      .build();
  }

  @Test
  void serializeTest() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content().treeLevel(2);

    ObjectMapperFactory producer = new ObjectMapperFactory(new DefaultPolymorphicRegistry(),
      new TestPropertyFilterSerializerModifier(params));
    ObjectMapper mapper = producer.createObjectMapper();

    String json = mapper.writeValueAsString(List.of(sl));
    log.info("{}", json);
  }
}