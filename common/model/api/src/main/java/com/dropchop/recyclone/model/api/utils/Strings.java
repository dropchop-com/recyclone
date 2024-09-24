package com.dropchop.recyclone.model.api.utils;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 27. 07. 22.
 */
@SuppressWarnings("unused")
public interface Strings {

  String DEEP_TREE_MATCH = "**";

  static boolean isAlphaNumeric(String s) {
    return s != null && s.matches("^\\p{L}[\\p{L}\\d]*$");
  }
  static boolean isNumber(String s) {
    char[] chars = s.toCharArray();
    if (chars.length == 0) {
      return false;
    }

    int begin = 0;
    if (chars[0] == '-') {
      begin = 1;
    }
    boolean isValid = false;
    boolean hasLeadingZero = false;
    boolean hasFraction = false;
    boolean hasExponent = false;
    for (int i = begin; i < chars.length; i += 1) {
      char c = chars[i];
      if ((c >= '0') && (c <= '9')) {
        if ((!hasFraction) && (!hasExponent)) {
          if (i == begin) {
            hasLeadingZero = c == '0';
          } else if (hasLeadingZero) {
            return false;
          }
        }
        isValid = true;
      } else if (c == '.') {
        if (hasFraction || hasExponent || (!isValid)) {
          return false;
        }
        hasFraction = true;
        isValid = false;
      } else if ((c == 'e') || (c == 'E')) {
        if (hasExponent || (!isValid) || ((i + 1) == chars.length)) {
          return false;
        }
        c = chars[i + 1];
        if ((c == '+') || (c == '-')) {
          i += 1;
        } else if ((c < '0') || (c > '9')) {
          return false;
        }
        hasExponent = true;
        isValid = false;
      } else {
        return false;
      }
    }
    return isValid;
  }


  static String jsonEscape(String s, String enclose) {
    if (enclose == null) {
      enclose = "";
    }
    StringBuilder sb = new StringBuilder();
    sb.append(enclose);
    for (int i = 0; i < s.length(); i += 1) {
      char c = s.charAt(i);
      if (c == '\b') {
        sb.append("\\b");
      } else if (c == '\f') {
        sb.append("\\f");
      } else if (c == '\n') {
        sb.append("\\n");
      } else if (c == '\r') {
        sb.append("\\r");
      } else if (c == '\t') {
        sb.append("\\t");
      } else if (c == '"') {
        sb.append("\\\"");
      } else if (c == '\'') {
        sb.append("\\'");
      } else if (c == '\\') {
        sb.append("\\\\");
      } else {
        if (c < ' ') {
          String hex = Integer.toHexString(c);
          sb.append("\\u00");
          if (hex.length() == 1) {
            sb.append('0');
          }
          sb.append(hex);
        } else {
          sb.append(c);
        }
      }
    }
    sb.append(enclose);
    return sb.toString();
  }

  static String jsonEscape(String s) {
    return jsonEscape(s, null);
  }

  private static boolean allStars(char[] chars, int start, int end) {
    for (int i = start; i <= end; ++i) {
      if (chars[i] != '*') {
        return false;
      }
    }
    return true;
  }

  private static boolean different(
    boolean caseSensitive, char ch, char other) {
    return caseSensitive
      ? ch != other
      : Character.toUpperCase(ch) != Character.toUpperCase(other);
  }

  /**
   * Tests whether a string matches against a pattern.
   * The pattern may contain two special characters:<br>
   * '*' means zero or more characters<br>
   * '?' means one and only one character
   *
   * @param pattern The pattern to match against.
   *                Must not be <code>null</code>.
   * @param str     The string which must be matched against the pattern.
   *                Must not be <code>null</code>.
   * @param caseSensitive Whether matching should be performed
   *                        case sensitively.
   *
   * @return <code>true</code> if the string matches against the pattern,
   *         or <code>false</code> otherwise.
   */
  static boolean match(String pattern, String str,
                              boolean caseSensitive) {
    char[] patArr = pattern.toCharArray();
    char[] strArr = str.toCharArray();
    int patIdxStart = 0;
    int patIdxEnd = patArr.length - 1;
    int strIdxStart = 0;
    int strIdxEnd = strArr.length - 1;

    boolean containsStar = false;
    for (char ch : patArr) {
      if (ch == '*') {
        containsStar = true;
        break;
      }
    }

    if (!containsStar) {
      // No '*'s, so we make a shortcut
      if (patIdxEnd != strIdxEnd) {
        return false; // Pattern and string do not have the same size
      }
      for (int i = 0; i <= patIdxEnd; i++) {
        char ch = patArr[i];
        if (ch != '?' && different(caseSensitive, ch, strArr[i])) {
          return false; // Character mismatch
        }
      }
      return true; // String matches against pattern
    }

    if (patIdxEnd == 0) {
      return true; // Pattern contains only '*', which matches anything
    }

    // Process characters before first star
    while (true) {
      char ch = patArr[patIdxStart];
      if (ch == '*' || strIdxStart > strIdxEnd) {
        break;
      }
      if (ch != '?'
        && different(caseSensitive, ch, strArr[strIdxStart])) {
        return false; // Character mismatch
      }
      patIdxStart++;
      strIdxStart++;
    }
    if (strIdxStart > strIdxEnd) {
      // All characters in the string are used. Check if only '*'s are
      // left in the pattern. If so, we succeeded. Otherwise, failure.
      return allStars(patArr, patIdxStart, patIdxEnd);
    }

    // Process characters after last star
    while (true) {
      char ch = patArr[patIdxEnd];
      if (ch == '*' || strIdxStart > strIdxEnd) {
        break;
      }
      if (ch != '?' && different(caseSensitive, ch, strArr[strIdxEnd])) {
        return false; // Character mismatch
      }
      patIdxEnd--;
      strIdxEnd--;
    }
    if (strIdxStart > strIdxEnd) {
      // All characters in the string are used. Check if only '*'s are
      // left in the pattern. If so, we succeeded. Otherwise, failure.
      return allStars(patArr, patIdxStart, patIdxEnd);
    }

    // process pattern between stars. padIdxStart and patIdxEnd point
    // always to a '*'.
    while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
      int patIdxTmp = -1;
      for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
        if (patArr[i] == '*') {
          patIdxTmp = i;
          break;
        }
      }
      if (patIdxTmp == patIdxStart + 1) {
        // Two stars next to each other, skip the first one.
        patIdxStart++;
        continue;
      }
      // Find the pattern between padIdxStart & padIdxTmp in str between
      // strIdxStart & strIdxEnd
      int patLength = (patIdxTmp - patIdxStart - 1);
      int strLength = (strIdxEnd - strIdxStart + 1);
      int foundIdx = -1;
      strLoop:
      for (int i = 0; i <= strLength - patLength; i++) {
        for (int j = 0; j < patLength; j++) {
          char ch = patArr[patIdxStart + j + 1];
          if (ch != '?' && different(caseSensitive, ch,
            strArr[strIdxStart + i + j])) {
            continue strLoop;
          }
        }
        foundIdx = strIdxStart + i;
        break;
      }

