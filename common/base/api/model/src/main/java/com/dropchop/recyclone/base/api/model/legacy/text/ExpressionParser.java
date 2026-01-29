package com.dropchop.recyclone.base.api.model.legacy.text;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 13. 06. 2025
 */
@Slf4j
@SuppressWarnings("unused")
public class ExpressionParser {
  @SuppressWarnings("RegExpRedundantEscape")
  public static String  SPECIAL_CHARS = "\\s\"'’`*\\(\\)\\{\\}…+\\-–_&!?.,;#@\\|:/\\[\\]\\\\";
  public static Pattern PUNCTATION = Pattern.compile("(?U)[" + SPECIAL_CHARS + "]");

  static private void terminateNameValue2(String name, String valueStr, Map<String, Object> metaData) {
    if (!valueStr.isEmpty()) {
      if (valueStr.equalsIgnoreCase("t")
          || valueStr.equalsIgnoreCase("true")
          || valueStr.equalsIgnoreCase("yes")) {
        metaData.put(name, Boolean.TRUE);
      } else if (valueStr.equalsIgnoreCase("f")
          || valueStr.equalsIgnoreCase("false")
          || valueStr.equalsIgnoreCase("no")) {
        metaData.put(name, Boolean.FALSE);
      } else {
        try {
          Double d = Double.parseDouble(valueStr);
          metaData.put(name, d);
        } catch (Exception e) {
          metaData.put(name, valueStr);
        }
      }
    } else { // special case: "name1, name2, name3" missing value is populated with empty hashmap
      metaData.put(name, new HashMap<>());
    }
  }

  static int readInQuotes(ExpressionToken token, StringBuilder buffer, CharSequence expression, int startPos) {
    boolean missingEnd = true;
    int j = startPos + 1;
    for (; j < expression.length(); j++) {
      char currChr = expression.charAt(j);
      char prevChr = j > 0 ? expression.charAt(j - 1) : ' ';
      if (currChr == '\'' && prevChr != '\\') {
        missingEnd = false;
        break;
      }
      buffer.append(currChr);
    }
    if (missingEnd) {
      token.getErrors().add("Invalid name value pair syntax in meta data. Unterminated value.");
      return -1;
    }
    return j;
  }

