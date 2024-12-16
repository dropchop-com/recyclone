package com.dropchop.recyclone.base.api.model.expr;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasAttributes;
import com.dropchop.recyclone.base.api.model.marker.HasId;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@SuppressWarnings("unused")
public interface Node extends Model, HasId, HasAttributes {

  Position getPosition();
  void setPosition(Position position);

  String getValue();
  void setValue(String value);

  Node getParent();
  void setParent(Node node);

  BinaryTree getTree();
  void setTree(BinaryTree tree);
}
