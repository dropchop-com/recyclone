package com.dropchop.recyclone.model.dto.filtering;

import com.dropchop.recyclone.model.api.attr.AttributeBool;
import com.dropchop.recyclone.model.api.attr.AttributeDate;
import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.attr.AttributeValueList;
import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.filtering.CollectionPathSegment;
import com.dropchop.recyclone.model.api.filtering.FieldFilter;
import com.dropchop.recyclone.model.api.filtering.PathSegment;
import com.dropchop.recyclone.model.api.filtering.PropertyPathSegment;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.model.api.utils.Iso8601;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.security.Action;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 09. 22.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal", "MismatchedQueryAndUpdateOfCollection"})
class FieldFilterTest {

  private static final Logger log = LoggerFactory.getLogger(FieldFilterTest.class);

  public interface PropertyConsumer extends Consumer<PathSegment> {
  }

  public interface DivePredicate extends Predicate<PathSegment> {
  }

  private LanguageGroup southSlavic, westGermanic, slavic, germanic, baltoSlav, indoEu;
  private Language sl, en;

  private Map<String, PathSegment> slPaths, enPaths;

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

    PathSegment root = PathSegment.root(sl);
    slPaths = new LinkedHashMap<>();
    slPaths.put("", root);
    buildPaths(p -> slPaths.put(String.join(".", p.indexedPath), p), root, sl);

