package com.dropchop.recyclone.base.es.model.events;

import com.dropchop.recyclone.base.api.model.event.Event;
import com.dropchop.recyclone.base.es.model.attr.EsAttribute;
import com.dropchop.recyclone.base.es.model.base.EsUuid;
import com.dropchop.recyclone.base.es.model.marker.HasEsAttributes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@Setter
@NoArgsConstructor
public class EsEvent extends EsUuid implements Event<EsEventDetail, EsEventItem, EsEventTrace>, HasEsAttributes {

  private String application;

  private String type;

  private String action;

  private String data;

  private Double value;

  private String unit;

  private EsEventItem source;

  private EsEventItem target;

  private EsEventItem cause;

  private EsEventTrace trace;

  private ZonedDateTime created;

  private Set<EsAttribute<?>> esAttributes = new HashSet<>();
}
