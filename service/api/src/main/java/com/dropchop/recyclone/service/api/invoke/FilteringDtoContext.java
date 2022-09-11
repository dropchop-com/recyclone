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
    if (filter != null) { // we precompute filter and dive for path segment.
      segment
        .dive(filter.dive(segment))
        .test(filter.test(segment));
    }

    log.info("Start object [{}] -> [{}] dive [{}] filter [{}].",
      source.getClass().getSimpleName(), segment, segment.dive(), segment.test());
  }

  public boolean filter(@TargetPropertyName String propName) {
    PathSegment curr = state.currentSegment();
    if (!curr.dive() || !curr.test()) {
      return false;
    }

    PathSegment segment = new PathSegment(curr, propName, curr.referer);

    boolean dive = true;
    boolean test = true;
    boolean nest = false;
    if (filter != null) {
      dive = filter.dive(segment);
      test = filter.test(segment);
      nest = FilterSegment.willPropertyNest(segment);
      if (nest) {
        state.pushField(propName);
      }
    }

    log.info("Property [{}] dive [{}] filter [{}] nest [{}].", segment, dive, test, nest);
    if (nest) {
      return dive && test;
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
