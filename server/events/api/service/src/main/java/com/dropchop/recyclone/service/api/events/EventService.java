package com.dropchop.recyclone.service.api.events;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.model.dto.event.Event;
import com.dropchop.recyclone.service.api.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface EventService extends CrudService<Event> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Events.EVENT;
  }
}
