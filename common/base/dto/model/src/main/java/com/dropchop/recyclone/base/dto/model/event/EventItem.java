package com.dropchop.recyclone.base.dto.model.event;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.dto.model.base.DtoId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
public class EventItem implements com.dropchop.recyclone.base.api.model.event.EventItem<EventDetail>, Dto {

  private EventDetail subject;
  private EventDetail object;
  private EventDetail service;
  private EventDetail context;
}