  static int parseMetaData4(ExpressionToken token, CharSequence expression, int startPos,
                            Map<String, Object> metaData) {
    List<String> errors = token.getErrors();
    if (startPos >= expression.length()) {
      errors.add("Missing matching } character for meta data at end.");
      return startPos;
    }

    int i = startPos;
    StringBuilder nameBuffer = new StringBuilder();
    StringBuilder valueBuffer = new StringBuilder();

    boolean parseName = true;
    boolean terminated = false;
    Map<String, Object> nested = null;

    for (; i < expression.length(); i++) {
      char currChr = expression.charAt(i);
      char prevChr = i > 0 ? expression.charAt(i - 1) : ' ';
      if (currChr == '\'' && prevChr != '\\') {
        int result = readInQuotes(token, parseName ? nameBuffer : valueBuffer, expression, i);
        if (parseName) {
          parseName = false;
        }
        if (result == -1) {
          errors.add("Unterminated metadata value! Missing '.");
          return i;
        }
        i = result;
        continue;
      }

      if (currChr == '{' && prevChr != '\\') { // nest to forward recursion
        if (nameBuffer.length() <= 0) {
          errors.add("Started nested metadata without name!");
          return i;
        }
        nested = new HashMap<>();
        metaData.put(nameBuffer.toString().trim(), nested);
        nameBuffer = new StringBuilder();
        // go into a nested structure
        i = parseMetaData4(token, expression, i + 1, nested);
        if (i >= expression.length() - 1) { // reached the end of expression prematurely
          errors.add("Unterminated metadata! Missing {.");
          return i;
        }
        if (!errors.isEmpty()) {
          return i;
        }
        terminated = true;
        parseName = true;
        continue;
      }

      if (currChr == ':') { // terminate name
        String name = nameBuffer.toString().trim();
        String value = valueBuffer.toString().trim();
        if (!name.isEmpty() && !value.isEmpty()) {
          errors.add("Missing name for : symbol!");
          return i;
        }
        if (!parseName && value.isEmpty() && !terminated) {
          errors.add("Missing value for : symbol!");
          return i;
        }
        terminated = false;
        parseName = false;
        continue;
      }

      if (currChr == ',') { // next metadata element
        String name = nameBuffer.toString().trim();
        String value = valueBuffer.toString().trim();
        nameBuffer = new StringBuilder();
        valueBuffer = new StringBuilder();
        if (name.isEmpty() && nested == null) {
          errors.add("Invalid name value pair syntax in meta data! Missing name.");
          break;
        }
        parseName = true;
        if (!name.isEmpty() && nested == null) {
          terminateNameValue2(name, value, metaData);
        }
        terminated = true;
        nested = null;
        continue;
      }

      if (currChr == '}' && prevChr != '\\') {
        break;
      }

      if (parseName) {
        if (Character.isWhitespace(currChr) && nameBuffer.length() <= 0) {
          continue;
        }
        if (Character.isWhitespace(prevChr) && !Character.isWhitespace(currChr) && !nameBuffer.isEmpty()) {
          errors.add("Invalid name syntax in meta data! Unterminated name.");
          continue;
        }
        nameBuffer.append(currChr);
      } else {
        if (Character.isWhitespace(currChr) && valueBuffer.length() <= 0) {
          continue;
        }
        if (Character.isWhitespace(prevChr) && !Character.isWhitespace(currChr) && !valueBuffer.isEmpty()) {
          errors.add("Invalid value syntax in meta data! Unterminated name.");
          continue;
        }
        valueBuffer.append(currChr);
      }
    }

    String name = nameBuffer.toString().trim();
    String value = valueBuffer.toString().trim();
    if (!name.isEmpty()) {
      terminateNameValue2(name, value, metaData);
    }

    if (metaData.isEmpty()) {
      errors.add("Empty metadata!");
    }

    return i;
  }

