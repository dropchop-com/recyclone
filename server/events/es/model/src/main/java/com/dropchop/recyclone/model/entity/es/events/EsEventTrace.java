package com.dropchop.recyclone.model.entity.es.events;

import com.dropchop.recyclone.model.api.event.EventTrace;
import com.dropchop.recyclone.model.entity.es.base.EsUuid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@Setter
@NoArgsConstructor
public class EsEventTrace extends EsUuid implements EventTrace {

  private String group;
  private String context;
}
