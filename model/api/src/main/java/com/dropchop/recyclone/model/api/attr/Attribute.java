package com.dropchop.recyclone.model.api.attr;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasName;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 17. 12. 21.
 */
public interface Attribute <X> extends Model, HasName {

  X getValue();
}