  public static List<ExpressionToken> parse(CharSequence expression,
                                            boolean keepPunctation,
                                            boolean collapseOr,
                                            boolean useNames) {
    List<ExpressionToken> tokens = new LinkedList<>();

    NavigableMap<Integer, Map<String, Object>> metaStarts = new TreeMap<>();
    NavigableMap<Integer, Map<String, Object>> metaEnds = new TreeMap<>();
    Deque<Map<String, Object>> metaStack = new ArrayDeque<>();

    String metaDataError = null;

    boolean tokenStart = true;
    boolean upperChar = false;
    int len = expression.length();

    ExpressionToken current = null;
    ExpressionToken previous;
    ExpressionToken prePrevious;

    for (int i = 0; i < len; i++) {
      char currChr = expression.charAt(i);
      char prevChr = i > 0 ? expression.charAt(i - 1) : ' ';
      char nextChr = i < len - 2 ? expression.charAt(i + 1) : ' ';

      // handle parse metadata k1 [ k2 OR [ k3{view} ] ]{edit; color:red}
      if (currChr == '[' && prevChr != '\\' && !(current instanceof Phrase)) {
        Map<String, Object> metaData = new HashMap<>();
        metaStarts.put(i, metaData);
        metaStack.push(metaData);
        continue;
      } else if (currChr == ']' && prevChr != '\\' && !(current instanceof Phrase)) {
        if (!metaStack.isEmpty()) {
          Map<String, Object> metaData = metaStack.pop();
          metaEnds.put(i, metaData);
        } else {
          metaDataError = "Missing matching [ character for meta data.";
          if (!tokens.isEmpty()) {
            tokens.getLast().getErrors().add(metaDataError);
          }
          // if the expression starts with ] it will be ignored and this is fine.
        }
        continue;
      } else if (currChr == '{' && prevChr != '\\') {
        Map<String, Object> parsedMeta = new HashMap<>();
        i = parseMetaData4(tokens.getLast(), expression, i + 1, parsedMeta);
        if (!parsedMeta.isEmpty()) {
          Map.Entry<Integer, Map<String, Object>> metaDataEntry = metaEnds.floorEntry(i);
          if (metaDataEntry != null) {
            Map<String, Object> metaData = metaDataEntry.getValue();
            metaData.putAll(parsedMeta);
          }
        }
        continue;
      } else if (currChr == '}' && prevChr != '\\') {
        metaDataError = "Missing matching { character for meta data details.";
        if (!tokens.isEmpty()) {
          tokens.getLast().getErrors().add(metaDataError);
        }
        continue;
      }

      if (Character.isWhitespace(currChr) && !(current instanceof Phrase)) {
        tokenStart = true;
        current = null;
        upperChar = false;
        continue;
      }

      if (current instanceof Phrase) {
        boolean inMetaDataInfo = false;
        while (i < len) {
          currChr = expression.charAt(i);
          prevChr = i > 0 ? expression.charAt(i - 1) : ' ';
          current.setEnd(i);
          List<String> errors = current.getErrors();
          if (currChr == '"' || currChr == '”' || currChr == '“') {
            current.setEnd(i + 1);
            break;
          }
          i++;
          if (currChr == '[' && prevChr != '\\') {
            metaDataError = "Meta data should not start in phrase.";
            errors.add(metaDataError);
            continue;
          }
          if (currChr == ']' && prevChr != '\\') {
            metaDataError = "Meta data should not end in phrase.";
            errors.add(metaDataError);
            continue;
          }
          if (inMetaDataInfo || currChr == '{' && prevChr != '\\') {
            if (currChr == '{' && prevChr != '\\') {
              metaDataError = "Meta data info should not start in phrase.";
              errors.add(metaDataError);
            }
            inMetaDataInfo = true;
            continue;
          }
          if (currChr == '}' && prevChr != '\\') {
            metaDataError = "Meta data info should not end in phrase.";
            errors.add(metaDataError);
            //noinspection DataFlowIssue
            inMetaDataInfo = false;
            continue;
          }
          StringBuilder currExpression = current.getExpression();
          if (Character.isLetterOrDigit(currChr)
              || (keepPunctation && (PUNCTATION.matcher(Character.toString(currChr)).find()))
              || (Character.isWhitespace(currChr)
              && !currExpression.isEmpty()
              && !Character.isWhitespace(currExpression.charAt(currExpression.length() - 1)))
              || (currChr == '*' && !Character.isWhitespace(prevChr))) {
            current.append(currChr);
          }
        }
        tokenStart = true;
        current = null;
        continue;
      }

      if (current instanceof Filter) {
        current.appendAndIncEnd(currChr);
        continue;
      }

      if (tokenStart) { // start of word
        previous = tokens.isEmpty() ? null : tokens.getLast();
        if (previous != null && previous.getExpression().toString().equals("OR")) {
          tokens.set(tokens.size() - 1, new Or());
        }
        if (currChr == '-' && !Character.isWhitespace(nextChr)) {
          current = new Term();
          current.setMustNot(true);
          current.setStartEnd(i + 1, i + 1);
        } else if (currChr == '+' && !Character.isWhitespace(nextChr)) {
          current = new Term();
          current.setMust(true);
          current.setStartEnd(i + 1, i + 1);
        } else if (currChr == '"' || currChr == '”' || currChr == '“') {
          current = new Phrase();
          tokenStart = false;
          if (prevChr == '-' || prevChr == '+') {
            if (prevChr == '-') {
              current.setMustNot(true);
            }
            if (prevChr == '+') {
              current.setMust(true);
            }
            if (previous != null && previous.getExpression().length() <= 0) {
              tokens.remove(previous);
            }
          }
          current.setStartEnd(i, i);
        } else if (currChr == '|') {
          current = new Or();
        } else if (Character.isLetterOrDigit(currChr) // middle of the word
            || (currChr == '*' && !Character.isWhitespace(prevChr))
            || (keepPunctation && PUNCTATION.matcher(Character.toString(currChr)).find())) {

          if (current == null) {
            current = new Term();
            current.setStartEnd(i, i);
          }

          if (currChr == '\\' && prevChr != '\\') { // skip escape symbol
            current.setStartEnd(i + 1, i + 1);
          } else {
            current.appendAndIncEnd(currChr);
          }
          if (Character.isLetter(currChr) && Character.isUpperCase(currChr)) {
            upperChar = true;
          }
          tokenStart = false;
        }

        if (current != null && current != previous) {
          tokens.add(current);
        }
      } else if (Character.isLetterOrDigit(currChr) // middle of the word
          || (currChr == '*' && !Character.isWhitespace(prevChr))
          || (currChr == ':' && !Character.isWhitespace(prevChr))
          || (keepPunctation && PUNCTATION.matcher(Character.toString(currChr)).find())
          || (currChr == '.' && Character.isDigit(prevChr) && Character.isDigit(nextChr))) {

        if (Character.isLetter(currChr)) {
          if (Character.isUpperCase(currChr)) {
            upperChar = true;
            current.setUpperCase(true);
          } else {
            current.setUpperCase(false);
          }
          if (upperChar && !Character.isUpperCase(currChr)) {
            current.setMixedCase(true);
          }
        }

        if (currChr == ':' && useNames) {
          Filter tmp = new Filter();
          tmp.setStartEnd(current.getStart(), i + 1);
          tmp.setName(current.getExpression().toString());
          tokens.remove(current);
          current = tmp;
          tokens.add(tmp);
        } else {
          if (currChr == '\\') {
            if (nextChr == '*') { // this si reserved symbol for later matching, so escaping must be supported
              current.appendAndIncEnd(currChr);
            } else if (!PUNCTATION.matcher(Character.toString(nextChr)).find()) {
              current.appendAndIncEnd(nextChr);
              i++;
            }
          } else {
            current.appendAndIncEnd(currChr);
          }
        }
        //noinspection DataFlowIssue
        tokenStart = false;
      }
    }

    ExpressionToken token = tokens.isEmpty() ? null : tokens.getLast();
    if (token != null && token.getExpression().toString().equals("OR")) {
      tokens.removeLast();
    }

    if (!metaStack.isEmpty() && !tokens.isEmpty()) {
      metaDataError = "Missing matching ] character for metadata.";
      tokens.getLast().getErrors().add(metaDataError);
    }

    //Normalize fields: detect must phrase and parse phrase tokens.
    List<ExpressionToken> rewritten = new LinkedList<>();
    previous = null;
    for (ExpressionToken token1 : tokens) {
      current = token1;

      //find its metadata
      Collection<Map<String, Object>> head = metaStarts.headMap(current.getStart()).values();
      Collection<Map<String, Object>> tail = metaEnds.tailMap(current.getEnd()).values();
      List<Map<String, Object>> intersect = new ArrayList<>(head);
      intersect.retainAll(tail);
      ListIterator<Map<String, Object>> metaDataIt = intersect.listIterator(intersect.size());
      if (metaDataIt.hasPrevious()) {
        if (metaDataError == null) {
          if (current.getMetaData() == null) {
            current.setMetaData(new HashMap<>());
          }
          current.getMetaData().putAll(metaDataIt.previous());
        } else if (current.getErrors().isEmpty()) {
          current.getErrors().add(metaDataError);
        }
      }

      if (current instanceof Phrase) {
        if (previous != null && previous.isMust() && previous.getExpression().length() <= 0) {
          current.setMust(true);
          rewritten.remove(previous);
        }
        List<ExpressionToken> phraseTokens = parse(
            current.getExpression().toString(), keepPunctation, collapseOr, useNames
        );
        ((Phrase) current).setExpressionTokens(phraseTokens);
        for (ExpressionToken phraseToken : phraseTokens) {
          if (phraseToken.isMixedCase()) {
            current.setMixedCase(true);
            break;
          }
          if (phraseToken.isUpperCase()) {
            current.setUpperCase(true);
            break;
          }
        }
      }
      rewritten.add(current);
      previous = current;
    }

    if (!collapseOr) {
      return rewritten;
    }

    //Normalize fields: collapse OR.
    Or tmpOr = null;
    current = null;
    previous = null;
    List<ExpressionToken> rewritten2 = new LinkedList<>();
    for (int i = 0; i < rewritten.size(); i++) {
      prePrevious = previous;
      previous = i > 0 ? current : null;
      current = rewritten.get(i);

      if (previous instanceof Or && i > 1) {
        if (tmpOr == null) {
          tmpOr = (Or) previous;
        }
        List<ExpressionToken> orTokens = tmpOr.getExpressionTokens();
        if (orTokens.isEmpty()) {//start new OR
          orTokens.add(prePrevious);
          rewritten2.remove(prePrevious);
          rewritten2.add(tmpOr);
        }
        tmpOr.getExpressionTokens().add(current);
        continue;
      } else if (current instanceof Or) {
        continue;
      }

      tmpOr = null;//reset for new OR
      rewritten2.add(current);
    }

    return rewritten2;
  }

