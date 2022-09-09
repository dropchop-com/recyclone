package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.filtering.CollectionPathSegment;
import com.dropchop.recyclone.model.api.filtering.FieldFilter;
import com.dropchop.recyclone.model.api.filtering.FilteringState;
import com.dropchop.recyclone.model.api.filtering.PathSegment;
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

  private boolean isCollection(Object obj) {
    return obj instanceof Collection<?> || (obj!=null && obj.getClass().isArray());
  }

  public void before(Object source, Object ignoredTarget) {
    String curr = state.pollField();
    state.pushObject(source);
    PathSegment parent = state.currentSegment();
    PathSegment segment;
    if (isCollection(source)) {
      segment = new CollectionPathSegment(parent, curr == null ? ROOT_OBJECT : curr,
        parent != null ? parent.referer : source);
    } else {
      segment = new PathSegment(parent, curr == null ? ROOT_OBJECT : curr, source);
    }
    state.pushSegment(segment);
    if (parent instanceof CollectionPathSegment cps) {
      cps.incCurrentIndex();
    }

    log.info("Start object [{}] -> [{}] dive [{}].", source, segment, filter.dive(segment));
    if (filter != null) {
      boolean dive = filter.dive(segment);
      if (!dive) {
        segment.dive(false);
      }
    }
  }

  public boolean filter(@TargetPropertyName String propName) {
    PathSegment parent = state.currentSegment();
    if (!parent.dive()) {
      return false;
    }
    PathSegment segment;
    if (parent instanceof CollectionPathSegment) {
      segment = parent;
    } else {
      segment = new PathSegment(parent, propName, state.currentObject());
    }

    if (filter != null) {
      boolean dive = filter.dive(segment);
      if (!dive) {
        segment.dive(false);
      } else {
        state.pushField(propName);
      }
    }
    log.info("Property [{}]", segment);
    return filter.test(segment);
  }

  public void after(Object ignoredSource, Object target) {
    state.pollObject();
    PathSegment segment = state.pollSegment();
    log.info("End object [{}] -> [{}] dive [{}].", ignoredSource, segment, filter.dive(segment));
  }
}
