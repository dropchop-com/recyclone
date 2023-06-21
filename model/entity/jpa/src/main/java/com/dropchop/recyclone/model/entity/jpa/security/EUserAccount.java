package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.marker.HasTitle;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;
import com.dropchop.recyclone.model.api.marker.state.HasModified;
import com.dropchop.recyclone.model.api.marker.state.HasStateInlinedCommon;
import com.dropchop.recyclone.model.api.security.UserAccount;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "security_user_account")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
public class EUserAccount extends EUuid
  implements UserAccount,
  HasTitle, HasCreated, HasModified, HasDeactivated, HasStateInlinedCommon {

  @Transient
  private String type = this.getClass().getSimpleName().substring(1);

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fk_security_user_uuid", foreignKey = @ForeignKey(name = "security_user_account_user_uuid_fk"), nullable = false)
  private EUser<?> user = null;

  @Column(name = "title")
  private String title;

  @Column(name="created")
  private ZonedDateTime created;

  @Column(name="modified")
  private ZonedDateTime modified;

  @Column(name="deactivated")
  private ZonedDateTime deactivated;
}
