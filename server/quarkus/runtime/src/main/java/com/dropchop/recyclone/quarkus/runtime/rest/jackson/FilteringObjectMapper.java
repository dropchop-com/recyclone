package com.dropchop.recyclone.quarkus.runtime.rest.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 3/27/25.
 */
@RegisterForReflection
public class FilteringObjectMapper extends ObjectMapper {
}
