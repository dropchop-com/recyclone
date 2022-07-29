package com.dropchop.recyclone.model.api.utils;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 27. 07. 22.
 */
public interface Strings {

  static boolean isAlphaNumeric(String s) {
    return s != null && s.matches("^[\\p{L}][\\p{L}\\d]*$");
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
        if (c < '\u0020') {
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
}
