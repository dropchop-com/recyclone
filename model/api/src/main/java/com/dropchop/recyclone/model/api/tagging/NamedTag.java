package com.dropchop.recyclone.model.api.tagging;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasName;
import com.dropchop.recyclone.model.api.marker.HasUuidV3;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface NamedTag<TT extends TitleTranslation> extends Tag<TT>, HasName, HasUuidV3 {
}
