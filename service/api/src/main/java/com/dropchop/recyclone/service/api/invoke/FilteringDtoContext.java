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
    String curr = state.pollField();
    state.pushObject(source);
    PathSegment parent = state.currentSegment();
    PathSegment segment;
    boolean isCollection = FilterSegment.isCollection(source);
    if (isCollection) {
      segment = new CollectionPathSegment(parent, curr == null ? ROOT_OBJECT : curr,
        parent != null ? parent.referer : source);
    } else {
      segment = new PathSegment(parent, curr == null ? ROOT_OBJECT : curr, source);
    }
    if (segment.parent instanceof CollectionPathSegment && !isCollection) {
      segment = segment.parent;
    }
    state.pushSegment(segment);

    boolean dive = true;
    if (filter != null) {
      dive = filter.dive(segment);
      if (!dive) {
        segment.dive(false);
      }
    }
    log.info("Start object [{}] -> [{}] dive [{}].", source.getClass().getSimpleName(), segment, dive);
  }

  public boolean filter(@TargetPropertyName String propName) {
    PathSegment curr = state.currentSegment();
    if (!curr.dive()) {
      return false;
    }
    if (curr.parent instanceof CollectionPathSegment) {
      curr = curr.parent;
    }
    PathSegment segment = new PathSegment(curr, propName, state.currentObject());

    boolean dive = true;
    boolean test = true;
    if (filter != null) {
      dive = filter.dive(segment);
      test = filter.test(segment);
      if (!dive) {
        segment.dive(false);
      } else {
        state.pushField(propName);
      }
    }


    log.info("Property [{}] dive [{}] filter [{}]", segment, dive, test);
    return test;
  }

  public void after(Object ignoredSource, Object target) {
    state.pollObject();
    PathSegment segment = state.pollSegment();
    if (segment.parent instanceof CollectionPathSegment cps) {
      cps.incCurrentIndex();
    }
    log.info("End object [{}] -> [{}].", ignoredSource.getClass().getSimpleName(), segment);
  }
}
