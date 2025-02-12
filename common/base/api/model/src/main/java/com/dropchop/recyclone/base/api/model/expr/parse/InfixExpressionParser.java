package com.dropchop.recyclone.base.api.model.expr.parse;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.attr.AttributeMarshaller;
import com.dropchop.recyclone.base.api.model.attr.AttributeSet;
import com.dropchop.recyclone.base.api.model.expr.*;
import com.dropchop.recyclone.base.api.model.expr.operand.Name;
import com.dropchop.recyclone.base.api.model.expr.operand.Phrase;
import com.dropchop.recyclone.base.api.model.expr.operand.Token;
import com.dropchop.recyclone.base.api.model.expr.relational.Eq;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.dropchop.recyclone.base.api.model.expr.ParserError.Code;
import static com.dropchop.recyclone.base.api.model.expr.parse.ParserHelper.makeError;
import static com.dropchop.recyclone.base.api.model.expr.parse.ParserState.WINDOW_OFFSET;

/**
 * This implementation is modeled after Shunting-yard algorithm.
 * <p>
 * The difference is that we use stack of lists, where each list represents single sub-expression and
 * contains unbind nodes. When we detect sub-expression termination, we pop the list from stack, validate correct
 * placement of operators and operands, bind nodes to a tree, push resulting sub-expression root at remaining stack's
 * list.
 * </p>
 * <p>
 * We could have used direct Shunting-yard implementation, but the validation and operator precedence makes the code
 * un-manageable.
 * </p>
 * <p>
 * At the end we are left with a single stack element containing single list element that is a root of a tree.
 * Additionally, we support not only simple expression (k1 AND k2 OR (k3 AND k4)) but also
 * relational operators (=, !=, <, ...), so you can form an expression n1 = k1 AND n1 = k2 OR n2 <= k4
 * where n1 ... nm are 'field names' and k1 ... kn are field values.
 * </p>
 * <p>
 * We also support custom attributes for tree nodes:
 * (n1 = k1 AND [n1 = k2 OR n2 <= k4]{custom: {color: red}})
 * Here, the nodes n1, k2, n2, k4 are given attributes that contain a field named "custom",
 * which is a sub-map with attribute field "color" having value "red".
 * </p>
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 11. 21.
 */
@Slf4j
public abstract class InfixExpressionParser implements ExpressionParser {

  protected final Map<String, Operator> supportedOperators;
  protected final List<Class<?>> operatorPrecedence;

  @Setter
  @Getter
  private String defaultNameOperandValue = null;

  @Setter
  private boolean compactSingleTokenPhrase = false;

  public InfixExpressionParser() {
    supportedOperators = Collections.unmodifiableMap(this.getSupportedOperators());
    operatorPrecedence = Collections.unmodifiableList(this.getOperatorPrecedence());
  }

  protected abstract Map<String, Operator> getSupportedOperators();
  protected abstract List<Class<?>> getOperatorPrecedence();

  private Operator tryOperator(String opName) {
    if (opName == null) {
      return null;
    }
    int attIdx = opName.indexOf(ReservedSymbols.ATTRIBUTE_DATA_SYMBOL_START);
    if (attIdx > -1) {
      opName = opName.substring(0, attIdx);
    }
    Operator node = supportedOperators.get(opName);
    if (node == null) {
      return null;
    }
    try {
      //clone
      Operator tmp = node.getClass().getDeclaredConstructor().newInstance();
      tmp.setValue(node.getValue());
      return tmp;
    } catch (Exception e) {
      throw new RuntimeException("Unable to construct operator [" + opName + "]!", e);
    }
  }

