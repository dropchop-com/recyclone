package com.dropchop.recyclone.quarkus.it.app;

import com.dropchop.recyclone.model.dto.tagging.LanguageGroup;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Just ignore this for now.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 21. 06. 22.
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type"
)
@JsonSubTypes({
  @JsonSubTypes.Type(value = LanguageGroup.class, name = "LanguageGroup")
})
@SuppressWarnings("unused")
public abstract class TagMixIn {
}