      if (foundIdx == -1) {
        return false;
      }
      patIdxStart = patIdxTmp;
      strIdxStart = foundIdx + patLength;
    }

    // All characters in the string are used. Check if only '*'s are left
    // in the pattern. If so, we succeeded. Otherwise, failure.
    return allStars(patArr, patIdxStart, patIdxEnd);
  }

  /**
   * Tests whether a string matches against a pattern.
   * The pattern may contain two special characters:<br>
   * '*' means zero or more characters<br>
   * '?' means one and only one character
   *
   * @param pattern The pattern to match against.
   *                Must not be <code>null</code>.
   * @param str     The string which must be matched against the pattern.
   *                Must not be <code>null</code>.
   *
   * @return <code>true</code> if the string matches against the pattern,
   *         or <code>false</code> otherwise.
   */
  static boolean match(String pattern, String str) {
    return match(pattern, str, true);
  }

  static boolean matchPath(String[] tokPattern, int patEnd, String[] tokStr, int strEnd,
                           boolean isCaseSensitive) {
    int patIdxStart = 0;
    int patIdxEnd = Math.min(patEnd, tokPattern.length - 1);
    int strIdxStart = 0;
    int strIdxEnd = Math.min(strEnd, tokStr.length - 1);

    // up to first '**'
    while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
      String patDir = tokPattern[patIdxStart];
      if (patDir.equals(DEEP_TREE_MATCH)) {
        break;
      }
      if (!match(patDir, tokStr[strIdxStart], isCaseSensitive)) {
        return false;
      }
      patIdxStart++;
      strIdxStart++;
    }
    if (strIdxStart > strIdxEnd) {
      // String is exhausted
      for (int i = patIdxStart; i <= patIdxEnd; i++) {
        if (!tokPattern[i].equals(DEEP_TREE_MATCH)) {
          return false;
        }
      }
      return true;
    }
    if (patIdxStart > patIdxEnd) {
      // String not exhausted, but pattern is. Failure.
      return false;
    }

    // up to last '**'
    while (patIdxStart <= patIdxEnd && strIdxStart <= strIdxEnd) {
      String patDir = tokPattern[patIdxEnd];
      if (patDir.equals(DEEP_TREE_MATCH)) {
        break;
      }
      if (!match(patDir, tokStr[strIdxEnd], isCaseSensitive)) {
        return false;
      }
      patIdxEnd--;
      strIdxEnd--;
    }
    if (strIdxStart > strIdxEnd) {
      // String is exhausted
      for (int i = patIdxStart; i <= patIdxEnd; i++) {
        if (!tokPattern[i].equals(DEEP_TREE_MATCH)) {
          return false;
        }
      }
      return true;
    }

    while (patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd) {
      int patIdxTmp = -1;
      for (int i = patIdxStart + 1; i <= patIdxEnd; i++) {
        if (tokPattern[i].equals(DEEP_TREE_MATCH)) {
          patIdxTmp = i;
          break;
        }
      }
      if (patIdxTmp == patIdxStart + 1) {
        // '**/**' situation, so skip one
        patIdxStart++;
        continue;
      }
      // Find the pattern between padIdxStart & padIdxTmp in str between
      // strIdxStart & strIdxEnd
      int patLength = (patIdxTmp - patIdxStart - 1);
      int strLength = (strIdxEnd - strIdxStart + 1);
      int foundIdx = -1;
      strLoop:
      for (int i = 0; i <= strLength - patLength; i++) {
        for (int j = 0; j < patLength; j++) {
          String subPat = tokPattern[patIdxStart + j + 1];
          String subStr = tokStr[strIdxStart + i + j];
          if (!match(subPat, subStr, isCaseSensitive)) {
            continue strLoop;
          }
        }
        foundIdx = strIdxStart + i;
        break;
      }
      if (foundIdx == -1) {
        return false;
      }

      patIdxStart = patIdxTmp;
      strIdxStart = foundIdx + patLength;
    }

    for (int i = patIdxStart; i <= patIdxEnd; i++) {
      if (!DEEP_TREE_MATCH.equals(tokPattern[i])) {
        return false;
      }
    }
    return true;
  }

  static boolean matchPath(String[] tokPattern, String[] tokStr, boolean isCaseSensitive) {
    return matchPath(tokPattern, tokPattern.length - 1, tokStr, tokStr.length - 1, isCaseSensitive);
  }
}
