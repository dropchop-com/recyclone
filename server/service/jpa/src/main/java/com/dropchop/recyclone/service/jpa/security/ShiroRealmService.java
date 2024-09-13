package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.service.api.RecycloneType;
import jakarta.enterprise.context.ApplicationScoped;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 03. 22.
 */
@ApplicationScoped
@RecycloneType(RECYCLONE_DEFAULT)
public class ShiroRealmService {
}
