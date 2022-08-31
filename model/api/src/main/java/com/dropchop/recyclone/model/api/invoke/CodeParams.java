package com.dropchop.recyclone.model.api.invoke;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
public interface CodeParams<
  RF extends ResultFilter<CF, LF>,
  CF extends ResultFilter.ContentFilter,
  LF extends ResultFilter.LanguageFilter,
  FD extends ResultFilterDefaults> extends CommonParams<RF, CF, LF, FD> {

  List<String> getCodes();
  void setCodes(List<String> identifiers);

  default List<String> codes() {
    return getCodes();
  }

  default CodeParams<RF, CF, LF, FD> codes(List<String> codes) {
    setCodes(codes);
    return this;
  }

  default CodeParams<RF, CF, LF, FD> code(String code) {
    List<String> codes = getCodes();
    if (codes == null) {
      codes = new ArrayList<>();
      setCodes(codes);
    }
    codes.add(code);
    return this;
  }
}
