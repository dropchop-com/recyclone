package com.dropchop.recyclone.model.api.tag;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasName;
import com.dropchop.recyclone.model.api.marker.HasUuidV3;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
public interface NamedTag<T extends TitleTranslation> extends Tag<T>, HasName, HasUuidV3 {
}
