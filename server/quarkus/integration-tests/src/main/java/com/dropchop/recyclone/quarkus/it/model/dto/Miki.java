package com.dropchop.recyclone.quarkus.it.model.dto;

import com.dropchop.recyclone.model.dto.base.DtoCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 09. 22.
 */
@Getter
@Setter
public class Miki extends DtoCode {
  private String test;
}