  private Operator initPushOperator(Expression expression, ParserState state, String operator) throws ParseException {
    log.trace("Operator: [{}]", operator);
    Operator node = tryOperator(operator);
    if (operator == null) {
      throw new ParseException(makeError(state, Code.UNKNOWW_OPERATOR));
    }
    Deque<Node> subExpression = state.currentSubExpression();

    if (node instanceof BinaryOperator) {
      if (subExpression == null || subExpression.isEmpty()) {
        throw new ParseException(makeError(state, Code.MISSING_LEFT_OPERAND));
      }
    }

    int opLen = operator.length();

    node.setId(state.id + "#" + state.eltIndex);
    node.setValue(operator);
    node.setTree(expression);
    node.setPosition(new Position(state.linePos - opLen, state.pos - opLen, state.lineNum));
    state.eltIndex++;

    Node left = subExpression.peekFirst();
    if (node instanceof BinaryLeafOperator) { // ... NOT text = DirtyHarry
      if (left instanceof BinaryOperator) { // AND and AND is illegal
        throw new ParseException(makeError(node.getPosition(), state, Code.MISSING_LEFT_OPERAND));
      }
      // we support unary operator in front of binary leaf operator: NOT k1 NEAR k3 is equivalent to k1 NOT NEAR k3
      // we just swap it
      if (left instanceof UnaryOperator && !(node instanceof RelationalOperator)) {
        subExpression.pollFirst(); // pop left operator
        Node operand = subExpression.pollFirst();
        if (!(operand instanceof Operand)) {
          throw new ParseException(makeError(node.getPosition(), state, Code.ILLEGAL_LEAF_OPERATOR_USE));
        }
        Node prev = subExpression.peekFirst();
        if (prev != null && !(prev instanceof BinaryOperator)) { // but we don't support expr "NOT k1 NOT NEAR k3"
          throw new ParseException(makeError(node.getPosition(), state, Code.ILLEGAL_LEAF_OPERATOR_USE));
        }
        subExpression.push(left); // unary operator
        subExpression.push(operand); // operand
      } else if (!(left instanceof Operand)) {
        throw new ParseException(makeError(node.getPosition(), state, Code.ILLEGAL_LEAF_OPERATOR_USE));
      }
    } else if (!(node instanceof UnaryOperator) && subExpression.isEmpty()) {
      throw new ParseException(makeError(node.getPosition(), state, Code.MISSING_LEFT_OPERAND));
    } else if (node instanceof BinaryOperator && left instanceof BinaryOperator binOp) {
      if (binOp.getLeft() == null && binOp.getRight() == null) { // unprocessed binary operator
        throw new ParseException(makeError(node.getPosition(), state, Code.MISSING_LEFT_OPERAND));
      }
    }

    state.currentSubExpression().push(node);
    return node;
  }


  private Operand<?> initPushOperand(Expression expression, ParserState state, String operand) throws ParseException {
    log.trace("Operand: [{}]", operand);
    Operand<?> node = ParserHelper.parseOperand(state, operand);
    if (compactSingleTokenPhrase && node instanceof Phrase) {
      List<Token> tokenList = ((Phrase)node).getParsedValue();
      if (tokenList != null && tokenList.size() == 1) {
        node = tokenList.getFirst();
      }
    }

    int opLen = operand.length();

    node.setId(state.id + "#" + state.eltIndex);
    node.setValue(operand);
    node.setTree(expression);
    node.setPosition(new Position(state.linePos - opLen, state.pos - opLen, state.lineNum));
    state.eltIndex++;

    Set<Attribute<?>> attributes = state.peekAttributes();
    if (attributes != null) {
      node.setAttributes(attributes);
    }

    Deque<Node> subExpression = state.currentSubExpression();
    if (!subExpression.isEmpty()) {
      Node operator = subExpression.peekFirst();
      if (!(operator instanceof Operator)) {
        throw new ParseException(makeError(node.getPosition(), state, Code.MISSING_OPERATOR));
      }
    }
    state.currentSubExpression().push(node);
    return node;
  }


  private String operatorLookAhead(ParserState state, char[] window) {
    for (Map.Entry<String, Operator> operatorEntry : supportedOperators.entrySet()) {
      String opStr = operatorEntry.getKey();
      String candidate1 = new String(window, WINDOW_OFFSET, opStr.length());
      if (opStr.equals(candidate1)) {
        if (ParserHelper.endsWith(state.buffer, ReservedSymbols.ESCAPE_SYMBOL)) {
          return null;
        }
        return opStr;
      }
    }
    return null;
  }

  private Node makeMissingNameOperand(Expression searchExpr, ParserState state, Node currParent, Node operand) {
    Position position = currParent.getPosition();
    Eq newParent = new Eq();
    newParent.setId(state.id + "#" + state.eltIndex);
    newParent.setValue(ReservedSymbols.Relational.EQ_SYMBOL);
    newParent.setTree(searchExpr);
    newParent.setPosition(new Position(position.getLinePos(), position.getAbsPos(), position.getLineNum()));
    state.eltIndex++;
    Name defaultName = new Name();
    defaultName.setId(state.id + "#" + state.eltIndex);
    defaultName.setValue(this.getDefaultNameOperandValue());
    defaultName.setParsedValue(this.getDefaultNameOperandValue());
    defaultName.setTree(searchExpr);
    defaultName.setPosition(new Position(position.getLinePos(), position.getAbsPos(), position.getLineNum()));
    state.eltIndex++;

    newParent.setParent(currParent);
    newParent.setLeft(defaultName);
    newParent.setRight(operand);
    operand.setParent(newParent);
    return newParent;
  }

