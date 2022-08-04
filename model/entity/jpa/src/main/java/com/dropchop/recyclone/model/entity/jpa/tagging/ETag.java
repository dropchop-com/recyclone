package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.api.tagging.Tag;
import com.dropchop.recyclone.model.entity.jpa.attr.EAttribute;
import com.dropchop.recyclone.model.entity.jpa.attr.HasEAttributes;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 06. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tag")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
@SuppressWarnings("JpaDataSourceORMInspection")
public class ETag extends EUuid
  implements Tag<ETitleTranslation>,
  HasCreated, HasDeactivated, HasModified, HasELanguage, HasStateInlinedCommon, HasEAttributes {

  @Transient
  private String type = this.getClass().getSimpleName().substring(1);

  @Column(name="title")
  private String title;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, targetEntity = ELanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "tag_fk_language_code"))
  private ELanguage language;

  @ElementCollection
  @CollectionTable(
    name="tag_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_tag_l_fk_language_code_lang", columnNames = {"fk_tag_uuid", "lang"}),
    foreignKey = @ForeignKey(name = "tag_l_fk_tag_uuid"),
    joinColumns = @JoinColumn(name="fk_tag_uuid")
  )
  private Set<ETitleTranslation> translations;

  @ElementCollection
  @CollectionTable(
    name="tag_a",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_tag_a_fk_tag_uuid_name", columnNames = {"fk_tag_uuid", "name"}),
    foreignKey = @ForeignKey(name = "tag_a_fk_tag_uuid"),
    joinColumns = @JoinColumn(name="fk_tag_uuid")
  )
  private Set<EAttribute<?>> eAttributes = new HashSet<>();

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
