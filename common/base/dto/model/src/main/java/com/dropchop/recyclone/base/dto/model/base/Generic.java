package com.dropchop.recyclone.base.dto.model.base;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.marker.HasId;
import com.dropchop.recyclone.base.api.model.marker.HasName;
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
public class Generic implements HasId, HasName, Dto {

  @NonNull
  private String id;

  @NonNull
  @EqualsAndHashCode.Exclude
  private String type;

  @EqualsAndHashCode.Exclude
  private String name;

  @NonNull
  @EqualsAndHashCode.Exclude
  private List<String> groups = new ArrayList<>();
}
