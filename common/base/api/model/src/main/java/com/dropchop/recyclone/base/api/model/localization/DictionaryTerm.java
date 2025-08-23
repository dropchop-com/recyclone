package com.dropchop.recyclone.base.api.model.localization;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasTags;
import com.dropchop.recyclone.base.api.model.marker.HasTitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasTranslationInlinedTitle;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.tagging.Tag;

public interface DictionaryTerm<T extends Tag<T, TDT>, TT extends TitleTranslation, TDT extends TitleDescriptionTranslation>
    extends Model, HasCode, HasCreated, HasModified, HasTags<T, TDT>, HasTitleTranslation<TT>, HasTranslationInlinedTitle {

}
