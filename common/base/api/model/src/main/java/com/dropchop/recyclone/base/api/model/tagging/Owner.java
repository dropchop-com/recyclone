package com.dropchop.recyclone.base.api.model.tagging;

import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;

public interface Owner<T extends Tag<T, TT>, TT extends TitleTranslation> extends NamedTag<T, TT> {
}
