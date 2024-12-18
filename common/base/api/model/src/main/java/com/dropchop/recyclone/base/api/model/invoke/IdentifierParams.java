package com.dropchop.recyclone.base.api.model.invoke;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@SuppressWarnings("unused")
public interface IdentifierParams<
  RF extends ResultFilter<CF, LF>,
  CF extends ResultFilter.ContentFilter,
  LF extends ResultFilter.LanguageFilter,
  FD extends ResultFilterDefaults> extends CommonParams<RF, CF, LF, FD> {

  List<String> getIdentifiers();
  void setIdentifiers(List<String> identifiers);

  default List<String> ids() {
    return getIdentifiers();
  }

  default IdentifierParams<RF, CF, LF, FD> ids(List<String> ids) {
    setIdentifiers(ids);
    return this;
  }

  default IdentifierParams<RF, CF, LF, FD> id(String id) {
    List<String> ids = getIdentifiers();
    if (ids == null) {
      ids = new ArrayList<>();
      setIdentifiers(ids);
    }
    ids.add(id);
    return this;
  }
}
