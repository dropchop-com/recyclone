package com.dropchop.recyclone.model.entity.jpa.localization;

import com.dropchop.recyclone.model.api.localization.Country;
import com.dropchop.recyclone.model.api.marker.HasTags;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.entity.jpa.base.ECode;
import com.dropchop.recyclone.model.entity.jpa.marker.HasELanguage;
import com.dropchop.recyclone.model.entity.jpa.tagging.ETag;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * Country with ISO 3166 2-letter code.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "country")
@SuppressWarnings("JpaDataSourceORMInspection")
public class ECountry extends ECode
  implements Country<ETitleTranslation>,
  HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon, HasELanguage,
  HasTags<ETag, ETitleTranslation> {

  @Column(name="title")
  private String title;

  @Column(name = "lang", insertable = false, updatable = false)
  private String lang;

  @OneToOne(targetEntity = ELanguage.class)
  @JoinColumn(name = "lang", referencedColumnName = "code", foreignKey = @ForeignKey(name = "country_fk_language_code"))
  private ELanguage language;

  @ElementCollection
  @CollectionTable(
    name="country_l",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_country_l_fk_language_code_lang", columnNames = {"fk_country_code", "lang"}
    ),
    foreignKey = @ForeignKey(name = "country_l_fk_country_code"),
    joinColumns = @JoinColumn(name="fk_country_code")
  )
  private Set<ETitleTranslation> translations;

  @ManyToMany
  @JoinTable(
    name="country_t",
    uniqueConstraints = @UniqueConstraint(
      name = "uq_country_t_fk_country_code_fk_tag_uuid", columnNames = {"fk_country_code", "fk_tag_uuid"}),
    joinColumns = @JoinColumn( name="fk_country_code", foreignKey = @ForeignKey(name = "country_t_fk_country_code")),
    inverseJoinColumns = @JoinColumn( name="fk_tag_uuid", foreignKey = @ForeignKey(name = "country_t_fk_tag_uuid"))
  )
  @OrderColumn(name = "idx")
  private List<ETag> tags;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;

  public ECountry(@NonNull String code) {
    super(code);
  }
}
