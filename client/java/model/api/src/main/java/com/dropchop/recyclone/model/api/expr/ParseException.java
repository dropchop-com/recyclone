package com.dropchop.recyclone.model.api.expr;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 26. 11. 21.
 */
public class ParseException extends Exception {
  private final ParserError parserError;

  public ParseException(ParserError parserError) {
    this.parserError = parserError;
  }

  public ParseException(String message, ParserError parserError) {
    super(message);
    this.parserError = parserError;
  }

  public ParseException(String message, Throwable cause, ParserError parserError) {
    super(message, cause);
    this.parserError = parserError;
  }

  public ParseException(Throwable cause, ParserError parserError) {
    super(cause);
    this.parserError = parserError;
  }

  public ParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ParserError parserError) {
    super(message, cause, enableSuppression, writableStackTrace);
    this.parserError = parserError;
  }

  public ParserError.Code getErrorCode() {
    return parserError != null ? parserError.getCode() : null;
  }

  @Override
  public String getMessage() {
    String msg = super.getMessage();
    if (parserError != null) {
      return "Error [" + parserError.getCode().name() + "] at column [" + parserError.getColumnNum()
        + "] line [" + parserError.getLineNum() + "] for expression [" + parserError.getExpression() + "]"
        + (msg == null ? "!" : ": " + msg);
    }
    return msg;
  }
}
