package com.dropchop.recyclone.model.dto.base;

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
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class DtoCode implements Dto, ModelWithCode, Comparable<ModelWithCode> {

  @NonNull
  @JsonInclude(NON_NULL)
  private String code;

  @Override
  @SuppressWarnings("ConstantConditions")
  public int compareTo(@NonNull ModelWithCode o) {
    String _code = this.getCode();
    String ocode = o != null ? o.getCode() : null;
    if (_code == null && ocode != null) {
      return -1;
    }
    if (_code != null && ocode == null) {
      return 1;
    }
    if (_code == null && ocode == null) {
      return 0;
    }
    return _code.compareTo(ocode);
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + ":" + this.getCode();
  }
}
