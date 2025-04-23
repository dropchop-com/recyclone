package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 06. 22.
 */
public interface Params extends Model, HasAttributes {

  String getRequestId();
  void setRequestId(String requestId);

  List<String> getModifyPolicy();
  void setModifyPolicy(List<String> modifyPolicy);

  String[] getAvailableModifyPolicy();

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
