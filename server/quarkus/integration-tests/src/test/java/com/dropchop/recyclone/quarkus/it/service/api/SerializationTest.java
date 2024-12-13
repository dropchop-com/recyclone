package com.dropchop.recyclone.quarkus.it.service.api;

import com.dropchop.recyclone.base.api.model.attr.AttributeBool;
import com.dropchop.recyclone.base.api.model.attr.AttributeDate;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.attr.AttributeValueList;
import com.dropchop.recyclone.base.api.model.rest.Constants;
import com.dropchop.recyclone.base.api.model.utils.Iso8601;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.invoke.DefaultExecContext;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.dropchop.recyclone.quarkus.runtime.invoke.ExecContextBinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 30. 08. 22.
 */
@Slf4j
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SerializationTest {

  static LanguageGroup indoEu, baltoSlav, slavic, southSlavic, germanic, westGermanic;
  static Language sl, en;

  @Inject
  ObjectMapper mapper;

  @Inject
  ExecContextBinder execContextBinder;

  @BeforeAll
  public static void initModels() {
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
    southSlavic.addAttribute(AttributeString.builder()
      .name("prop1").value("prop1 value").build());
    southSlavic.addAttribute(AttributeBool.builder()
      .name("prop2").value(Boolean.FALSE).build());
    southSlavic.addAttribute(AttributeDate.builder()
      .name("prop3").value(Iso8601.fromIso("2022-08-01")).build());
    southSlavic.addAttribute(AttributeValueList.builder()
        .name("prop4").value(List.of("item1", "item2", "item3")).build());
    southSlavic.addTranslation(new TitleDescriptionTranslation("sl", "Južno slovanski"));
    southSlavic.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    southSlavic.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));

    westGermanic = new LanguageGroup("west_germanic");
    westGermanic.setTitle("West Germanic");
    westGermanic.setLang("en");
    westGermanic.addTag(germanic);
    westGermanic.setAttributes(new LinkedHashSet<>());
    westGermanic.addTranslation(new TitleDescriptionTranslation("sl", "Zahodno Germanski"));
    westGermanic.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    westGermanic.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));

    sl = new Language("sl");
    sl.setTitle("Slovene");
    sl.setLang("en");
    sl.addTag(southSlavic);
    sl.addTranslation(new TitleTranslation("sl", "Slovenski"));
    sl.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    sl.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));

    en = new Language("sl");
    en.setTitle("English");
    en.setLang("en");
    en.addTag(germanic);
    en.addTranslation(new TitleTranslation("sl", "Angleški"));
    en.setCreated(Iso8601.fromIso("2022-08-27T00:00:00Z"));
    en.setModified(Iso8601.fromIso("2022-08-27T00:00:00Z"));
  }

  @Test
  public void testDeepSerialization() throws Exception {
    DefaultExecContext<?> execContext = new DefaultExecContext<>();
    CodeParams params = new CodeParams();
    params
      .filter()
      .from(0)
      .size(10)
      .content()
        .treeLevel(1)
        .detailLevel(Constants.ContentDetail.ALL_OBJS_IDCODE);
      //params.setContentIncludes(List.of("code", "lang"));
    execContextBinder.bind(execContext, params);

    List<Language> languages = List.of(en, sl);
    String json = mapper.writeValueAsString(languages);
    log.info("Got json {}", json);
  }
}
