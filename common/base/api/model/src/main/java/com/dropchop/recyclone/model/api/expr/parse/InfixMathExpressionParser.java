package com.dropchop.recyclone.model.api.expr.parse;

import com.dropchop.recyclone.model.api.expr.BinaryOperator;
import com.dropchop.recyclone.model.api.expr.Operator;
import com.dropchop.recyclone.model.api.expr.UnaryOperator;
import com.dropchop.recyclone.model.api.expr.ReservedSymbols;
import com.dropchop.recyclone.model.api.expr.math.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Written just for the fun of it.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 16. 12. 21.
 */
@Slf4j
public class InfixMathExpressionParser extends InfixExpressionParser {

  private static final List<Class<?>> OPERATOR_PRECEDENCE = List.of(
    UnaryOperator.class,
    BinaryOperator.class
  );

  private static final Map<String, Operator> SUPPORTED_OPERATORS;

  static {
    Map<String, Operator> map = new LinkedHashMap<>();
    map.put(ReservedSymbols.Math.SINH_SYMBOL, new HyperbolicSine(ReservedSymbols.Math.SINH_SYMBOL));
    map.put(ReservedSymbols.Math.SIN_SYMBOL, new Sine(ReservedSymbols.Math.SIN_SYMBOL));
    map.put(ReservedSymbols.Math.POWER_SYMBOL, new Power(ReservedSymbols.Math.POWER_SYMBOL));
    map.put(ReservedSymbols.Math.MULTIPLICATION_SYMBOL, new Multiplication(ReservedSymbols.Math.MULTIPLICATION_SYMBOL));
    map.put(ReservedSymbols.Math.DIVSION_SYMBOL, new Division(ReservedSymbols.Math.DIVSION_SYMBOL));
    map.put(ReservedSymbols.Math.ADDITION_SYMBOL, new Addition(ReservedSymbols.Math.ADDITION_SYMBOL));
    map.put(ReservedSymbols.Math.SUBSTRACTION_SYMBOL, new Substraction(ReservedSymbols.Math.SUBSTRACTION_SYMBOL));

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