    root = PathSegment.root(en);
    enPaths = new LinkedHashMap<>();
    enPaths.put("", root);
    buildPaths(p -> enPaths.put(String.join(".", p.indexedPath), p), root, en);
  }

  private void buildPaths(PropertyConsumer listener, DivePredicate divePredicate, PathSegment parent, Model model) throws Exception {
    BeanInfo beanInfo = Introspector.getBeanInfo(model.getClass());
    PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
    for (PropertyDescriptor prop : props) {
      String name = prop.getName();
      Class<?> type = prop.getPropertyType();
      Method wm = prop.getWriteMethod();
      if (wm == null) {
        continue;
      }
      Method rm = prop.getReadMethod();
      //log.info("{}.{} -> {}", model.getClass().getSimpleName(), name, type);
      if (Collection.class.isAssignableFrom(type)) {
        Collection<?> c = (Collection<?>) rm.invoke(model);
        CollectionPathSegment coll = new CollectionPathSegment(parent, name, c);
        listener.accept(coll);
        if (divePredicate.test(coll) && c != null) {
          for (Object x : c) {
            if (x instanceof Model m) {
              PathSegment p = new PathSegment(coll, null, m);
              buildPaths(listener, p, m);
              coll.incCurrentIndex();
            }
          }
        }
      } else {
        PathSegment p = new PropertyPathSegment(parent, name, model);
        listener.accept(p);
        if (Model.class.isAssignableFrom(type)) {
          Model m = (Model)rm.invoke(model);
          if (divePredicate.test(p)) {
            buildPaths(listener, p, m);
          }
        }
      }
    }
  }

  private void buildPaths(PropertyConsumer listener, PathSegment parent, Model model) throws Exception {
    buildPaths(listener, segment -> true, parent, model);
  }

  private Map<String, PathSegment> walkAndDive(Params params, Model m) throws Exception {
    FieldFilter fieldFilter = FieldFilter.fromParams(params);
    Map<String, PathSegment> paths = new LinkedHashMap<>();
    PathSegment root = PathSegment.root(m);
    paths.put("", root);
    buildPaths(
      p -> {
        if (fieldFilter.test(p)) {
          paths.put(String.join(".", p.indexedPath), p);
        }
      },
      fieldFilter::dive,
      root, m
    );
    return paths;
  }

  @Test
  void fromParamsFilterLevel1() {
    CodeParams params = new CodeParams();
    params.filter().content().treeLevel(1);

//    log.info("****************************************************");
//    log.info("All path elements:");
//    slPaths.keySet().forEach(s -> log.info("{}", s));

    FieldFilter fieldFilter = FieldFilter.fromParams(params);
//    log.info("");
//    log.info("****************************************************");
//    log.info("Filter includes:");
//    fieldFilter.includes.forEach(s -> log.info("{}", s));

    List<String> filtered = slPaths.entrySet()
      .stream()
      .filter(e -> fieldFilter.test(e.getValue()))
      .map(Map.Entry::getKey)
      .toList();
//    log.info("");
//    log.info("****************************************************");
//    log.info("Filtered list:");
//    filtered.forEach(s -> log.info("{}", s));

    assertEquals(List.of(
      "",
      "code",
      "created",
      "deactivated",
      "lang",
      "modified",
      "tags",
      "title",
      "translations"), filtered);
  }

  @Test
  void fromParamsFilterLevel2() {
    CodeParams params = new CodeParams();
    params.filter().content().treeLevel(2);
    FieldFilter fieldFilter = FieldFilter.fromParams(params);

    List<String> filtered = slPaths.entrySet()
      .stream()
      .filter(e -> fieldFilter.test(e.getValue()))
      .map(Map.Entry::getKey)
      .toList();

    List<String> dive = slPaths.entrySet()
      .stream()
      .filter(e -> fieldFilter.dive(e.getValue()))
      .map(Map.Entry::getKey)
      .toList();

    assertEquals(List.of(
      "",
      "code",
      "created",
      "deactivated",
      "lang",
      "modified",
      "tags",
      "tags[0].attributes",
      "tags[0].created",
      "tags[0].deactivated",
      "tags[0].description",
      "tags[0].id",
      "tags[0].lang",
      "tags[0].modified",
      "tags[0].name",
      "tags[0].tags",
      "tags[0].title",
      "tags[0].translations",
      "tags[0].type",
      "tags[0].uuid",
      "title",
      "translations",
      "translations[0].base",
      "translations[0].lang",
      "translations[0].title",
      "translations[1].base",
      "translations[1].lang",
      "translations[1].title"), filtered);

    assertEquals(List.of(
      "",
      "tags",
      "translations"), dive);
  }

  @Test
  void fromParamsFilterLevel2CodeId() {
    long err = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(12);
    for (int i = 0; i < 20000; i++) {
      err = System.currentTimeMillis() + err;
    }

    CodeParams params = new CodeParams();
    params.filter().content()
      .treeLevel(2)
      .include("*.code")
      .include("*.id")
      .include("*.lang")
      .include("*.title")
      .exclude("*.translations");

    FieldFilter fieldFilter = FieldFilter.fromParams(params);

    List<String> filtered = slPaths.entrySet()
      .stream()
      .filter(e -> fieldFilter.test(e.getValue()))
      .map(Map.Entry::getKey)
      .toList();

    List<String> dive = slPaths.entrySet()
      .stream()
      .filter(e -> fieldFilter.dive(e.getValue()))
      .map(Map.Entry::getKey)
      .toList();

    assertEquals(List.of(
      "",
      "code",
      "lang",
      "tags[0].id",
      "tags[0].lang",
      "tags[0].title",
      "title",
      "translations[0].lang",
      "translations[0].title",
      "translations[1].lang",
      "translations[1].title"), filtered);

    assertEquals(List.of(
      "",
      "tags"), dive);
  }

  @Test
  void fromParamsFilterLevel3AllExcludesAnyCreated() {
    CodeParams params = new CodeParams();
    params.filter().content()
      .treeLevel(3)
      .exclude("*.created")
      .exclude("*.modified")
      .exclude("*.deactivated");

    FieldFilter fieldFilter = FieldFilter.fromParams(params);

    List<String> filtered = slPaths.entrySet()
      .stream()
      .filter(e -> fieldFilter.test(e.getValue()))
      .map(Map.Entry::getKey)
      .toList();

    List<String> dive = slPaths.entrySet()
      .stream()
      .filter(e -> fieldFilter.dive(e.getValue()))
      .map(Map.Entry::getKey)
      .toList();

    assertEquals(List.of(
      "",
      "code",
      "lang",
      "tags",
      "tags[0].attributes",
      "tags[0].description",
      "tags[0].id",
      "tags[0].lang",
      "tags[0].name",
      "tags[0].tags",
      "tags[0].title",
      "tags[0].translations",
      "tags[0].translations[0].base",
      "tags[0].translations[0].description",
      "tags[0].translations[0].lang",
      "tags[0].translations[0].title",
      "tags[0].type",
      "tags[0].uuid",
      "title",
      "translations",
      "translations[0].base",
      "translations[0].lang",
      "translations[0].title",
      "translations[1].base",
      "translations[1].lang",
      "translations[1].title"), filtered);

    assertEquals(List.of(
      "",
      "tags",
      "tags[0].attributes",
      "tags[0].tags",
      "tags[0].translations",
      "translations"), dive);
  }

  @Test
  void fromParamsFilterLevel2CodeIdWalk() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content()
      .treeLevel(2)
      .include("*.code")
      .include("*.id")
      .include("*.lang")
      .include("*.title")
      .exclude("*.translations");

    Map<String, PathSegment> paths = walkAndDive(params, sl);
    List<String> filteredAndVisited = paths.keySet()
      .stream()
      .toList();
    assertEquals(List.of(
      "",
      "code",
      "lang",
      "tags[0].id",
      "tags[0].lang",
      "tags[0].title",
      "title"
    ), filteredAndVisited);
  }

  @Test
  void fromParamsFilterWalkContentDetailAllIdCode() throws Exception {
    CodeParams params = new CodeParams();
    params.filter()
      .content()
      .treeLevel(2)
      .detailLevel(Constants.ContentDetail.ALL_OBJS_IDCODE);
    Map<String, PathSegment> paths = walkAndDive(params, sl);
    List<String> filteredAndVisited = paths.keySet()
      .stream()
      .toList();
    assertEquals(List.of(
      "",
      "code",
      "tags[0].id",
      "tags[0].type"
    ), filteredAndVisited);
  }



  @Test
  void fromParamsFilterWalkContentDetailAllIdCodeTitle() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content()
      .treeLevel(2)
      .detailLevel(Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE)
    ;

    Map<String, PathSegment> paths = walkAndDive(params, sl);
    List<String> filteredAndVisited = paths.keySet()
      .stream()
      .toList();
    assertEquals(List.of(
      "",
      "code",
      "lang",
      "tags[0].id",
      "tags[0].lang",
      "tags[0].name",
      "tags[0].title",
      "tags[0].type",
      "title"
      ), filteredAndVisited);
  }

  @Test
  void fromParamsFilterWalkContentDetailAllIdCodeTitleTranslations() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content()
      .treeLevel(2)
      .detailLevel(Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE_TRANS)
    ;

    Map<String, PathSegment> paths = walkAndDive(params, sl);
    List<String> filteredAndVisited = paths.keySet()
      .stream()
      .toList();
