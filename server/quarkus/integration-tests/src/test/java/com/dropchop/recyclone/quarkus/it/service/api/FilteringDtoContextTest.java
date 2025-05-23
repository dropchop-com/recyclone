package com.dropchop.recyclone.quarkus.it.service.api;

import com.dropchop.recyclone.base.api.mapper.FilteringDtoContext;
import com.dropchop.recyclone.base.api.mapper.MappingContext;
import com.dropchop.recyclone.base.jpa.mapper.security.DomainToDtoMapper;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasLanguageCode;
import com.dropchop.recyclone.base.api.model.marker.HasTitle;
import com.dropchop.recyclone.base.api.model.marker.HasTitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.rest.Constants;
import com.dropchop.recyclone.base.dto.model.invoke.CodeParams;
import com.dropchop.recyclone.base.dto.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.security.Domain;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleTranslation;
import com.dropchop.recyclone.base.jpa.model.security.JpaAction;
import com.dropchop.recyclone.base.jpa.model.security.JpaDomain;
import com.dropchop.recyclone.quarkus.it.mapper.jpa.NodeToDtoMapper;
import com.dropchop.recyclone.quarkus.it.model.dto.Node;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaMiki;
import com.dropchop.recyclone.quarkus.it.model.entity.jpa.JpaNode;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.*;

import static com.dropchop.recyclone.base.api.model.security.Constants.Actions.CREATE;
import static com.dropchop.recyclone.base.api.model.security.Constants.Actions.VIEW;
import static com.dropchop.recyclone.base.api.model.security.Constants.Domains.Security;
import static com.dropchop.recyclone.base.api.model.security.Constants.Permission;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 05. 22.
 */
@QuarkusTest
class FilteringDtoContextTest {

  @Inject
  NodeToDtoMapper mapper;

  @Inject
  DomainToDtoMapper domainMapper;

  final List<String> availableLangs = List.of("sl", "hr", "sr", "fi", "it", "de", "fr");

  private <M extends Model> M createMockModel(M model, String code, int numTranslations) {
    String title = code != null && !code.isEmpty() ?
      (code.substring(0, 1).toUpperCase() + code.substring(1).toLowerCase()).replace(".", " ")
      : "UNTITLED_CODE";
    if (model instanceof HasCode) {
      ((HasCode) model).setCode(code);
    }
    if (model instanceof HasTitle) {
      ((HasTitle) model).setTitle(title);
    }
    if (model instanceof HasLanguageCode) {
      ((HasLanguageCode) model).setLang("en");
    }
    if (model instanceof HasTitleTranslation<?> && model instanceof Entity) {
      Set<JpaTitleTranslation> translations = new HashSet<>();
      for (int i = 0; i < numTranslations; i++) {
        if (numTranslations >= availableLangs.size()) {
          break;
        }
        String lang = availableLangs.get(i);
        JpaTitleTranslation titleTranslation = new JpaTitleTranslation();
        titleTranslation.setTitle(title + " [" + lang + "]");
        titleTranslation.setLang(lang);
        titleTranslation.setCreated(ZonedDateTime.now());
        titleTranslation.setModified(ZonedDateTime.now());
        translations.add(titleTranslation);
      }
      //noinspection unchecked
      ((HasTitleTranslation<JpaTitleTranslation>) model).setTranslations(translations);
    }
    if (model instanceof HasTitleTranslation<?> && model instanceof Dto) {
      Set<TitleTranslation> translations = new HashSet<>();
      for (int i = 0; i < numTranslations; i++) {
        if (numTranslations >= availableLangs.size()) {
          break;
        }
        String lang = availableLangs.get(i);
        TitleTranslation titleTranslation = new TitleTranslation();
        titleTranslation.setTitle(title + " [" + lang + "]");
        titleTranslation.setLang(lang);
        translations.add(titleTranslation);
      }
      //noinspection unchecked
      ((HasTitleTranslation<TitleTranslation>) model).setTranslations(translations);
    }
    if (model instanceof HasModified) {
      ((HasModified) model).setModified(ZonedDateTime.now());
    }
    if (model instanceof HasCreated) {
      ((HasCreated) model).setCreated(ZonedDateTime.now());
    }
    return model;
  }

