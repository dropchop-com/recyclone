package com.dropchop.recyclone.quarkus.it.model.api;

import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.tagging.NamedTag;
import com.dropchop.recyclone.base.api.model.tagging.Tag;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 4/18/25.
 */
public interface DummyTag<T extends Tag<T, TT>, TT extends TitleTranslation> extends NamedTag<T, TT> {
}
