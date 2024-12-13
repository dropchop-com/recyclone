package com.dropchop.recyclone.base.api.model.expr;

import com.dropchop.recyclone.base.api.model.attr.Attribute;

import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
public interface ExpressionParser {

  /**
   * Parses a boolean expression character sequence to a binary expression tree representation.
   *
   * @param id expression identifier (used as a base for each binary tree node id)
   * @param expression boolean expression represented as character sequence
   * @param globals set of global attributes per expression contained in result - BinaryTree
   *
   * @return BinaryTree representation with reference to root node, id, global attributes.
   * Each node in a tree has a reference to a BinaryTree.
   *
   * @throws ParseException thrown if expression is invalid. It contains error code and character
   * sequence position where the error occurred.
   */
  BinaryTree parse(String id, CharSequence expression, Set<Attribute<?>> globals) throws ParseException;
  BinaryTree parse(String id, CharSequence expression) throws ParseException;
  BinaryTree parse(CharSequence expression) throws ParseException;
  //CharSequence print(SearchExpression searchExpression);
}
