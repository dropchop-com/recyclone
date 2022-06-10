package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasEmbeddedTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;

public interface RoleGroup<T extends TitleTranslation,
    A extends Action<T>, D extends Domain<T, A>, P extends Permission<T, A, D>, R extends Role<T, A, D, P>>
    extends Model, HasCode, HasEmbeddedTitleTranslation, HasTitleTranslation<T> {
}
