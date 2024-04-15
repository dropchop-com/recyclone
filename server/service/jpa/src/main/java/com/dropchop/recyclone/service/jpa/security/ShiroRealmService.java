package com.dropchop.recyclone.service.jpa.security;

import com.dropchop.recyclone.service.api.ServiceType;
import jakarta.enterprise.context.ApplicationScoped;

import static com.dropchop.recyclone.model.api.marker.Constants.Implementation.RECYCLONE_JPA_DEFAULT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 03. 22.
 */
@ApplicationScoped
@ServiceType(RECYCLONE_JPA_DEFAULT)
public class ShiroRealmService {
}
