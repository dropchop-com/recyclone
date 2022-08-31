package com.dropchop.recyclone.model.api.invoke;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
public interface TypeParams<
  RF extends ResultFilter<CF, LF>,
  CF extends ResultFilter.ContentFilter,
  LF extends ResultFilter.LanguageFilter,
  FD extends ResultFilterDefaults> extends CommonParams<RF, CF, LF, FD> {

  List<String> getTypes();
  void setTypes(List<String> types);

  default List<String> types() {
    return getTypes();
  }

  default TypeParams<RF, CF, LF, FD> types(List<String> types) {
    setTypes(types);
    return this;
  }

  default TypeParams<RF, CF, LF, FD> type(String type) {
    List<String> types = getTypes();
    if (types == null) {
      types = new ArrayList<>();
      setTypes(types);
    }
    types.add(type);
    return this;
  }
}
