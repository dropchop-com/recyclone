package com.dropchop.recyclone.model.api.expr;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.expr.bool.And;
import com.dropchop.recyclone.model.api.expr.bool.Or;
import com.dropchop.recyclone.model.api.expr.operand.Name;
import com.dropchop.recyclone.model.api.expr.operand.Token;
import com.dropchop.recyclone.model.api.expr.parse.DefaultBoolExpressionParser;
import com.dropchop.recyclone.model.api.expr.relational.Eq;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 07. 22.
 */
class ExpressionParserTest {

  @Test
  void parse() throws ParseException {
    ExpressionParser parser = new DefaultBoolExpressionParser();

    String id = UUID.randomUUID().toString();
    String expression = "[(k1 AND [(k2 OR k3)]{ color: yellow, edit})]{view}";
    BinaryTree tree = parser.parse(
      id,
      expression,
      Set.of(
        AttributeString.builder().name("test").value("L'Oreal").build()
      )
    );
    assertEquals(id, tree.getId());
    And root = (And) tree.getRoot();
    assertNotNull(root);

    Eq left = (Eq) root.getLeft();
    assertNotNull(left);
    Name name = (Name) left.getLeft();
    assertNotNull(name);
    assertEquals("text", name.getValue());
    Token token = (Token) left.getRight();
    assertNotNull(token);
    assertEquals("k1", token.getValue());
    assertTrue(token.getAttributes() != null && !token.getAttributes().isEmpty());
    assertEquals(new LinkedHashSet<>(), token.getAttributeValue("view"));

    Or right = (Or) root.getRight();
    Eq eqLeft = (Eq) right.getLeft();

    Name lname = (Name) eqLeft.getLeft();
    assertNotNull(lname);
    assertEquals("text", lname.getValue());
    Token ltoken = (Token) eqLeft.getRight();
    assertNotNull(ltoken);
    assertEquals("k2", ltoken.getValue());
    assertTrue(ltoken.getAttributes() != null && !token.getAttributes().isEmpty());
    assertEquals(new LinkedHashSet<>(), ltoken.getAttributeValue("edit"));
    assertEquals("yellow", ltoken.getAttributeValue("color"));

    Eq eqRight = (Eq) right.getRight();
    Name rname = (Name) eqRight.getLeft();
    assertNotNull(rname);
    assertEquals("text", rname.getValue());
    Token rtoken = (Token) eqRight.getRight();
    assertNotNull(rtoken);
    assertEquals("k3", rtoken.getValue());
    assertTrue(token.getAttributes() != null && !rtoken.getAttributes().isEmpty());
    assertEquals(new LinkedHashSet<>(), rtoken.getAttributeValue("edit"));
    assertEquals("yellow", rtoken.getAttributeValue("color"));
  }
}