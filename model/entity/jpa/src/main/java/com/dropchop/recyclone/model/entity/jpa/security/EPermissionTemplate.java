package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.PermissionTemplate;
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
@Table(name = "security_permission_template")
public class EPermissionTemplate extends EUuid implements PermissionTemplate<EPermission,
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

  @Column(name = "sub_subject")
  private String subSubject;

  @Column(name = "sub_subject_uuid")
  private UUID subSubjectId;

}
