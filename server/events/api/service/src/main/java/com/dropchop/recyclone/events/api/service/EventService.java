package com.dropchop.recyclone.events.api.service;

import com.dropchop.recyclone.base.api.model.security.Constants;
import com.dropchop.recyclone.base.dto.model.event.Event;
import com.dropchop.recyclone.base.api.service.CrudService;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
public interface EventService extends CrudService<Event> {
  @Override
  default String getSecurityDomain() {
    return Constants.Domains.Events.EVENT;
  }
}
