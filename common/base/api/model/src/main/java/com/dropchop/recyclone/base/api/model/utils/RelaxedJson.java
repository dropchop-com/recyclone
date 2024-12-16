package com.dropchop.recyclone.base.api.model.utils;

import java.math.BigDecimal;
import com.dropchop.recyclone.base.api.model.expr.ParseException;
import com.dropchop.recyclone.base.api.model.expr.ParserError;
import com.dropchop.recyclone.base.api.model.expr.Position;
import com.dropchop.recyclone.base.api.model.expr.ReservedSymbols;
import com.dropchop.recyclone.base.api.model.expr.parse.ParserState;

import java.text.StringCharacterIterator;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static com.dropchop.recyclone.base.api.model.expr.parse.ParserHelper.makeError;

/**
 * Simple "relaxed" JSON-like string parser.
 * It supports missing quotes on names and parses ISO 8601 strings to ZonedDateTime.
 * Rewritten <a href="https://github.com/jtchen/jsonsingle">jsonsingle</a> to support JSON relaxation and ZonedDateTime,
 * but more importantly to support pluggable object structure instantiation by @see Listener.
 * Relaxation is:
 * - no need for name / value quoting,
 * {color: #FDFDFD} == {'color': '#FDFDFD'}
 * - dangling value w/o name is interpreted as __untitled property.
 * {color: #FDFDFD, 12.3} == {'color': '#FDFDFD', '__untitled': 12.3}
 * - dangling value that can't be parsed as decimal, boolean or date is interpreted as and empty object.
 * {'test': 'some string', dangling} == {'test': 'some string', 'dangling': {}}
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27.7.2022.
 */
public abstract class RelaxedJson {

  /**
   * Listener that is invoked on parsing events to provide runtime representation structure.
   *
   * @param <C>
   * @param <L>
   */
  public interface Listener<C, L> {
    C onContainer();
    void onProperty(C container, String name, Object value);
    L onList();
    void onItem(L list, Object item);
  }

  public static class DefaultListener implements Listener<Map<String, Object>, List<Object>> {
    @Override
    public Map<String, Object> onContainer() {
      return new HashMap<>();
    }

    @Override
    public void onProperty(Map<String, Object> container, String name, Object value) {
      container.put(name, value);
    }

    @Override
    public List<Object> onList() {
      return new ArrayList<>();
    }

    @Override
    public void onItem(List<Object> list, Object item) {
      list.add(item);
    }
  }

  private static Object parseValue(String s) {
    switch (s) {
      case "null" -> {
        return null;
      }
      case "true" -> {
        return Boolean.TRUE;
      }
      case "false" -> {
        return Boolean.FALSE;
      }
    }
    if (Strings.isNumber(s)) {
      return new BigDecimal(s);
    }
    Matcher m = Iso8601.matchIso(s);
    if (m.matches()) {
      return Iso8601.fromMatchedIso(m);
    }
    return s;
  }

  private static boolean validNameStringValueSymbol(char c) {
    return c == '+' || c == '-' || c == '#' || c == '_';
  }

