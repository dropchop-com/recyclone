package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.model.api.security.PermissionTemplate;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleDescriptionTranslation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_permission_template")
public class EPermissionTemplate extends EPermissionInstance implements PermissionTemplate<EPermission,
  ETitleDescriptionTranslation, EAction, EDomain> {

  @Column(name = "sub_subject")
  private String subSubject;

  @Column(name = "sub_subject_uuid")
  private UUID subSubjectId;

}
