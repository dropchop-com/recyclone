package com.dropchop.recyclone.base.dto.model.event;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
public class EventTrace implements com.dropchop.recyclone.base.api.model.event.EventTrace, Dto {

  private String name;
  private String group;
  private String context;

}
