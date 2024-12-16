package com.dropchop.recyclone.base.api.model.expr;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@SuppressWarnings("unused")
public interface BinaryTree extends HasAttributes, Model {
  String getId();
  void setId(String id);

  String getExpression();
  void setExpression(String expression);

  Node getRoot();
  void setRoot(Node node);
}
