package com.dropchop.recyclone.service.api.invoke;

import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.localization.Translation;
import com.dropchop.recyclone.model.api.marker.*;
import com.dropchop.recyclone.model.api.rest.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 05. 22.
 */
public interface FilteringDtoContextConditions {

  Logger log = LoggerFactory.getLogger(FilteringDtoContextConditions.class);

  static boolean willPropertyNest(FieldFilter.PathSegment segment) {
    boolean willNest = false;
    String propName = segment.name;
    try {
      Method m = segment.referer.getClass().getMethod("get" + propName.substring(0, 1).toUpperCase() + propName.substring(1));
      Class<?> clazz = m.getReturnType();
      if (Entity.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz)) {
        willNest = true;
      }
    } catch (NoSuchMethodException e) {
      log.warn("Unable to find property [{}] getter!", propName, e);
      willNest = true;
    }
    return willNest;
  }

  static boolean isPropertyIdCode(FieldFilter.PathSegment segment) {
    if (segment.referer instanceof HasCode && "code".equals(segment.name)) {
      return true;
    }
    if (segment.referer instanceof HasId && "id".equals(segment.name)) {
      return true;
    }
    return segment.referer instanceof HasUuid && "id".equals(segment.name);
  }

  static boolean isPropertyTitle(FieldFilter.PathSegment segment) {
    return segment.referer instanceof HasTitle && "title".equals(segment.name);
  }

  static boolean isPropertyLang(FieldFilter.PathSegment segment) {
    return segment.referer instanceof HasLanguageCode && "lang".equals(segment.name);
  }

  static boolean isSpecialCollection(FieldFilter.PathSegment segment, boolean translationsOnly) {
    if (segment.referer == null) {
      return false;
    }
    Class<?> currentClass = segment.referer.getClass();
    String propName = segment.name;
    boolean isTranslations = HasTranslation.class.isAssignableFrom(currentClass) && "translations".equals(propName);
    boolean isAttributes = HasAttributes.class.isAssignableFrom(currentClass) && "attributes".equals(propName);
    if (translationsOnly && !isTranslations) {
      return false;
    } else {
      if (!(isTranslations || isAttributes)) {
        return false;
      }
    }
    try {
      Method m = segment.referer.getClass().getMethod("get" + propName.substring(0, 1).toUpperCase() + propName.substring(1));
      Class<?> clazz = m.getReturnType();
      return Collection.class.isAssignableFrom(clazz);
    } catch (NoSuchMethodException e) {
      log.warn("Unable to find property [{}] getter!", propName, e);
    }
    return false;
  }

  static boolean isSpecialClass(Class<?> currentClass, boolean translationsOnly) {
    boolean isTranslation = Translation.class.isAssignableFrom(currentClass);
    if (translationsOnly) {
      return isTranslation;
    }
    boolean isAttribute = Attribute.class.isAssignableFrom(currentClass);
    return isTranslation || isAttribute;
  }

  static boolean isSpecialClass(FieldFilter.PathSegment segment, boolean translationsOnly) {
    if (segment.referer == null) {
      return false;
    }
    return isSpecialClass(segment.referer.getClass(), translationsOnly);
  }

  static boolean isTranslatableInstance(FieldFilter.PathSegment segment) {
    return isObjectTranslatable(segment.referer);
  }

  static boolean isObjectTranslatable(Object current) {
    if (current == null) {
      return false;
    }
    return current instanceof HasTranslation;
  }

  static boolean isDetailForAll(String contentDetail) {
    if (contentDetail == null) {
      return false;
    }
    return contentDetail.startsWith(Constants.ContentDetail.ALL_PREFIX);
  }

  static boolean isDetailForNested(String contentDetail) {
    if (contentDetail == null) {
      return false;
    }
    return contentDetail.startsWith(Constants.ContentDetail.NESTED_PREFIX);
  }
}
