package com.dropchop.recyclone.model.entity.jpa.localization;

import com.dropchop.recyclone.model.api.localization.Country;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.entity.jpa.ECode;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

/**
 * Country with ISO 3166 2-letter code.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
@Getter
@Setter
@Entity
@Table(name = "country")
@NoArgsConstructor
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class ECountry extends ECode
  implements HasCreated, HasModified, HasDeactivated, Country<ETitleTranslation> {

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
      name = "uq_country_l_fk_language_code_lang", columnNames = {"fk_country_code", "lang"}),
    foreignKey = @ForeignKey(name = "country_l_fk_country_code"),
    joinColumns = @JoinColumn(name="fk_country_code")
  )
  private Set<ETitleTranslation> translations;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
