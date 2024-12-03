package com.dropchop.recyclone.model.entity.es.event;

import com.dropchop.recyclone.model.api.event.Event;
import com.dropchop.recyclone.model.api.event.EventDetail;
import com.dropchop.recyclone.model.api.event.EventItem;
import com.dropchop.recyclone.model.entity.es.base.EsUuid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