  public static <C, L> Object next(ParserState state, Listener<C, L> listener,
                                   StringCharacterIterator it, boolean valueExpected, int depth)
    throws ParseException {
    char c = it.current();
    while (Character.isWhitespace(c)) {
      c = it.next();
    }

    if ((c == '{') && (depth <= 255)) {
      it.next();
      C container = listener.onContainer();
      int size = 0;
      boolean hasValue = false;
      depth += 1;
      while (true) {
        Object k = next(state, listener, it, false, depth);
        if ((k != null) && k.equals('}') && ((size == 0) || hasValue)) {
          return container;
        }
        if ((k != null) && k.equals(',') && hasValue) {
          hasValue = false;
        } else if (!hasValue) {
          Object next = next(state, listener, it, false, depth);
          if (!next.equals(':')) {// check possible unnamed values
            if (next.equals(',') && k instanceof String) {
              k = parseValue((String) k);
            }
            boolean added = false;
            if (k instanceof BigDecimal) { // add unnamed decimal
              listener.onProperty(container, ReservedSymbols.DEFAULT_ATTR_NAME, k);
              added = true;
            } else if (k instanceof ZonedDateTime) { // add unnamed date
              listener.onProperty(container, ReservedSymbols.DEFAULT_ATTR_NAME, k);
              added = true;
            } else if (k instanceof Boolean) { // add unnamed bool
              listener.onProperty(container, ReservedSymbols.DEFAULT_ATTR_NAME, k);
              added = true;
            } else if (k instanceof String && Strings.isAlphaNumeric((String) k)) {
              // add empty container if string is missing value
              C tmp = listener.onContainer();
              listener.onProperty(container, (String) k, tmp);
              added = true;
            }
            if (added) {
              if (next.equals('}')) {
                return container;
              }
              size++;
              continue;
            }
            // by default, we break which triggers error
            break;
          }
          Object v = next(state, listener, it, true, depth);
          listener.onProperty(container, (String) k, v);
          size++;
          hasValue = true;
        } else {
          break;
        }
      }
    } else if ((c == '[') && (depth <= 255)) {
      it.next();
      L list = listener.onList();
      int size = 0;
      boolean hasValue = false;
      depth += 1;
      while (true) {
        Object e = next(state, listener, it, true, depth);
        if ((e != null) && e.equals(']') && ((size == 0) || hasValue)) {
          return list;
        }
        if ((e != null) && e.equals(',') && hasValue) {
          hasValue = false;
        } else if (!hasValue) {
          listener.onItem(list, e);
          //list.add(e);
          size++;
          hasValue = true;
        } else {
          break;
        }
      }
    } else if (c == '"' || c == '\'' ||
      ((Character.isAlphabetic(c) || validNameStringValueSymbol(c)) && !valueExpected)) { // relaxed name / value
      char enclosed = 0;
      if (c == '"' || c == '\'') {
        enclosed = c;
        it.next();
      }
      StringBuilder sb = new StringBuilder();
      for (c = it.current(); it.getIndex() < it.getEndIndex(); c = it.next()) {
        if (c == enclosed) {
          it.next(); // skips the ending enclosed
          String result = sb.toString();
          Matcher m = Iso8601.matchIso(result); // try and parse date
          if (m.matches()) {
            return Iso8601.fromMatchedIso(m);
          }
          return result;
        }
        if (Character.isWhitespace(c) && enclosed == 0) {
          continue;
        }
        if (((c == ':') || (c == ',') || (c == '}') || (c == ']')) && enclosed == 0) {
          return sb.toString();
        }
        if (c == '\\') {
          c = it.next();
          if (c == 'b') {
            c = '\b';
          } else if (c == 'f') {
            c = '\f';
          } else if (c == 'n') {
            c = '\n';
          } else if (c == 'r') {
            c = '\r';
          } else if (c == 't') {
            c = '\t';
          } else if (c == 'u') {
            int u = 0;
            for (int shift = 12; shift >= 0; shift -= 4) {
              int i = Character.digit(it.next(), 16);
              if ((i < 0) || (i >= 16)) {
                throw new IllegalArgumentException();
              }
              u += (i << shift);
            }
            c = (char) u;
          } else if (!((c == '"') || (c == '\'') || (c == '\\'))) {
            break;
          }
        } else if (c < ' ') {
          break;
        }
        sb.append(c);
      }
    } else if ((c == '}') || (c == ']') || (c == ',') || (c == ':')) {
      it.next();
      return c; // uses autoboxing
    } else {
      StringBuilder sb = new StringBuilder();
      for (c = it.current(); it.getIndex() < it.getEndIndex(); c = it.next()) {
        if (Character.isWhitespace(c) || (c == ',') || (c == '}') || (c == ']')) {
          break;
        }
        sb.append(c);
      }
      String s = sb.toString();
      return parseValue(s);
      /*Object tmp = parseValue(s);
      if (!s.equals(tmp)) {
        return tmp;
      }*/
    }
    throw new ParseException("Unexpected symbol!",
      makeError(new Position(it.current(), 0, 0), state, ParserError.Code.PARSE_ATTRIBUTES_DATA_ERROR));
  }

  public static <C, L> C parse(ParserState state, Listener<C, L> listener, String s) throws ParseException {
    StringCharacterIterator it = new StringCharacterIterator(s);
    Object o = next(state, listener, it, false, 0);
    for (char c = it.current(); it.getIndex() < it.getEndIndex(); c = it.next()) {
      if (!Character.isWhitespace(c)) {
        throw new ParseException("Unexpected character!",
          makeError(new Position(it.current(), 0, 0), state, ParserError.Code.PARSE_ATTRIBUTES_DATA_ERROR));
      }
    }
    //noinspection unchecked
    return (C)o;
  }

  /**
   * Parses the JSON text and constructs a structure represented by the
   * JSON text. This method always creates {@code BigDecimal} objects
   * because large numbers may exceed the limits of Java primitive types. An
   * {@code IllegalArgumentException} is thrown if the JSON text is
   * malformed.
   *
   * @param listener object construction parser listener
   * @param s the string containing the JSON text
   * @return a JSON value represented by the JSON text
   * @throws ParseException if the JSON text is malformed
   */
  public static <C, L> C parse(Listener<C, L> listener, String s) throws ParseException {
    return parse(null, listener, s);
  }

  /**
   * Parses the JSON text and constructs a default Map / List structure represented by the
   * JSON text. This method always creates {@code BigDecimal} objects
   * because large numbers may exceed the limits of Java primitive types. An
   * {@code IllegalArgumentException} is thrown if the JSON text is
   * malformed.
   *
   * @param s the string containing the JSON text
   * @return a JSON value represented by the JSON text
   * @throws ParseException if the JSON text is malformed
   */
  public static Map<String, Object> parse(String s) throws ParseException {
    return parse(new DefaultListener(), s);
  }
}
