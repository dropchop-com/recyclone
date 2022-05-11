package com.dropchop.recyclone.model.api.expr.parse;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.expr.Node;
import com.dropchop.recyclone.model.api.expr.ReservedSymbols;
import lombok.ToString;

import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 27. 11. 21.
 */
@ToString
public class ParserState {

  static final int WINDOW_OFFSET = ReservedSymbols.ESCAPE_SYMBOL.length();

  int linePos = 0;
  int pos = 0;
  int lineNum = 0;
  int eltIndex = 0;
  int bracketCount = 0;


  final int len;
  final String id;
  final CharSequence sequence;

  StringBuilder buffer = new StringBuilder();

  @ToString.Exclude
  Node justPushedNode = null;
  @ToString.Exclude
  boolean justEndedAttributes = false;

  @ToString.Exclude
  private final Deque<Set<Attribute<?>>>
                            attributeStack = new ArrayDeque<>();
  @ToString.Exclude
  private final Deque<LinkedList<Node>>
                            subExprStack   = new ArrayDeque<>();

  public ParserState(CharSequence sequence, String sequenceId) {
    this.sequence = sequence;
    this.len = sequence.length();
    this.id = Objects.requireNonNullElse(sequenceId, "");
  }

  public int inc(int num) {
    if (this.pos + num >= len) {
      return Integer.MIN_VALUE;
    }
    this.linePos += num;
    this.pos += num;
    return num;
  }

  public char charAt(int pos) {
    return this.sequence.charAt(pos);
  }

  public char charAt() {
    return this.sequence.charAt(pos);
  }

  public void appendAtPos() {
    buffer.append(sequence.charAt(pos));
  }

  public boolean atLastPos() {
    return this.pos >= this.len - 1;
  }

  public void pushAttributes(Set<Attribute<?>> attributes) {
    this.attributeStack.push(attributes);
  }

  public Set<Attribute<?>> popAttributes() {
    return this.attributeStack.pollFirst();
  }

  public Set<Attribute<?>> peekAttributes() {
    return this.attributeStack.peekFirst();
  }

  public boolean emptyExpression() {
    return this.currentSubExpression().isEmpty();
  }

  public void startSubExpression() {
    LinkedList<Node> expression = new LinkedList<>();
    this.subExprStack.push(expression);
  }

  public void endSubExpression() {
    this.subExprStack.pollFirst();
  }

  public LinkedList<Node> currentSubExpression() {
    return this.subExprStack.peekFirst();
  }

  public boolean emptyExpressions() {
    return this.subExprStack.isEmpty();
  }

  public void dec(int num) {
    if (this.pos - num < 0) {
      return;
    }
    this.linePos -= num;
    this.pos -= num;
  }

  public void nl() {
    this.lineNum++;
    this.linePos = 0;
  }
}
