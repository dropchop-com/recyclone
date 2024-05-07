package com.dropchop.recyclone.quarkus.it.model.entity.jpa;

import com.dropchop.recyclone.model.entity.jpa.base.JpaCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 09. 22.
 */
@Setter
@Getter
public class JpaMiki extends JpaCode {
  private String test;
}
