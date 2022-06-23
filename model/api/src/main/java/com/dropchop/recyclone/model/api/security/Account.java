package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasTitle;
import com.dropchop.recyclone.model.api.marker.HasUuid;
import com.dropchop.recyclone.model.api.marker.state.HasDeactivated;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 01. 22.
 */
public interface Account extends Model, HasUuid, HasTitle, HasDeactivated {
}
