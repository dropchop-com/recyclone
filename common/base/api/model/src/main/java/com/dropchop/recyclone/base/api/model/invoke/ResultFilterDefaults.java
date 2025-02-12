package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.base.State;
import com.dropchop.recyclone.base.api.model.rest.Constants;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 31. 08. 22.
 */
@SuppressWarnings("unused")
public interface ResultFilterDefaults extends Model {

  default int getSize() {
    return 100;
  }

  default int getFrom() {
    return 0;
  }

  default int getSizeMin() {
    return 0;
  }

  default int getFromMin() {
    return 0;
  }

  default int getTreeLevel() {
    return 1;
  }

  default String getDetailLevel() {
    return Constants.ContentDetail.NESTED_OBJS_IDCODE;
  }

  default List<String> getAvailableVersions() {
    return List.of("v1.0");
  }

  default List<String> getAvailableLevelOfContentDetails() {
    return List.of(
      Constants.ContentDetail.ALL_OBJS_IDCODE,
      Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE,
      Constants.ContentDetail.ALL_OBJS_IDCODE_TITLE_TRANS,
      Constants.ContentDetail.NESTED_OBJS_IDCODE,
      Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE,
      Constants.ContentDetail.NESTED_OBJS_IDCODE_TITLE_TRANS
    );
  }

  default Collection<State.Code> getAvailableHiddenStates() {
    return Collections.emptySet();
  }

  default String[] getAvailableSortFields() {
    return new String[]{};
  }
}