  private void forceMissingNameOperand(Expression searchExpr, ParserState state, UnaryNode currParent, Node operand) {
    if (currParent instanceof RelationalOperator) {
      return;
    }
    Node right = currParent.getRight();
    if (right == operand && operand instanceof Operand) {
      Node newParent = makeMissingNameOperand(searchExpr, state, currParent, operand);
      currParent.setRight(newParent);
    }
  }

  private void forceMissingNameOperand(Expression searchExpr, ParserState state, BinaryNode currParent, Node operand) {
    if (currParent instanceof RelationalOperator) {
      return;
    }
    Node left = currParent.getLeft();
    if (left == operand && operand instanceof Operand) {
      Node newParent = makeMissingNameOperand(searchExpr, state, currParent, operand);
      currParent.setLeft(newParent);
    }
  }

  private void validateRelationalOperator(ParserState state, RelationalOperator currParent) throws ParseException {
    Node right = currParent.getRight();
    Node left = currParent.getLeft();

    if (!(left instanceof Name) || !(right instanceof Operand)) {
      throw new ParseException(makeError(currParent.getPosition(), state, Code.ILLEGAL_RELATIONAL_OPERATOR_USE));
    }
    if (right instanceof TextOperand && !(currParent instanceof Eq)) {
      throw new ParseException(makeError(currParent.getPosition(), state, Code.ILLEGAL_RELATIONAL_OPERATOR_TEXT_OPERAND_USE));
    }
  }

  private void validateUnaryLeafOperator(ParserState state, UnaryLeafOperator currParent) throws ParseException {
    Node right = currParent.getRight();

    if (!(right instanceof RelationalOperator) || !(right instanceof Operand)) {
      throw new ParseException(makeError(currParent.getPosition(), state, Code.ILLEGAL_LEAF_OPERATOR_USE));
    }
  }

  private void drainStack(Expression searchExpr, ParserState state) throws ParseException {
    Node root = null;
    if (state.emptyExpressions()) {
      throw new ParseException(makeError(state, Code.MISSING_OPEN_BRACKET));
    }

    String defaultNameOperandValue = getDefaultNameOperandValue();

    LinkedList<Node> subExpr = state.currentSubExpression();
    for (Class<?> opMarkClass : operatorPrecedence) {
      for (ListIterator<Node> subExprIt = subExpr.listIterator(); subExprIt.hasNext();) {
        Node operator = subExprIt.next();
        root = operator;
        if (!opMarkClass.isAssignableFrom(operator.getClass())) {
          continue;
        }
        if (subExpr.size() == 1) {
          continue;
        }
        if (operator instanceof UnaryOperator || operator instanceof BinaryOperator) {
          UnaryNode unOp = ((UnaryNode) operator);
          if (unOp.getRight() == null) { // we have to set right child
            subExprIt.previous(); // move back to operator
            if (!subExprIt.hasPrevious()) {
              throw new ParseException(makeError(unOp.getPosition(), state, Code.MISSING_RIGHT_OPERAND));
            }
            Node right = subExprIt.previous(); // move to right operand
            subExprIt.remove(); // remove right operand
            subExprIt.next(); // move back to operator
            unOp.setRight(right);
            right.setParent(operator);
            if (defaultNameOperandValue != null) {
              forceMissingNameOperand(searchExpr, state, unOp, right);
            }
          }
          if (operator instanceof UnaryLeafOperator) {
            validateUnaryLeafOperator(state, (UnaryLeafOperator) operator);
          }
        }

        if (operator instanceof BinaryOperator binOp) {
          if (binOp.getLeft() == null) { // we have to set left child
            if (!subExprIt.hasNext()) {
              throw new ParseException(makeError(binOp.getPosition(), state, Code.MISSING_LEFT_OPERAND));
            }
            Node left = subExprIt.next(); // move to left operand
            subExprIt.remove(); // remove left operand
            if (operator instanceof RelationalOperator) {
              left = Name.fromOperand((Operand<?>) left);
            }
            ((BinaryOperator) operator).setLeft(left);
            left.setParent(operator);
            if (defaultNameOperandValue != null) {
              forceMissingNameOperand(searchExpr, state, binOp, left);
            }
          }
        }

        if (operator instanceof RelationalOperator) {
          validateRelationalOperator(state, (RelationalOperator)operator);
        }
      }
    }

    state.endSubExpression();
    if (root != null && !state.emptyExpressions()) {
      state.currentSubExpression().push(root);
    }
    searchExpr.setRoot(root);
  }


