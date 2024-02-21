package com.dropchop.recyclone.model.entity.jpa.test;

import com.dropchop.recyclone.model.api.test.Node;
import com.dropchop.recyclone.model.entity.jpa.attr.EAttribute;
import com.dropchop.recyclone.model.entity.jpa.base.ECode;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasEAttributes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.SortedSet;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 05. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
public class ENode extends ECode implements Node<ETitleTranslation, ENode>, HasEAttributes {
  private SortedSet<ENode> children;

  private Set<EAttribute<?>> eAttributes;

  private String title;

  private String lang;

  private ELanguage language;

  private Set<ETitleTranslation> translations;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;

  private EMiki miki;
}
