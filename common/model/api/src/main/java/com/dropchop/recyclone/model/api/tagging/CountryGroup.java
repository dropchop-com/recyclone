package com.dropchop.recyclone.model.api.tagging;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 4. 08. 22.
 */
public interface CountryGroup<T extends Tag<T, TT>, TT extends TitleTranslation> extends NamedTag<T, TT> {
}
