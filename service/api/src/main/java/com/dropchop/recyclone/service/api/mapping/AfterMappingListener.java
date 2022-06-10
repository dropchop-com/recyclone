package com.dropchop.recyclone.service.api.mapping;

import com.dropchop.recyclone.model.api.invoke.Params;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 06. 22.
 */
public interface AfterMappingListener<P extends Params>
  extends MappingListener<P> {
}
