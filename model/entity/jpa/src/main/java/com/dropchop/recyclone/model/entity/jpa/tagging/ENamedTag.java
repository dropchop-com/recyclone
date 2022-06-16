package com.dropchop.recyclone.model.entity.jpa.tagging;

import com.dropchop.recyclone.model.api.tagging.NamedTag;
import com.dropchop.recyclone.model.entity.jpa.localization.ETitleTranslation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 1. 06. 22.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@SuppressWarnings("JpaDataSourceORMInspection")
public class ENamedTag extends ETag implements NamedTag<ETitleTranslation> {

  @Column(name="name")
  private String name;
}
