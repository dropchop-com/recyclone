package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.Model;
import com.dropchop.recyclone.model.api.State;
import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.api.marker.HasLanguageCode;
import com.dropchop.recyclone.model.api.rest.Constants;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
public interface Params extends Model, HasAttributes<Attribute<?>>, HasLanguageCode {

  String CFIELDS_HEADER = "X-Content-Fields";
  String CLEVEL_HEADER = "X-Content-Level";

  String VERSION_HEADER = "X-Content-Version";
  String LANG_HEADER = "Accept-Language";

  String CFIELDS_QUERY = "c_fields";
  String CLEVEL_QUERY = "c_level";
  String LANG_QUERY = "lang";
  String FROM_QUERY = "from";
  String SIZE_QUERY = "size";
  String STATE_QUERY = "state";
  String SORT_QUERY = "sort";

  int SIZE_QUERY_DEFAULT = 100;
  int FROM_QUERY_DEFAULT = 0;
  int SIZE_QUERY_MIN = 0;
  int FROM_QUERY_MIN = 0;

  default List<String> getAvailableVersions() {
    return List.of("v1.0");
  }
  default List<String> getAvailableLevelOfContentDetails() {
    return List.of(
      Constants.ContentDetail.ALL_OBJS_IDCODE,
      Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE,
      Constants.ContentDetail.NESTED_OBJS_IDCODE,
      Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE
    );
  }

  default Collection<State.Code> getHiddenStates() {
    return Collections.emptySet();
  }

  default String[] getSortFields() {
    return new String[]{};
  }

  String getRequestId();
  void setRequestId(String requestId);

  List<String> getContentIncludes();
  void setContentIncludes(List<String> contentIncludes);

  List<String> getContentExcludes();
  void setContentExcludes(List<String> contentExcludes);

  Integer getContentTreeLevel();
  void setContentTreeLevel(Integer level);

  String getContentDetailLevel();
  void setContentDetailLevel(String level);

  String getTranslationLang();
  void setTranslationLang(String translationLang);

  String getVersion();
  void setVersion(String version);

  int getFrom();
  void setFrom(int from);

  int getSize();
  void setSize(int size);

  List<String> getStates();
  void setStates(List<String> states);

  List<String> getSort();
  void setSort(List<String> states);
}
