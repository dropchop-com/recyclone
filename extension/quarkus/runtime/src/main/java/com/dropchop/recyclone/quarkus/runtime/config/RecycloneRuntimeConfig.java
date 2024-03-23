package com.dropchop.recyclone.quarkus.runtime.spi;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
@ConfigRoot(name = "recyclone", phase = ConfigPhase.RUN_TIME)
public class RecycloneRuntimeConfig {
}
