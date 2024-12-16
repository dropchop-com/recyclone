package com.dropchop.recyclone.base.api.model.expr.parse;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 9. 12. 21.
 */
public class DefaultBoolExpressionParser extends InfixBoolExpressionParser {

  public static final String DEFAULT_NAME_OPERAND_VALUE = "text";

  public DefaultBoolExpressionParser(String defaultNameOperandValue) {
    this.setCompactSingleTokenPhrase(true);
    this.setDefaultNameOperandValue(defaultNameOperandValue);
  }

  public DefaultBoolExpressionParser() {
    this(DEFAULT_NAME_OPERAND_VALUE);
  }
}
