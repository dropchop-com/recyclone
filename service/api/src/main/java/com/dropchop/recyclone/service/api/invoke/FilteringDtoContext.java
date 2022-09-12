package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.filtering.*;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.api.marker.HasTranslation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.TargetPropertyName;

import static com.dropchop.recyclone.model.api.filtering.PathSegment.ROOT_OBJECT;
import static com.dropchop.recyclone.model.api.rest.Constants.ContentDetail.NESTED_PREFIX;
import static com.dropchop.recyclone.model.api.rest.Constants.ContentDetail.TRANS_SUFIX;

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
      // we precompute "dive" for path segment so that, we don't repeat exact same
      // computation for each property in filter(@TargetPropertyName String propName) method
      // (remember: we can't return true or false for continuation here due to Mapstruct API)
      segment
        .dive(filter.dive(segment));
    }
    //log.info("Start object [{}] -> [{}] dive [{}] filter [{}].",
    //  source.getClass().getSimpleName(), segment, segment.dive(), segment.test());
  }

  static boolean isSpecialCollection(PathSegment segment) {
    Class<?> currentClass = segment.referer.getClass();
    String propName = segment.name;
    boolean isTranslations = HasTranslation.class.isAssignableFrom(currentClass) && "translations".equals(propName);
    boolean isAttributes = HasAttributes.class.isAssignableFrom(currentClass) && "attributes".equals(propName);
    if (!(isTranslations || isAttributes)) {
      return false;
    }
    return segment.collectionLike;
  }

  /**
   * Precompute nesting so that we don't dive into ORM collection
   *
   * @param segment current segment
   * @return true if we should dive
   */
  private boolean specialCollectionDive(PathSegment segment) {
    if (this.filter == null) {
      return true;
    }
    Integer treeLevel = this.filter.getTreeLevel();
    if (treeLevel == null || treeLevel == Integer.MAX_VALUE) {
      return true;
    }
    if (segment == null || segment.referer == null) {
      return true;
    }
    String contentDetail = this.filter.getDetailLevel();
    if (contentDetail == null || contentDetail.isBlank()) {
      return true;
    }
    boolean nested = contentDetail.startsWith(NESTED_PREFIX);
    if (!isSpecialCollection(segment)) {
      return nested ?  segment.level < treeLevel + 1: segment.level < treeLevel;
    }

    if (nested) {
      if (contentDetail.contains(TRANS_SUFIX)) {
        return segment.level <= treeLevel + 1;
      } else {
        return true;
      }
    } else {
      if (contentDetail.contains(TRANS_SUFIX)) {
        return segment.level <= treeLevel;
      } else {
        return true;
      }
    }
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

    //referer contains property
    PathSegment segment = PathSegment.fromContainer(curr, propName, curr.referer);

    boolean test = true;
    if (filter != null) {
      test = filter.test(segment);
    }

    if (segment.nestable()) {
      boolean dive = true;
      boolean nest = false;
      if (filter != null) {
        dive = filter.dive(segment);
        // we must precompute nesting so that we don't dive into ORM collection
        nest = filter.nest(segment);
      }
      //log.info("Property [{}] dive [{}] nest [{}].", segment, dive, nest);
      if (nest && dive && specialCollectionDive(segment)) {
        state.pushField(propName);
        return true;
      } else {
        return false;
      }
    }
    //log.info("Property [{}] filter [{}].", segment, test);
    return test;
  }

  public void after(Object ignoredSource, Object target) {
    PathSegment segment = state.pollSegment();
    if (segment.parent instanceof CollectionPathSegment cps) {
      cps.incCurrentIndex();
    }
    //log.info("End object [{}] -> [{}].", ignoredSource.getClass().getSimpleName(), segment);
  }
}
