package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.PermissionInstance;
import com.dropchop.recyclone.model.entity.jpa.base.EUuid;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_permission_instance")
public class EPermissionInstance extends EUuid implements PermissionInstance<EPermission,
  ETitleDescriptionTranslation, EAction, EDomain> {

  @ManyToOne
  @JoinColumn(name = "fk_permission_uuid", referencedColumnName = "uuid", foreignKey = @ForeignKey(name = "permission_fk"))
  private EPermission permission;


  @Column(name = "subject")
  private String subject;


  @Column(name = "subject_uuid")
  private UUID subjectId;


  @Column(name = "allowed")
  private Boolean allowed;
}
