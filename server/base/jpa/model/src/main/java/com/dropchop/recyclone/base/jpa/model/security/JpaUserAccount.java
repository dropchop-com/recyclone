package com.dropchop.recyclone.base.jpa.model.security;

import com.dropchop.recyclone.base.api.model.marker.HasTitle;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.base.api.model.security.UserAccount;
import com.dropchop.recyclone.base.jpa.model.base.JpaUuid;
import com.dropchop.recyclone.base.jpa.model.localization.JpaCountry;
import com.dropchop.recyclone.base.jpa.model.localization.JpaLanguage;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleDescriptionTranslation;
import com.dropchop.recyclone.base.jpa.model.localization.JpaTitleTranslation;
import com.dropchop.recyclone.base.jpa.model.tagging.JpaTag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_user_account")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
public class JpaUserAccount extends JpaUuid
  implements UserAccount<JpaUser, JpaUserAccount, JpaTitleDescriptionTranslation, JpaTitleTranslation,
  JpaAction, JpaDomain, JpaPermission, JpaRole, JpaCountry, JpaLanguage, JpaTag>,
  HasTitle, HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon {

  @Transient
  private String type = this.getClass().getSimpleName().substring(1);

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "fk_security_user_uuid",
    referencedColumnName = "uuid",
    foreignKey = @ForeignKey(name = "security_user_account_user_uuid_fk"),
    nullable = false)
  private JpaUser user = null;

  @Column(name = "title")
  private String title;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
