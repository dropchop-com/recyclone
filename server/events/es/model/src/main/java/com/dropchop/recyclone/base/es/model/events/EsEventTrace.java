package com.dropchop.recyclone.base.es.model.events;

import com.dropchop.recyclone.base.api.model.event.EventTrace;
import com.dropchop.recyclone.base.es.model.base.EsEntity;
import com.dropchop.recyclone.base.es.model.base.EsUuid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Armando Ota <armando.ota@dropchop.com> on 3. 12. 24.
 */
@Getter
@Setter
@NoArgsConstructor
public class EsEventTrace implements EventTrace, EsEntity {

  private String name;
  private String group;
  private String context;

}
