package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.api.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.attr.JpaAttribute;
import com.dropchop.recyclone.model.entity.jpa.base.JpaUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaLanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaAttributes;
import com.dropchop.recyclone.model.entity.jpa.marker.HasJpaLanguage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 1. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tag")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
@SuppressWarnings("JpaDataSourceORMInspection")
public class JpaTag extends JpaUuid
  implements Tag<JpaTag, JpaTitleDescriptionTranslation>,
  HasCreated, HasDeactivated, HasModified, HasStateInlinedCommon, HasJpaLanguage, HasJpaAttributes {

  @Column(name="type", insertable = false, updatable = false)
  private String type = this.getClass().getSimpleName().substring(1);

  @Column(name="title")
  private String title;

  @Column(name="description")
  private String description;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = JpaLanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "tag_fk_language_code"))
  private JpaLanguage language;

  @ElementCollection
  @CollectionTable(
    name="tag_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_tag_l_fk_language_code_lang", columnNames = {"fk_tag_uuid", "lang"}),
    foreignKey = @ForeignKey(name = "tag_l_fk_tag_uuid"),
    joinColumns = @JoinColumn(name="fk_tag_uuid")
  )
  private Set<JpaTitleDescriptionTranslation> translations;

  @ElementCollection
  @CollectionTable(
    name="tag_a",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_tag_a_fk_tag_uuid_name", columnNames = {"fk_tag_uuid", "name"}),
    foreignKey = @ForeignKey(name = "tag_a_fk_tag_uuid"),
    joinColumns = @JoinColumn(name="fk_tag_uuid")
  )
  private Set<JpaAttribute<?>> jpaAttributes = new HashSet<>();

  @ManyToMany
  @JoinTable(
    name="tag_t",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_tag_t_fk_tag_uuid_fk_next_tag_uuid", columnNames = {"fk_tag_uuid", "fk_next_tag_uuid"}),
    joinColumns = @JoinColumn( name="fk_tag_uuid", foreignKey = @ForeignKey(name = "tag_t_fk_tag_uuid")),
    inverseJoinColumns = @JoinColumn( name="fk_next_tag_uuid", foreignKey = @ForeignKey(name = "tag_t_fk_next_tag_uuid"))
  )
  @OrderColumn(name = "idx")
  private List<JpaTag> tags;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
