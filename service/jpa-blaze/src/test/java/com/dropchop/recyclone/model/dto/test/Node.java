package com.dropchop.recyclone.model.dto.test;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.dto.base.DtoCode;
import com.dropchop.recyclone.model.dto.localization.TitleTranslation;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.SortedSet;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 9. 05. 22.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Node extends DtoCode
  implements com.dropchop.recyclone.model.api.test.Node<TitleTranslation, Node> {
  @JsonInclude(NON_NULL)
  SortedSet<Node> children;

  @JsonInclude(NON_NULL)
  private String title;

  @JsonInclude(NON_NULL)
  private String lang;

  @JsonInclude(NON_NULL)
  private Set<TitleTranslation> translations;

  @JsonInclude(NON_NULL)
  private Set<Attribute<?>> attributes;

  @JsonInclude(NON_NULL)
  private ZonedDateTime created;

  @JsonInclude(NON_NULL)
  private ZonedDateTime modified;

  @JsonInclude(NON_NULL)
  private ZonedDateTime deactivated;
}
