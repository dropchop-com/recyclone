package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasAttributes;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 06. 22.
 */
public interface Params extends Model, HasAttributes {

  String getRequestId();
  void setRequestId(String requestId);

  default ResultFilter<?, ?> tryGetResultFilter() {
    if (this instanceof CommonParams<?,?,?,?> commonParams) {
      return commonParams.getFilter();
    }
    return null;
  }

  default ResultFilterDefaults tryGetFilterDefaults() {
    if (this instanceof CommonParams<?,?,?,?> commonParams) {
      return commonParams.getFilterDefaults();
    }
    return null;
  }

  default ResultFilter.ContentFilter tryGetResultContentFilter() {
    ResultFilter<?, ?> resultFilter = tryGetResultFilter();
    if (resultFilter != null) {
      return resultFilter.getContent();
    }
    return null;
  }

  default ResultFilter.LanguageFilter tryGetResultLanguageFilter() {
    ResultFilter<?, ?> resultFilter = tryGetResultFilter();
    if (resultFilter != null) {
      return resultFilter.getLang();
    }
    return null;
  }
}
