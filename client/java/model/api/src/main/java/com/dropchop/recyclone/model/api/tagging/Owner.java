package com.dropchop.recyclone.model.api.tagging;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;

public interface Owner<T extends Tag<T, TT>, TT extends TitleTranslation> extends NamedTag<T, TT> {
}
