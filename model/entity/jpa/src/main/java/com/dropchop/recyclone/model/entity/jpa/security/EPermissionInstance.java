package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.PermissionInstance;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_permission_details")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(value = "Instance")
public class EPermissionInstance extends EUuid implements PermissionInstance<EPermission,
  ETitleDescriptionTranslation, EAction, EDomain> {

  @ManyToOne
  @JoinColumn(name = "fk_permission_uuid", referencedColumnName = "uuid", foreignKey = @ForeignKey(name = "security_permission_detail_permission_fk"))
  private EPermission permission;


  @Column(name = "subject")
  private String subject;


  @Column(name = "subject_uuid")
  private UUID subjectId;


  @Column(name = "allowed")
  private Boolean allowed;


  @Column(name = "created")
  private ZonedDateTime created;


  @Column(name = "modified")
  private ZonedDateTime modified;
}
