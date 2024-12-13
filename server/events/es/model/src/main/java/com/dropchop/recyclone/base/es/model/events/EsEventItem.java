package com.dropchop.recyclone.base.es.model.events;

import com.dropchop.recyclone.base.api.model.event.EventItem;
import com.dropchop.recyclone.base.es.model.base.EsUuid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@Setter
@NoArgsConstructor
public class EsEventItem extends EsUuid implements EventItem<EsEventDetail> {

  private EsEventDetail subject;
  private EsEventDetail object;
  private EsEventDetail service;
  private EsEventDetail context;
  private ZonedDateTime created;
}