  public static List<ExpressionToken> parse(CharSequence expression) {
    return parse(expression, false, true, false);
  }


  public static List<ExpressionToken> parse(CharSequence expression, boolean useNames) {
    return parse(expression, false, true, useNames);
  }


  public static List<String> getAllTokens(String searchString) {
    List<String> words = new LinkedList<>();
    if (searchString == null || searchString.isEmpty()) {
      return words;
    }

    List<ExpressionToken> tokens = parse(searchString);
    for (ExpressionToken token : tokens) {
      if (token instanceof Phrase) {
        StringBuilder phrase = new StringBuilder();
        for (ExpressionToken subToken : ((Phrase) token).getExpressionTokens()) {
          phrase.append(subToken.getExpression().toString());
          phrase.append(" ");
        }
        words.add(phrase.toString());
      } else if (token instanceof Or) {
        for (ExpressionToken subToken : ((Or) token).getExpressionTokens()) {
          List<String> subTokens = getAllTokens(subToken.getExpression().toString());
          words.addAll(subTokens);
        }
      } else {
        String expr = token.getExpression().toString();
        if (!expr.isEmpty()) {
          words.add(expr);
        }
      }
    }

    return words;
  }

  public static String createSearchTextExpression(final List<String> theAllWords,
                                                  final String thePhrase,
                                                  final List<String> theAnyWords,
                                                  final List<String> theNotWords,
                                                  String theSearchString) {
    StringBuilder expression = new StringBuilder();
    if (theSearchString == null || theSearchString.isEmpty()) {
      if ((theAllWords != null) && !theAllWords.isEmpty()) {
        for (String word : theAllWords) {
          expression.append("+");
          expression.append(word);
          expression.append(" ");
        }
      }
      if ((thePhrase != null) && !thePhrase.isEmpty()) {
        expression.append("+\"");
        expression.append(thePhrase);
        expression.append("\"");
        expression.append(" ");
      }
      if ((theAnyWords != null) && !theAnyWords.isEmpty()) {
        boolean wisited = false;
        for (String word : theAnyWords) {
          if (wisited) {
            expression.append(" OR ");
          }
          expression.append(word);
          wisited = true;
        }
      }
      if ((theNotWords != null) && !theNotWords.isEmpty()) {
        for (String word : theNotWords) {
          if (!expression.isEmpty()) {
            expression.append(" ");
          }
          expression.append("-");
          expression.append(word);
          expression.append(" ");
        }
      }
    } else {
      expression.append(theSearchString);
    }
    return expression.toString();
  }

  public static List<String> parseWords(String words) {
    List<String> searchWords = new LinkedList<>();
    if (words == null || words.isEmpty()) {
      return searchWords;
    }
    StringBuilder stringBuilder = new StringBuilder();
    boolean inQuotes = false;
    char c;
    for (int i = 0; i < words.length(); i++) {
      c = words.charAt(i);
      if (Character.isLetterOrDigit(c) || c == '"' || c == '*' || c == '\'' || inQuotes) {
        stringBuilder.append(c);
        if (c == '"') {
          inQuotes = !inQuotes;
        }
      } else //noinspection ConstantValue
        if (!inQuotes && !stringBuilder.isEmpty()) {
        searchWords.add(stringBuilder.toString().trim());
        stringBuilder = new StringBuilder();
      }
    }

    if (!stringBuilder.isEmpty()) {
      if (inQuotes) { // missing end quotes
        stringBuilder.append("\"");
      }
      searchWords.add(stringBuilder.toString().trim());
    }
    return searchWords;
  }
}
