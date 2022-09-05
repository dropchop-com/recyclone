package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.dto.filtering.CollectionPathSegment;
import com.dropchop.recyclone.model.dto.filtering.FieldFilter;
import com.dropchop.recyclone.model.dto.filtering.PathSegment;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import static com.dropchop.recyclone.model.dto.filtering.PathSegment.ROOT_OBJECT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 30. 08. 22.
 */
@Slf4j
public class FilteringContext {
  private FieldFilter fieldFilter;
  private String lastProp;

  /**
   * PathSegment which represents unmodified walk through object graph.
   * It is constructed in a way that was not intended for the filtering:
   * we get called once for collection as property and once for each collection element
   * and for each collection element property
   * What is desired is to be called once for collection and then for each collection element property
   */
  private PathSegment walk;

  /**
   * Here we skip segment if parent is collection and current is collection element
   * with obviously same property name.
   * that way we get correct paths:
   * - translations[0].lang
   * - translations[0].title
   * - translations[1].lang
   * - translations[1].title
   * Instead of:
   * - translations[0].translations.lang
   * - translations[0].translations.title
   * - translations[1].translations.lang
   * - translations[1].translations.title
   */
  private PathSegment filter;

  public FilteringContext setParams(Params params) {
    fieldFilter = FieldFilter.fromParams(params);
    return this;
  }

  private boolean isCollection(Object referer) {
    if (referer != null) {
      if (referer instanceof Collection<?> || referer.getClass().isArray()) {
        return true;
      }
    }
    return false;
  }

  public void before(Object referer) {
    if (referer == null) {
      return;
    }
    if (this.lastProp == null) {
      this.lastProp = ROOT_OBJECT;
    }

    if (isCollection(referer)) {
      this.walk = new CollectionPathSegment(
          this.walk, lastProp, referer
      );
      this.filter = new CollectionPathSegment(
        this.filter, lastProp, referer
      );
    } else {
      if (!(this.walk instanceof CollectionPathSegment && this.walk.name.equals(lastProp))) {
        this.filter = new PathSegment(this.filter, lastProp, referer);
      }
      this.walk = new PathSegment(
        this.walk, lastProp, referer
      );
    }
    //log.info("before [{}]", this.walkSegment);
  }

  public boolean filter(String name) {
    PathSegment segment = new PathSegment(this.filter, name, this.filter.referer);
    log.info("property [{}] [{}]", name, segment);
    this.lastProp = name;
    return fieldFilter.test(segment);
  }

  public void after() {
    if (walk instanceof CollectionPathSegment collSegment) {
      collSegment.incCurrentIndex();
    }
    //log.info("after [{}]", this.walkSegment);
    if (this.walk != null && walk.parent != null) {
      this.walk = this.walk.parent;
      this.lastProp = this.walk.name;
    } else {
      this.lastProp = ROOT_OBJECT;
    }

    if (filter instanceof CollectionPathSegment collSegment) {
      collSegment.incCurrentIndex();
    }

    if (!(this.walk instanceof CollectionPathSegment && this.walk.name.equals(lastProp))) {
      PathSegment parent = this.filter != null ? this.filter.parent : null;
      if (parent != null) {
        this.filter = parent;
      }
    }
  }
}
