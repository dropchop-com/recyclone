package com.dropchop.recyclone.quarkus.deployment.registry;

import com.dropchop.shiro.cdi.DefaultShiroEnvironmentProvider;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@Alternative
@Priority(1)
@ApplicationScoped
public class TestShiroEnvironmentProvider extends DefaultShiroEnvironmentProvider {
}
