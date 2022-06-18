package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasLanguageCode;
import com.dropchop.recyclone.model.api.marker.HasTitle;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.test.Node;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.model.dto.security.Domain;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import com.dropchop.recyclone.model.entity.jpa.security.EAction;
import com.dropchop.recyclone.model.entity.jpa.security.EDomain;
import com.dropchop.recyclone.model.entity.jpa.test.ENode;
import com.dropchop.recyclone.service.jpa.blaze.security.DomainToDtoMapper;
import com.dropchop.recyclone.service.jpa.blaze.security.DomainToDtoMapperImpl;
import com.dropchop.recyclone.service.jpa.blaze.test.NodeToDtoMapper;
import com.dropchop.recyclone.service.jpa.blaze.test.NodeToDtoMapperImpl;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.*;

import static com.dropchop.recyclone.model.api.security.Constants.Actions.CREATE;
import static com.dropchop.recyclone.model.api.security.Constants.Actions.VIEW;
import static com.dropchop.recyclone.model.api.security.Constants.Domains.Security;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 05. 22.
 */
class FilteringDtoContextTest {
  List<String> availableLangs = List.of("sl", "hr", "sr", "fi", "it", "de", "fr");

  private <E extends Entity> E createMockEntity(E entity, String code, int numTranslations) {
    String title = code != null && !code.isEmpty() ?
      (code.substring(0, 1).toUpperCase() + code.substring(1).toLowerCase()).replace(".", " ")
      : "UNTITLED_CODE";
    if (entity instanceof HasCode) {
      ((HasCode) entity).setCode(code);
    }
    if (entity instanceof HasTitle) {
      ((HasTitle) entity).setTitle(title);
    }
    if (entity instanceof HasLanguageCode) {
      ((HasLanguageCode) entity).setLang("en");
    }
    if (entity instanceof HasTitleTranslation<?>) {
      Set<ETitleTranslation> translations = new HashSet<>();
      for (int i = 0; i < numTranslations; i++) {
        if (numTranslations >= availableLangs.size()) {
          break;
        }
        String lang = availableLangs.get(i);
        ETitleTranslation titleTranslation = new ETitleTranslation();
        titleTranslation.setTitle(title + " [" + lang + "]");
        titleTranslation.setLang(lang);
        titleTranslation.setCreated(ZonedDateTime.now());
        titleTranslation.setModified(ZonedDateTime.now());
        translations.add(titleTranslation);
      }
      //noinspection unchecked
      ((HasTitleTranslation<ETitleTranslation>) entity).setTranslations(translations);
    }
    if (entity instanceof HasModified) {
      ((HasModified) entity).setModified(ZonedDateTime.now());
    }
    if (entity instanceof HasCreated) {
      ((HasCreated) entity).setCreated(ZonedDateTime.now());
    }
    return entity;
  }

  @Test
  void filter() {
    SortedSet<EAction> actions = new TreeSet<>(Set.of(
      createMockEntity(new EAction(), VIEW, 2),
      createMockEntity(new EAction(), CREATE, 3)
    ));

    List<EDomain> eDomains = new ArrayList<>();
    EDomain eDomain = createMockEntity(new EDomain(), Security.DOMAIN, 3);
    eDomain.setActions(actions);
    eDomains.add(eDomain);
    eDomain = createMockEntity(new EDomain(), Security.ACTION, 4);
    eDomain.setActions(actions);
    eDomains.add(eDomain);


    MappingContext mappingContext = new FilteringDtoContext();
    mappingContext.setTotalCount(eDomains.size());
    mappingContext.setSecurityAction(VIEW);
    mappingContext.setSecurityDomain(Security.DOMAIN);

    DomainToDtoMapper mapper = new DomainToDtoMapperImpl();
    Result<Domain> result = mapper.toDtosResult(eDomains, mappingContext);
    assertNotNull(result);
  }

  private List<ENode> createTree() {
    SortedSet<ENode> grandChildNodes = new TreeSet<>(Set.of(
      createMockEntity(new ENode(), "grand_child_1", 2),
      createMockEntity(new ENode(), "grand_child_2", 3)
    ));

    SortedSet<ENode> childNodes = new TreeSet<>(Set.of(
      createMockEntity(new ENode(), "child_1", 2),
      createMockEntity(new ENode(), "child_2", 3)
    ));
    for (ENode node: childNodes) {
      node.setChildren(grandChildNodes);
    }

    List<ENode> parentNodes = List.of(
      createMockEntity(new ENode(), "parent_1", 2),
      createMockEntity(new ENode(), "parent_1", 3)
    );
    for (ENode node: parentNodes) {
      node.setChildren(childNodes);
    }
    return parentNodes;
  }

  private MappingContext createMappingContext(List<ENode> roots, Integer treeLevel, String detail) {
    MappingContext mappingContext = new FilteringDtoContext();
    mappingContext.setTotalCount(roots.size());
    mappingContext.setSecurityAction(VIEW);
    mappingContext.setSecurityDomain(Security.DOMAIN);

    CodeParams params = new CodeParams();
    if (treeLevel != null) {
      params.setContentTreeLevel(treeLevel);
    }
    if (detail != null) {
      params.setContentDetailLevel(detail);
    }
    mappingContext.setParams(params);

    return mappingContext;
  }

  @Test
  void filterContentLevelDefaultTest() {
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, null, null);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, null);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 3, null);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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

    node = node.getChildren().iterator().next();
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

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.ALL_OBJS_IDCODE);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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
  void filterContentTreeLevelTwoDetailAllIdTest() {
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.ALL_OBJS_IDCODE);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNull(node.getLang());
    assertNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNotNull(node.getChildren());

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
  }

  @Test
  void filterContentTreeLevelTwoDetailAllIdTitleTest() {
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
    assertNotNull(node);
    assertNotNull(node.getCode());
    assertNotNull(node.getLang());
    assertNotNull(node.getTitle());
    assertNull(node.getCreated());
    assertNull(node.getModified());
    assertNull(node.getTranslations());
    assertNotNull(node.getChildren());
    assertEquals(2, node.getChildren().size());

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE_TRANS);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE_TRANS);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.NESTED_OBJS_IDCODE);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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
    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.NESTED_OBJS_IDCODE);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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

    node = node.getChildren().iterator().next();
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

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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

    node = node.getChildren().iterator().next();
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

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 1, Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE_TRANS);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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

    node = node.getChildren().iterator().next();
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
    List<ENode> roots = createTree();

    MappingContext mappingContext = createMappingContext(roots, 2, Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE_TRANS);

    //ignore missing class ... compile with maven first then debug works in intellij
    NodeToDtoMapper mapper = new NodeToDtoMapperImpl();
    List<Node> result = mapper.toDtosResult(roots, mappingContext).getData();
    assertNotNull(result);
    assertEquals(2, result.size());

    Node node = result.get(0);
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

    node = node.getChildren().iterator().next();
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

    node = node.getChildren().iterator().next();
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