package com.dropchop.recyclone.model.api.expr;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.marker.HasAttributes;
import com.dropchop.recyclone.model.api.marker.HasId;


/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 11. 21.
 */
public interface Node extends Model, HasId, HasAttributes<Attribute<?>> {

  Position getPosition();
  void setPosition(Position position);

  String getValue();
  void setValue(String value);

  Node getParent();
  void setParent(Node node);

  BinaryTree getTree();
  void setTree(BinaryTree tree);
}
