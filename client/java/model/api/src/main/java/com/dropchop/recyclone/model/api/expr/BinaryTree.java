package com.dropchop.recyclone.model.api.expr;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.marker.HasAttributes;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
public interface BinaryTree extends HasAttributes, Model {
  String getId();
  void setId(String id);

  String getExpression();
  void setExpression(String expression);

  Node getRoot();
  void setRoot(Node node);
}