  private boolean isOperandTerminated(ParserState state, char[] window) {
    boolean subTreeEnd = ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.SUB_EXPRESSION_SYMBOL_END);
    if (state.atLastPos() && !subTreeEnd) {
      return true;
    }
    if (ParserHelper.isNotEscapedWhitespace(window) && !subTreeEnd) {
      return true;
    }
    if (ParserHelper.isNotEscapedSymbolAt(window, WINDOW_OFFSET + 1, ReservedSymbols.ATTRIBUTE_DATA_SYMBOL_START) && !subTreeEnd) {
      return true;
    }
    if (ParserHelper.isNotEscapedSymbolAt(window, WINDOW_OFFSET + 1, ReservedSymbols.SUB_EXPRESSION_SYMBOL_END) && !subTreeEnd) {
      return true;
    }
    //noinspection RedundantIfStatement
    if (ParserHelper.isNotEscapedSymbolAt(window, WINDOW_OFFSET + 1, ReservedSymbols.ATTRIBUTE_SYMBOL_END) && !subTreeEnd) {
      return true;
    }
    return false;
  }


  private void parseAttributes(ParserState state) throws ParseException {
    int attributeDataCount = 1;
    StringBuilder buffer = new StringBuilder();
    buffer.append("{");
    do {
      char[] window = ParserHelper.getWindow(state, -ReservedSymbols.ESCAPE_SYMBOL.length(), ReservedSymbols.MAX_SYMBOL_LEN);
      if (ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.ATTRIBUTE_DATA_SYMBOL_START)) {
        if (attributeDataCount == 0) {
          throw new ParseException(makeError(state, Code.MISSING_ATTRIBUTES_DATA_END));
        }
        attributeDataCount++;
        continue;
      }
      if (ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.ATTRIBUTE_DATA_SYMBOL_END)) {
        if (attributeDataCount <= 0) {
          throw new ParseException(makeError(state, Code.MISSING_ATTRIBUTES_DATA_START));
        }
        attributeDataCount--;
        if (attributeDataCount == 0) { // terminate
          break;
        }
        continue;
      }
      buffer.append(state.charAt());
    } while (state.inc(1) > Integer.MIN_VALUE);
    buffer.append("}");

    AttributeSet attributeSet = AttributeMarshaller.parse(state, buffer.toString());
    Set<Attribute<?>> attributes = attributeSet.getAttributes();
    if (attributes == null) {
      throw new ParseException(makeError(state, Code.PARSE_ATTRIBUTES_DATA_ERROR));
    }
    if (attributeDataCount > 0) {
      throw new ParseException(makeError(state, Code.MISSING_ATTRIBUTES_DATA_END));
    }

    if (state.justEndedAttributes) { //[term1 OR term2]{attributes}
      Set<Attribute<?>> toFill = state.popAttributes();
      if (toFill != null) {
        toFill.addAll(attributes);
        log.trace("Operand set attr: [{}]", attributes);
      } else {
        throw new ParseException(makeError(state, Code.MISSING_ATTRIBUTES_START));
      }
    } else if (state.justPushedNode != null) { // my_simple_term{attributes}
      state.justPushedNode.setAttributes(attributes);
      log.trace("Node attr: [{}] -> [{}]", state.justPushedNode.getClass().getSimpleName(), attributes);
    } else {
      throw new ParseException(makeError(state, Code.INVALID_ATTRIBUTE_DATA_START));
    }
    state.buffer = new StringBuilder();
  }


  @Override
  public BinaryTree parse(final String id, final CharSequence expressionSeq, Set<Attribute<?>> globalAttributes)
    throws ParseException {
    Expression searchExpr = new Expression();
    searchExpr.setId(id);
    searchExpr.setAttributes(globalAttributes);
    searchExpr.setExpression(expressionSeq.toString());
    if (expressionSeq.isEmpty()) {
      return searchExpr;
    }

    ParserState state = new ParserState(expressionSeq, id);
    log.trace("Expression start.");
    state.startSubExpression();
    do {
      char[] window = ParserHelper.getWindow(state, -ReservedSymbols.ESCAPE_SYMBOL.length(), ReservedSymbols.MAX_SYMBOL_LEN);

      // read until attribute data ends
      if (ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.ATTRIBUTE_DATA_SYMBOL_START)) {
        state.inc(ReservedSymbols.ATTRIBUTE_DATA_SYMBOL_START.length());
        parseAttributes(state);
        continue;
      }

      if (ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.ATTRIBUTE_DATA_SYMBOL_END)) {
        throw new ParseException(makeError(state, Code.MISSING_ATTRIBUTES_START));
      }

      // mark nodes with attributes
      if (ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.ATTRIBUTE_SYMBOL_END)) {
        state.justEndedAttributes = true;
        continue;
      }
      state.justEndedAttributes = false;

      if (ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.ATTRIBUTE_SYMBOL_START)) {
        state.pushAttributes(new LinkedHashSet<>());
        continue;
      }

      // subtree expression start (SUB_EXPRESSION_SYMBOL_START marks
      // when we should stop popping stacks later when we drain)
      if (ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.SUB_EXPRESSION_SYMBOL_START)) {
        log.trace("Sub-expression start.");
        state.startSubExpression();
        state.bracketCount++;
        continue;
      }

      // sub-expression termination (pop the stacks)
      if (ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.SUB_EXPRESSION_SYMBOL_END)) {
        drainStack(searchExpr, state);
        state.bracketCount--;
        if (state.bracketCount < 0) {
          throw new ParseException(makeError(state, Code.MISSING_OPEN_BRACKET));
        }
        log.trace("Sub-expression end.");
        continue;
      }

      String op; // look ahead if operator starts
      if ((op = operatorLookAhead(state, window)) != null) {
        if (!state.buffer.isEmpty()) {
          initPushOperand(searchExpr, state, state.buffer.toString());
        }
        state.justPushedNode = initPushOperator(searchExpr, state, op);
        state.inc(op.length() - 1);
        state.buffer = new StringBuilder();
        continue;
      }

      // read until phrase ends
      if (ParserHelper.isNotEscapedSymbol(window, ReservedSymbols.TextSearch.PHRASE_SYMBOL)) {
        ParserHelper.readUntil(state, ReservedSymbols.TextSearch.PHRASE_SYMBOL, Code.UNTERMINATED_PHRASE);
        state.justPushedNode = initPushOperand(searchExpr, state, state.buffer.toString());
        state.buffer = new StringBuilder();
        state.dec(1); //backtrack since attributes might start
        continue;
      }

      // is operand terminated
      if (isOperandTerminated(state, window)) {

        if (state.atLastPos()
          || ParserHelper.isNotEscapedSymbolAt(window, WINDOW_OFFSET + 1, ReservedSymbols.ATTRIBUTE_DATA_SYMBOL_START)
          || ParserHelper.isNotEscapedSymbolAt(window, WINDOW_OFFSET + 1, ReservedSymbols.SUB_EXPRESSION_SYMBOL_END)
          || ParserHelper.isNotEscapedSymbolAt(window, WINDOW_OFFSET + 1, ReservedSymbols.ATTRIBUTE_SYMBOL_END)) {
          if (!Character.isWhitespace(state.charAt(state.pos))) {
            state.appendAtPos();
          }
        }
        if (!state.buffer.isEmpty()) {
          state.justPushedNode = initPushOperand(searchExpr, state, state.buffer.toString());
          state.buffer = new StringBuilder();
        }
        if (window[WINDOW_OFFSET] == '\n') {
          state.nl();
        }
        continue;
      }
      state.justPushedNode = null;

      state.appendAtPos();
    } while (state.inc(1) > Integer.MIN_VALUE);

    if (!state.emptyExpression()) {
      drainStack(searchExpr, state);
      if (state.bracketCount < 0) {
        throw new ParseException(makeError(state, Code.MISSING_OPEN_BRACKET));
      }
      if (state.bracketCount > 0) {
        throw new ParseException(makeError(state, Code.MISSING_CLOSE_BRACKET));
      }
      log.trace("Expression end.");
    }

    Set<Attribute<?>> attributes = state.popAttributes();
    if (attributes != null) {
      throw new ParseException(makeError(state, Code.MISSING_ATTRIBUTES_END));
    }

    return searchExpr;
  }

  @Override
  public BinaryTree parse(final String id, final CharSequence expressionSeq) throws ParseException {
    return this.parse(id, expressionSeq, Collections.emptySet());
  }

  @Override
  public BinaryTree parse(final CharSequence expressionSeq) throws ParseException {
    return this.parse("<<default-id>>", expressionSeq, Collections.emptySet());
  }
}
