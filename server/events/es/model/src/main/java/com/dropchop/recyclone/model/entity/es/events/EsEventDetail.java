package com.dropchop.recyclone.model.entity.es.events;

import com.dropchop.recyclone.base.api.model.event.EventDetail;
import com.dropchop.recyclone.model.entity.es.base.EsUuid;
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
public class EsEventDetail extends EsUuid implements EventDetail<EsEventDetail> {

  private String name;
  private EsEventDetail parent;
  private EsEventDetail child;
  private ZonedDateTime created;
}
