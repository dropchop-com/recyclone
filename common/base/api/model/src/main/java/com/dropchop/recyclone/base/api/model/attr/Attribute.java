package com.dropchop.recyclone.base.api.model.attr;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasName;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 17. 12. 21.
 */
public interface Attribute <X> extends Model, HasName {

  X getValue();
}
