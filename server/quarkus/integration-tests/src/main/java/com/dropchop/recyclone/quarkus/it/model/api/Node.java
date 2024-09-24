package com.dropchop.recyclone.quarkus.it.model.api;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasTranslationInlinedTitle;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;

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
