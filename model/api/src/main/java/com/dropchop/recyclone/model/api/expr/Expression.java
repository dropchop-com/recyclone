package com.dropchop.recyclone.model.api.expr;

import com.dropchop.recyclone.model.api.attr.Attribute;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
@Data
public class Expression implements BinaryTree {

  private String id;

  @EqualsAndHashCode.Exclude
  private Node root;

  @EqualsAndHashCode.Exclude
  private String expression;

  @EqualsAndHashCode.Exclude
  private Set<Attribute<?>> attributes;
}
