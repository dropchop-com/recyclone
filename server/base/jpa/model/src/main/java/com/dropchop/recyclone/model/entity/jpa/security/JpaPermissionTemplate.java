package com.dropchop.recyclone.model.entity.jpa.security;

import com.dropchop.recyclone.base.api.model.security.PermissionTemplate;
import com.dropchop.recyclone.model.entity.jpa.localization.JpaTitleDescriptionTranslation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue(value = "Template")
@SuppressWarnings("unused")
public class JpaPermissionTemplate extends JpaPermissionInstance implements PermissionTemplate<JpaPermission,
    JpaTitleDescriptionTranslation, JpaAction, JpaDomain> {

  @Column(name = "sub_subject")
  private String subSubject;

  @Column(name = "sub_subject_uuid")
  private UUID subSubjectId;
}
