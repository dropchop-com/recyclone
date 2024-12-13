package com.dropchop.recyclone.base.api.model.security;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasTitle;
import com.dropchop.recyclone.base.api.model.marker.HasType;
import com.dropchop.recyclone.base.api.model.marker.HasUuid;
import com.dropchop.recyclone.base.api.model.marker.state.HasDeactivated;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 01. 22.
 */
public interface Account extends Model, HasUuid,  HasType, HasTitle, HasDeactivated {
}
