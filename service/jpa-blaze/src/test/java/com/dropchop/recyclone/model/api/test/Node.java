package com.dropchop.recyclone.model.api.test;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.localization.TitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.HasEmbeddedTitleTranslation;
import com.dropchop.recyclone.model.api.marker.HasTitleTranslation;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;

import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 05. 22.
 */
public interface Node<T extends TitleTranslation, N extends Node<T, N>>
  extends Model, HasCode, HasEmbeddedTitleTranslation, HasTitleTranslation<T>, HasAttributes, HasCreated, HasModified {

  SortedSet<N> getChildren();
  void setChildren(SortedSet<N> children);
}
