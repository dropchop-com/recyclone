package com.dropchop.recyclone.base.api.model.tagging;

import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasName;
import com.dropchop.recyclone.base.api.model.marker.HasUuidV3;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
public interface NamedTag<T extends Tag<T, TT>, TT extends TitleTranslation> extends Tag<T, TT>, HasName, HasUuidV3 {
}