  @Test
  void filter() {
    SortedSet<JpaAction> actions = new TreeSet<>(Set.of(
      createMockModel(new JpaAction(), VIEW, 2),
      createMockModel(new JpaAction(), CREATE, 3)
    ));

    List<JpaDomain> JpaDomains = new ArrayList<>();
    JpaDomain JpaDomain = createMockModel(new JpaDomain(), Security.DOMAIN, 3);
    JpaDomain.setActions(actions);
    JpaDomains.add(JpaDomain);
    JpaDomain = createMockModel(new JpaDomain(), Security.ACTION, 4);
    JpaDomain.setActions(actions);
    JpaDomains.add(JpaDomain);


    MappingContext mappingContext = new FilteringDtoContext();
    mappingContext.setTotalCount(JpaDomains.size());
    mappingContext.setRequiredPermissions(List.of(Permission.compose(Security.DOMAIN, VIEW)));

    Result<Domain> result = domainMapper.toDtosResult(JpaDomains, mappingContext);
    assertNotNull(result);
  }

  private List<JpaNode> createTree() {
    SortedSet<JpaNode> grandChildNodes = new TreeSet<>(Set.of(
      createMockModel(new JpaNode(), "grand_child_1", 2),
      createMockModel(new JpaNode(), "grand_child_2", 3)
    ));

    SortedSet<JpaNode> childNodes = new TreeSet<>(Set.of(
      createMockModel(new JpaNode(), "child_1", 2),
      createMockModel(new JpaNode(), "child_2", 3)
    ));
    for (JpaNode node: childNodes) {
      node.setChildren(grandChildNodes);
    }

    List<JpaNode> parentNodes = List.of(
      createMockModel(new JpaNode(), "parent_1", 2),
      createMockModel(new JpaNode(), "parent_1", 3)
    );
    JpaMiki miki = new JpaMiki();
    miki.setCode("test");
    miki.setTest("test");
    for (JpaNode node: parentNodes) {
      node.setChildren(childNodes);
      node.setMiki(miki);
    }
    return parentNodes;
  }

  private <M extends Model> MappingContext createMappingContext(List<M> roots, Integer treeLevel, String detail) {
    MappingContext mappingContext = new FilteringDtoContext();
    mappingContext.setTotalCount(roots.size());
    mappingContext.setRequiredPermissions(List.of(Permission.compose(Security.DOMAIN, VIEW)));

    CodeParams params = new CodeParams();
    if (treeLevel != null) {
      params.filter().content().treeLevel(treeLevel);

    }
    if (detail != null) {
      params.filter().content().detailLevel(detail);
    }
    mappingContext.setParams(params);

    return mappingContext;
  }

  @Test
  void filterContentLevelDefaultTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, null, null);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node.getCode());
    assertNull(node.getTitle());
    assertNull(node.getLang());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevel2Test() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, null);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNull(node.getTranslations());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevel3Test() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 3, null);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNull(node.getTranslations());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevelOneDetailAllIdTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.ALL_OBJS_IDCODE);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNull(node.getLang());
    assertNull(node.getMiki());
    assertNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevelTwoDetailAllIdTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.ALL_OBJS_IDCODE);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getMiki());
    assertNotNull(node.getMiki().getCode());
    assertNull(node.getMiki().getTest());
    assertNull(node.getLang());
    assertNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNotNull(node.getChildren());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNull(node.getLang());
    assertNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
  }

  @Test
  void filterContentTreeLevelOneDetailAllIdTitleTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getMiki());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
  }

  @Test
  void filterContentTreeLevelTwoDetailAllIdTitleTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
  }


  @Test
  void filterContentTreeLevelOneDetailAllIdTitleTransTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE_TRANS);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevelTwoDetailAllIdTitleTransTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE_TRANS);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNotNull(node.getTranslations());
    translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNull(node.getChildren());
  }


  @Test
  void filterContentTreeLevelOneDetailNestedIdTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.NESTED_OBJS_IDCODE);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());
    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNull(node.getLang());
    assertNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevelTwoDetailNestedIdTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.NESTED_OBJS_IDCODE);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNull(node.getLang());
    assertNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevelOneDetailNestedIdTitleTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevelTwoDetailNestedIdTitleTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevelOneDetailNestedIdTitleTransTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE_TRANS);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNotNull(node.getTranslations());
    translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNull(node.getChildren());
  }

  @Test
  void filterContentTreeLevelTwoDetailNestedIdTitleTransTest() {
    List<JpaNode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE_TRANS);

    //ignore missing class ... compile with maven first then debug works in intellij
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    TitleTranslation translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNotNull(node.getCreated());
    assertNotNull(node.getModified());
    assertNotNull(node.getTranslations());
    translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNotNull(node.getChildren());

    node = node.getChildren().getFirst();
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNotNull(node.getTranslations());
    translation = node.getTranslations().iterator().next();
    assertNotNull(translation.getLang());
    assertNotNull(translation.getTitle());
    assertNull(node.getChildren());
  }
}