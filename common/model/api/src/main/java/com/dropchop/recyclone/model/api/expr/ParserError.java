package com.dropchop.recyclone.model.api.expr;

import lombok.Data;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
@Data
public class ParserError {
  public enum Code {
    UNKNOWW_OPERATOR,
    MISSING_OPERATOR,
    EMPTY_OPERATOR,
    MISSING_CLOSE_BRACKET,
    MISSING_OPEN_BRACKET,
    UNTERMINATED_PHRASE,
    MISSING_ATTRIBUTES_START,
    ILLEGAL_LEAF_OPERATOR_USE,
    ILLEGAL_RELATIONAL_OPERATOR_USE,
    ILLEGAL_RELATIONAL_OPERATOR_TEXT_OPERAND_USE,
    MISSING_LEFT_OPERAND,
    MISSING_RIGHT_OPERAND,
    MISSING_ATTRIBUTES_END,
    MISSING_ATTRIBUTES_DATA_START,
    MISSING_ATTRIBUTES_DATA_END,
    INVALID_ATTRIBUTE_DATA_START,
    PARSE_ATTRIBUTES_DATA_ERROR,
    PARSE_VALUE_ERROR
  }

  private int lineNum;
  private int columnNum;
  private Code code;
  private String expression;
}
