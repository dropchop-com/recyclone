package com.dropchop.recyclone.model.api.expr;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 11. 21.
 */
public class ReservedSymbols {

  public static final String DEFAULT_ATTR_NAME = "__untitled";

  public static final String ESCAPE_SYMBOL = "\\";
  public static final String NAME_DELIM = ":";
  public static final String VALUE_SYMBOL = "'";

  public static final int MAX_SYMBOL_LEN = 4;

  public static final String SUB_EXPRESSION_SYMBOL_START = "(";
  public static final String SUB_EXPRESSION_SYMBOL_END = ")";
  public static final String ATTRIBUTE_SYMBOL_START = "[";
  public static final String ATTRIBUTE_SYMBOL_END = "]";
  public static final String ATTRIBUTE_DATA_SYMBOL_START = "{";
  public static final String ATTRIBUTE_DATA_SYMBOL_END = "}";
  public static final String ATTRIBUTE_DATA_DELIM = ",";

  public static class Bool extends ReservedSymbols {
    public static final String AND_WORD = "AND";
    public static final String AND_SYMBOL = "&&";
    public static final String OR_WORD = "OR";
    public static final String OR_SYMBOL = "||";
    public static final String XOR_WORD = "XOR";
    public static final String XOR_SYMBOL = "++";
    public static final String NOT_WORD = "NOT";
    public static final String NOT_SYMBOL = "!!";
    public static final String NEAR_WORD = "NEAR";
    public static final String NEAR_SYMBOL = "~~";
    public static final String INDEPENDENT_WORD = "IDPT";
    public static final String INDEPENDENT_SYMBOL = "##";
  }

  public static class Math extends ReservedSymbols {
    public static final String SIN_SYMBOL = "sin";
    public static final String SINH_SYMBOL = "sinh";
    public static final String POWER_SYMBOL = "^";
    public static final String MULTIPLICATION_SYMBOL = "*";
    public static final String DIVSION_SYMBOL = "/";
    public static final String ADDITION_SYMBOL = "+";
    public static final String SUBSTRACTION_SYMBOL = "-";
  }

  public static class Relational extends ReservedSymbols {
    //public static final String EQ_WORD = "EQ";
    public static final String EQ_SYMBOL = "=";
    public static final String EQ_SYMBOL_2 = NAME_DELIM;
    //public static final String LT_WORD = "LT";
    public static final String LT_SYMBOL = "<";
    //public static final String GT_WORD = "GT";
    public static final String GT_SYMBOL = ">";
    //public static final String LTE_WORD = "LTE";
    public static final String LTE_SYMBOL = "<=";
    //public static final String GTE_WORD = "GTE";
    public static final String GTE_SYMBOL = ">=";
  }

  public static class TextSearch extends Bool {
    public static final String ANY_CHARS_SYMBOL = "*";
    public static final String ANY_CHAR_SYMBOL = "?";
    public static final String PHRASE_SYMBOL = "\"";
  }
}
