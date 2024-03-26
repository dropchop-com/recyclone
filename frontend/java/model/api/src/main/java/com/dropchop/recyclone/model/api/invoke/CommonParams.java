package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.invoke.ResultFilter.ContentFilter;
import com.dropchop.recyclone.model.api.invoke.ResultFilter.LanguageFilter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 01. 22.
 */
public interface CommonParams<
  RF extends ResultFilter<CF, LF>,
  CF extends ContentFilter,
  LF extends LanguageFilter,
  FD extends ResultFilterDefaults> extends Params {

  RF getFilter();
  void setFilter(RF resultFilter);

  default RF filter() {
    return getFilter();
  }

  default CommonParams<RF, CF, LF, FD> filter(RF resultFilter) {
    this.setFilter(resultFilter);
    return this;
  }

  FD getFilterDefaults();
}