//    log.info("");
//    log.info("****************************************************");
//    log.info("Result list:");
//    fileredAndVisited.forEach(s -> log.info("{}", s));
    assertEquals(List.of(
      "",
      "code",
      "lang",
      "tags[0].id",
      "tags[0].lang",
      "tags[0].name",
      "tags[0].title",
      "tags[0].translations",
      "tags[0].translations[0].base",
      "tags[0].translations[0].description",
      "tags[0].translations[0].lang",
      "tags[0].translations[0].title",
      "tags[0].type",
      "title",
      "translations",
      "translations[0].base",
      "translations[0].lang",
      "translations[0].title",
      "translations[1].base",
      "translations[1].lang",
      "translations[1].title"
    ), filteredAndVisited);
  }

  @Test
  void fromParamsFilterWalkContentDetailNestedIdCode() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content()
      .treeLevel(1)
      .detailLevel(Constants.ContentDetail.NESTED_OBJS_IDCODE)
    ;
    Map<String, PathSegment> paths = walkAndDive(params, sl);
    List<String> filteredAndVisited = paths.keySet()
      .stream()
      .toList();
    assertEquals(List.of(
      "",
      "code",
      "created",
      "deactivated",
      "lang",
      "modified",
      "tags",
      "tags[0].id",
      "tags[0].type",
      "title",
      "translations",
      "translations[0].base",
      "translations[0].lang",
      "translations[0].title",
      "translations[1].base",
      "translations[1].lang",
      "translations[1].title"
    ), filteredAndVisited);
  }

  @Test
  void fromParamsFilterWalkContentDetailNestedIdCodeTitle() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content()
      .treeLevel(1)
      .detailLevel(Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE)
    ;
    Map<String, PathSegment> paths = walkAndDive(params, sl);
    List<String> filteredAndVisited = paths.keySet()
      .stream()
      .toList();
    assertEquals(List.of(
      "",
      "code",
      "created",
      "deactivated",
      "lang",
      "modified",
      "tags",
      "tags[0].id",
      "tags[0].lang",
      "tags[0].name",
      "tags[0].title",
      "tags[0].type",
      "title",
      "translations",
      "translations[0].base",
      "translations[0].lang",
      "translations[0].title",
      "translations[1].base",
      "translations[1].lang",
      "translations[1].title"
    ), filteredAndVisited);
  }

  @Test
  void fromParamsFilterWalkContentDetailNestedIdCodeTitleTranslations() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content()
      .treeLevel(1)
      .detailLevel(Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE_TRANS)
    ;
    Map<String, PathSegment> paths = walkAndDive(params, sl);
    List<String> filteredAndVisited = paths.keySet()
      .stream()
      .toList();
    assertEquals(List.of(
      "",
      "code",
      "created",
      "deactivated",
      "lang",
      "modified",
      "tags",
      "tags[0].id",
      "tags[0].lang",
      "tags[0].name",
      "tags[0].title",
      "tags[0].translations",
      "tags[0].translations[0].base",
      "tags[0].translations[0].description",
      "tags[0].translations[0].lang",
      "tags[0].translations[0].title",
      "tags[0].type",
      "title",
      "translations",
      "translations[0].base",
      "translations[0].lang",
      "translations[0].title",
      "translations[1].base",
      "translations[1].lang",
      "translations[1].title"
    ), filteredAndVisited);
  }

  @Test
  void fromParamsFilterWalkContentDetailNestedIdCodeTitleTranslationsPermission() throws Exception {
    CodeParams params = new CodeParams();
    params.filter().content()
      .treeLevel(1)
      .detailLevel(Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE_TRANS)
    ;
    Domain domain = new Domain();
    domain.setCode(com.dropchop.recyclone.model.api.security.Constants.Domains.Localization.LANGUAGE);
    Action action = new Action();
    action.setCode(com.dropchop.recyclone.model.api.security.Constants.Actions.ALL);
    Permission permission = new Permission();
    permission.setUuid("28c9e87d-befe-4c70-b582-c176653d917c");
    permission.setAction(action);
    permission.setDomain(domain);
    permission.setTitle("Permit all actions on Language");
    permission.setLang("en");
    permission.addTranslation(new TitleDescriptionTranslation("sl", "Dovoli vse akcije na jezikih."));
    Map<String, PathSegment> paths = walkAndDive(params, permission);
    List<String> filteredAndVisited = paths.keySet()
      .stream()
      .toList();
    assertEquals(List.of(
      "",
      "action",
      "action.code",
      "action.lang",
      "action.title",
      "action.translations",
      "created",
      "deactivated",
      "description",
      "domain",
      "domain.actions",
      "domain.code",
      "domain.lang",
      "domain.title",
      "domain.translations",
      "id",
      "instances",
      "lang",
      "modified",
      "title",
      "translations",
      "translations[0].base",
      "translations[0].description",
      "translations[0].lang",
      "translations[0].title",
      "uuid"
    ), filteredAndVisited);
  }
}