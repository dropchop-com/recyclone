package com.dropchop.recyclone.model.dto.test;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.marker.HasTags;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.dropchop.recyclone.model.dto.localization.TitleDescriptionTranslation;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.dropchop.recyclone.model.dto.tagging.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 05. 22.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@JsonInclude(NON_NULL)
public class Node extends DtoCode
  implements com.dropchop.recyclone.model.api.test.Node<TitleTranslation, Node>,
  HasTags<Tag, TitleDescriptionTranslation> {

  @JsonInclude(NON_EMPTY)
  SortedSet<Node> children;

  private String title;

  private String lang;

  @JsonInclude(NON_EMPTY)
  private Set<TitleTranslation> translations = new LinkedHashSet<>();

  @JsonInclude(NON_EMPTY)
  private Set<Attribute<?>> attributes = new LinkedHashSet<>();

  @JsonInclude(NON_EMPTY)
  private List<Tag> tags = new ArrayList<>();

  private ZonedDateTime created;

  private ZonedDateTime modified;

  private ZonedDateTime deactivated;
}
