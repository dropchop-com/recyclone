package com.dropchop.recyclone.base.api.model.tagging;

import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 08. 22.
 */
public interface LanguageGroup<T extends Tag<T, TT>, TT extends TitleTranslation> extends GroupTag<T, TT> {
}
