package com.dropchop.recyclone.quarkus.it.model.api;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.localization.TitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.base.api.model.marker.HasCode;
import com.dropchop.recyclone.base.api.model.marker.HasTranslationInlinedTitle;
import com.dropchop.recyclone.base.api.model.marker.HasTitleTranslation;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;

import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 05. 22.
 */
public interface Node<TT extends TitleTranslation, N extends Node<TT, N>>
  extends Model, HasCode, HasTranslationInlinedTitle, HasTitleTranslation<TT>,
  HasAttributes, HasCreated, HasModified {

  SortedSet<N> getChildren();
  void setChildren(SortedSet<N> children);
}
