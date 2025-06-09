package com.dropchop.recyclone.base.dto.model.base;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 5/22/25.
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
public class Generic implements HasId, Dto {

  @NonNull
  private String id;

  @NonNull
  private String type;

  @NonNull
  @EqualsAndHashCode.Exclude
  private List<String> groups = new ArrayList<>();
}
