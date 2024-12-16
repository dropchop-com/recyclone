package com.dropchop.recyclone.base.api.model.expr;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
public interface Leaf<T> extends Node {
  T getParsedValue();
}
