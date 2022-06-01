package com.dropchop.recyclone.model.entity.jpa.tag;

import com.dropchop.recyclone.model.api.tag.Tag;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.ELanguage;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
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
@DiscriminatorColumn(name="type",
  discriminatorType = DiscriminatorType.STRING)
@SuppressWarnings("JpaDataSourceORMInspection")
public class ETag extends EUuid implements HasELanguage, Tag<ETitleTranslation> {

  @Column(name = "type", insertable = false, updatable = false)
  private String type;

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

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
