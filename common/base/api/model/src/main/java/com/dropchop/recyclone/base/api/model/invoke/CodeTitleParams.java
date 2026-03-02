package com.dropchop.recyclone.base.api.model.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 01. 22.
 */
@SuppressWarnings("unused")
public interface CodeTitleParams<
  RF extends ResultFilter<CF, LF>,
  CF extends ResultFilter.ContentFilter,
  LF extends ResultFilter.LanguageFilter,
  FD extends ResultFilterDefaults> extends CodeParams<RF, CF, LF, FD> {

  String getTitle();
  void setTitle(String title);

  default CodeTitleParams<RF, CF, LF, FD> title(String title) {
    setTitle(title);
    return this;
  }

  default String title() {
    return getTitle();
  }
}
