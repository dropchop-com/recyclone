package com.dropchop.recyclone.quarkus.it.model.entity.jpa;

import com.dropchop.recyclone.quarkus.it.model.api.Node;
import com.dropchop.recyclone.model.entity.jpa.attr.JpaAttribute;
import com.dropchop.recyclone.model.entity.jpa.base.JpaCode;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaAttributes;
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
public class JpaNode extends JpaCode implements Node<JpaTitleTranslation, JpaNode>, HasJpaAttributes {
  private SortedSet<JpaNode> children;

  private Set<JpaAttribute<?>> jpaAttributes;

  private String title;

  private String lang;

  private JpaLanguage language;

  private Set<JpaTitleTranslation> translations;

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;

  private JpaMiki miki;
}
