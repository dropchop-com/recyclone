package com.dropchop.recyclone.test.quarkus;

import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 21. 06. 22.
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = LanguageGroup.class, name = "LanguageGroup")
})
public abstract class TagMixIn {
}
