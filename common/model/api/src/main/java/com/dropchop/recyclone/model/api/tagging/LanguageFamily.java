package com.dropchop.recyclone.model.api.tagging;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4. 08. 22.
 */
public interface LanguageFamily<T extends Tag<T, TT>, TT extends TitleTranslation> extends NamedTag<T, TT> {
}
