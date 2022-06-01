package com.dropchop.recyclone.model.dto.base;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.ModelWithId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class DtoId implements Dto, ModelWithId {

  @NonNull
  @JsonInclude(NON_NULL)
  private String id;

  @JsonIgnore
  @EqualsAndHashCode.Exclude
  private UUID uuid;
}
