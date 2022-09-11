package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.filtering.*;
import com.dropchop.recyclone.model.api.invoke.Params;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.TargetPropertyName;

import java.util.Collection;

import static com.dropchop.recyclone.model.api.filtering.PathSegment.ROOT_OBJECT;

/**
 * MappingContext that can filter (include/exclude) object graph paths based on REST client parameters.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 30. 04. 22.
 */
@Slf4j
public class FilteringDtoContext extends MappingContext {

  private FieldFilter filter = null;
  private FilteringState state = new FilteringState();

  @Override
  public void setParams(@NonNull Params params) {
    super.setParams(params);
    filter = FieldFilter.fromParams(params);
    state = new FilteringState();
  }

  @Override
  public FilteringDtoContext params(Params params) {
    this.setParams(params);
    return this;
  }

  public void before(Object source, Object ignoredTarget) {
    if (source == null) {
      return;
    }
    String curr = state.pollField();
    PathSegment parent = state.currentSegment();
    boolean isCollection = FilterSegment.isCollection(source);
    PathSegment segment;
    if (parent instanceof CollectionPathSegment) {
      if (isCollection) {
        segment = new CollectionPathSegment(parent, null, source);
      } else {
        segment = new PathSegment(parent, null, source);
      }
    } else {
      if (isCollection) {
        segment = new CollectionPathSegment(parent, curr == null ? ROOT_OBJECT : curr, source);
      } else {
        segment = new PathSegment(parent, curr == null ? ROOT_OBJECT : curr, source);
      }
    }
    state.pushSegment(segment);
    if (filter != null) {
      // we precompute "filter" and "dive" for path segment so that,
      // we don't repeat exact same computation for each property
      // (remember: we can't return true or false for continuation here due to Mapstruct API)
      segment
        .dive(filter.dive(segment))
        .test(filter.test(segment));
    }

    log.info("Start object [{}] -> [{}] dive [{}] filter [{}].",
      source.getClass().getSimpleName(), segment, segment.dive(), segment.test());
  }

  /**
   * Test if property can be mapped
   *
   * @param propName property name
   * @return true if property is ok false if not.
   */
  public boolean filter(@TargetPropertyName String propName) {
    PathSegment curr = state.currentSegment();
    // property containing object passes?
    if (!curr.dive()) {
      return false;
    }

    PathSegment segment = new PathSegment(curr, propName, curr.referer);

    boolean dive = true;
    boolean test = true;
    boolean nest = false;
    if (filter != null) {
      dive = filter.dive(segment);
      test = filter.test(segment);
      // we must precompute nesting so that we don't dive into ORM collection
      nest = filter.nest(segment);
    }

    log.info("Property [{}] dive [{}] filter [{}:{}] nest [{}].", segment, dive, test, curr.test(), nest);
    if (nest) {
      if (dive) {
        state.pushField(propName);
        return true;
      }
      return false;
    }
    return test;
  }

  public void after(Object ignoredSource, Object target) {
    PathSegment segment = state.pollSegment();
    if (segment.parent instanceof CollectionPathSegment cps) {
      cps.incCurrentIndex();
    }

    log.info("End object [{}] -> [{}].", ignoredSource.getClass().getSimpleName(), segment);
    /*segment = state.currentSegment();
    if (segment != null) {
      this.lastProp = segment.name;
    } else {
      this.lastProp = ROOT_OBJECT;
    }*/
  }
}
