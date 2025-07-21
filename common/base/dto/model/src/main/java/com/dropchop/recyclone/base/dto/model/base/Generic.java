package com.dropchop.recyclone.base.dto.model.base;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.base.Titled;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.base.api.model.marker.HasName;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Represents a generic entity in a domain model with an identifier, type, name,
 * and a list of associated groups. This class implements the contracts defined
 * by HasId, HasName, Dto, and Titled interfaces.
 * <br />
 * The class uses Lombok annotations to reduce boilerplate code for generating
 * getters, setters, and constructors. It also ensures that null values are excluded
 * from JSON serialization.
 * <br />
 * It provides a default behavior to use the `name` property as its title, as defined
 * by the Titled interface.
 * <br />
 * Features:
 * - Implements identifiers and naming conventions via HasId and HasName interfaces.
 * - JSON serialization excludes null fields.
 * - Groups are initialized as an empty list by default.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 22. 5. 2025.
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@JsonInclude(NON_NULL)
public class Generic implements
    HasName, Dto, Titled, HasAttributes, com.dropchop.recyclone.base.api.model.base.Generic {

  @NonNull
  private String id;

  @NonNull
  @EqualsAndHashCode.Exclude
  private String type;

  @EqualsAndHashCode.Exclude
  private String name;

  @NonNull
  @EqualsAndHashCode.Exclude
  private List<String> groups = new ArrayList<>();

  @JsonInclude(NON_EMPTY)
  private Set<Attribute<?>> attributes;

  @Override
  public String getTitle() {
    return getName();
  }

  @Override
  public void setTitle(String title) {
    setName(title);
  }
}
