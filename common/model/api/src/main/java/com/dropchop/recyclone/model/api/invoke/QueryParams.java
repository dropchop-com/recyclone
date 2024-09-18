package com.dropchop.recyclone.model.api.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 09. 24.
 */
public interface QueryParams<
    RF extends ResultFilter<CF, LF>,
    CF extends ResultFilter.ContentFilter,
    LF extends ResultFilter.LanguageFilter,
    FD extends ResultFilterDefaults> extends CommonParams<RF, CF, LF, FD> {
}
