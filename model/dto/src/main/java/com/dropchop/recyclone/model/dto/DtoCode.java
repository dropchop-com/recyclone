package com.dropchop.recyclone.model.dto;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.ModelWithCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DtoCode implements Dto, ModelWithCode, Comparable<DtoCode> {

  @NonNull
  @JsonInclude(NON_NULL)
  private String code;

  @Override
  public int compareTo(@NonNull DtoCode o) {
    /*if (this.code == null && o.code != null) {
      return -1;
    }
    if (this.code != null && o.code == null) {
      return 1;
    }
    if (this.code == null && o.code == null) {
      return 0;
    }*/
    return this.code.compareTo(o.code);
  }
}
