package com.dropchop.recyclone.model.dto.rest;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 18. 12. 21.
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Result<T> implements Model, HasId {

  @NonNull
  @JsonInclude(NON_NULL)
  private String id;

  @NonNull
  @EqualsAndHashCode.Exclude
  private ResultStatus status;

  @NonNull
  @EqualsAndHashCode.Exclude
  private List<T> data = new ArrayList<>();
}
