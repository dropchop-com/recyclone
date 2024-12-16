package com.dropchop.recyclone.base.api.model.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 01. 22.
 */
public interface CommonParams<
  RF extends ResultFilter<CF, LF>,
  CF extends ResultFilter.ContentFilter,
  LF extends ResultFilter.LanguageFilter,
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
