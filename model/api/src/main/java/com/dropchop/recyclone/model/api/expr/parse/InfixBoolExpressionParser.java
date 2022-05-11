package com.dropchop.recyclone.model.api.expr.parse;

import com.dropchop.recyclone.model.api.expr.bool.And;
import com.dropchop.recyclone.model.api.expr.bool.Not;
import com.dropchop.recyclone.model.api.expr.bool.Or;
import com.dropchop.recyclone.model.api.expr.bool.Xor;
import com.dropchop.recyclone.model.api.expr.*;
import com.dropchop.recyclone.model.api.expr.relational.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 16. 12. 21.
 */
@Slf4j
public class InfixBoolExpressionParser extends InfixExpressionParser {

  private static final List<Class<?>> OPERATOR_PRECEDENCE = List.of(
    RelationalOperator.class,
    UnaryLeafOperator.class,
    BinaryLeafOperator.class,
    UnaryOperator.class,
    BinaryOperator.class
  );

  private static final Map<String, Operator> SUPPORTED_OPERATORS;

  static {
    Map<String, Operator> map = new LinkedHashMap<>();
    map.put(ReservedSymbols.Bool.INDEPENDENT_WORD, new Near(ReservedSymbols.Bool.INDEPENDENT_WORD));
    map.put(ReservedSymbols.Bool.NEAR_WORD, new Near(ReservedSymbols.Bool.NEAR_WORD));
    map.put(ReservedSymbols.Bool.AND_WORD, new And(ReservedSymbols.Bool.AND_WORD));
    map.put(ReservedSymbols.Bool.XOR_WORD, new Xor(ReservedSymbols.Bool.XOR_WORD));
    map.put(ReservedSymbols.Bool.NOT_WORD, new Not(ReservedSymbols.Bool.NOT_WORD));

    map.put(ReservedSymbols.Bool.OR_WORD, new Or(ReservedSymbols.Bool.OR_WORD));
    map.put(ReservedSymbols.Bool.XOR_SYMBOL, new Xor(ReservedSymbols.Bool.XOR_SYMBOL));
    map.put(ReservedSymbols.Bool.OR_SYMBOL, new Or(ReservedSymbols.Bool.OR_SYMBOL));
    map.put(ReservedSymbols.Bool.AND_SYMBOL, new And(ReservedSymbols.Bool.AND_SYMBOL));
    map.put(ReservedSymbols.Bool.NEAR_SYMBOL, new Near(ReservedSymbols.Bool.NEAR_SYMBOL));
    map.put(ReservedSymbols.Bool.NOT_SYMBOL, new Not(ReservedSymbols.Bool.NOT_SYMBOL));
    map.put(ReservedSymbols.Bool.INDEPENDENT_SYMBOL, new Not(ReservedSymbols.Bool.INDEPENDENT_SYMBOL));

    map.put(ReservedSymbols.Relational.LTE_SYMBOL, new Lte(ReservedSymbols.Relational.LTE_SYMBOL));
    map.put(ReservedSymbols.Relational.GTE_SYMBOL, new Gte(ReservedSymbols.Relational.GTE_SYMBOL));

    map.put(ReservedSymbols.Relational.EQ_SYMBOL, new Eq(ReservedSymbols.Relational.EQ_SYMBOL));
    map.put(ReservedSymbols.Relational.EQ_SYMBOL_2, new Eq(ReservedSymbols.Relational.EQ_SYMBOL_2));
    map.put(ReservedSymbols.Relational.LT_SYMBOL, new Lt(ReservedSymbols.Relational.LT_SYMBOL));
    map.put(ReservedSymbols.Relational.GT_SYMBOL, new Gt(ReservedSymbols.Relational.GT_SYMBOL));

    SUPPORTED_OPERATORS = Collections.unmodifiableMap(map);
  }


  @Override
  protected Map<String, Operator> getSupportedOperators() {
    return SUPPORTED_OPERATORS;
  }

  @Override
  protected List<Class<?>> getOperatorPrecedence() {
    return OPERATOR_PRECEDENCE;
  }
}
