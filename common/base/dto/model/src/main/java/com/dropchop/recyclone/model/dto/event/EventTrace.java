package com.dropchop.recyclone.model.dto.event;

import com.dropchop.recyclone.model.dto.base.DtoId;
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
public class EventTrace extends DtoId implements com.dropchop.recyclone.base.api.model.event.EventTrace {

  private String group;
  private String context;

}
