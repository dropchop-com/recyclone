package com.dropchop.recyclone.model.dto.event;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
public class Event extends DtoId
    implements com.dropchop.recyclone.model.api.event.Event<EventDetail, EventItem, EventTrace> {

  private String application;
  private String type;
  private String action;
  private String data;
  private Double value;
  private String unit;
  private EventItem source;
  private EventItem target;
  private EventItem cause;
  private EventTrace trace;
  private ZonedDateTime created;

  @JsonInclude(NON_EMPTY)
  private Set<Attribute<?>> attributes;
}
