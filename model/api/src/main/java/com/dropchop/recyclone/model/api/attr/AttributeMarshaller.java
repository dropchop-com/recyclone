package com.dropchop.recyclone.model.api.attr;

import com.dropchop.recyclone.model.api.expr.ReservedSymbols;
import com.dropchop.recyclone.model.api.expr.ParseException;
import com.dropchop.recyclone.model.api.expr.parse.ParserState;
import com.dropchop.recyclone.model.api.utils.RelaxedJson;
import com.dropchop.recyclone.model.api.utils.Strings;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

import static com.dropchop.recyclone.model.api.expr.ReservedSymbols.*;
import static com.dropchop.recyclone.model.api.utils.Iso8601.DATE_TIME_MS_TZ_FORMATTER;

/**
 * AttributeMarshaller which supports marshalling / unmarshalling of attributes to string.
 * (This string representation ready to be written to a database text column)
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 27. 07. 22.
 */
public class AttributeMarshaller {

  public static class ParserListener implements RelaxedJson.Listener<AttributeSet, AttributeValueList<Object>> {
    @Override
    public AttributeSet onContainer() {
      return AttributeSet.builder()
        .value(new LinkedHashSet<>())
        .name(ReservedSymbols.DEFAULT_ATTR_NAME)
        .build();
    }

    @Override
    public void onProperty(AttributeSet container, String name, Object value) {
      Set<Attribute<?>> attributeSet = container.getValue();
      if (attributeSet == null) {
        attributeSet = new LinkedHashSet<>();
        container.setValue(attributeSet);
      }
      Attribute<?> attribute;
      if (value instanceof AttributeSet) {
        ((AttributeSet) value).setName(name);
        attribute = (AttributeSet) value;
      } else if (value instanceof AttributeValueList<?>) {
        ((AttributeValueList<?>) value).setName(name);
        attribute = (AttributeValueList<?>) value;
      } else if (value instanceof String) {
        attribute = new AttributeString(name, (String) value);
      } else if (value instanceof Boolean) {
        attribute = new AttributeBool(name, (Boolean) value);
      } else if (value instanceof BigDecimal) {
        attribute = new AttributeDecimal(name, (BigDecimal) value);
      } else if (value instanceof ZonedDateTime) {
        attribute = new AttributeDate(name, (ZonedDateTime) value);
      } else if (value == null) {
        for (Iterator<Attribute<?>> attrIt = attributeSet.iterator(); attrIt.hasNext();) {
          attribute = attrIt.next();
          if (attribute != null && name.equals(attribute.getName())) {
            attrIt.remove();
          }
        }
        return;
      } else {
        throw new IllegalArgumentException("Unsupported property [" + name + "] value type []");
      }

      attributeSet.add(attribute);
    }

    @Override
    public AttributeValueList<Object> onList() {
      return new AttributeValueList<>();
    }

    @Override
    public void onItem(AttributeValueList<Object> list, Object item) {
      List<Object> contents = list.getValue();
      if (contents == null) {
        contents = new ArrayList<>();
        list.setValue(contents);
      }
      contents.add(item);
    }
  }

  /**
   * Parses (relaxed) JSON string to attribute set.
   *
   * For instance relaxed JSON:
   * {color: '#FDFDFD', miki: 12.3, date: '2022-07-12', arr:[1, 2, 3, 4]}
   *
   * or real JSON:
   * {'color': '#FDFDFD', 'miki': 12.3, 'date': '2022-07-12', 'arr1':[1, 2, 3, 4.1]}
   *
   * @param state optional ParserState to give more data to parsing exception.
   * @param str JSON string
   * @return attribute set.
   * @throws ParseException if JSON string is invalid
   */
  public static AttributeSet parse(ParserState state, String str) throws ParseException {
    return RelaxedJson.parse(state, new ParserListener(), str);
  }

  /**
   * Parses (relaxed) JSON string to attribute set.
   *
   * @param str JSON string
   * @return attribute set.
   * @throws RuntimeException if JSON string is invalid
   */
  public static AttributeSet parse(String str) {
    try {
      return RelaxedJson.parse(new ParserListener(), str);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Unmarshall string value to attribute object given attribute name and expected type.
   *
   * @param name attribute name string
   * @param strValue attribute serialized string value.
   * @param tClass expected value type class.
   * @return attribute instance (One of AttributeString, AttributeBool, AttributeDate,
   * AttributeDecimal, AttributeSet, AttributeValueList)
   * @param <X> expected value type.
   */
  public static <X> Attribute<X> unmarshall(String name, String strValue, Class<X> tClass) {
    if (strValue == null) {
      //noinspection unchecked
      return (Attribute<X>) new AttributeToRemove(name);
    }
    if (String.class.isAssignableFrom(tClass)) {
      //noinspection unchecked
      return (Attribute<X>) new AttributeString(name, strValue);
    } else if (Boolean.class.isAssignableFrom(tClass)) {
      //noinspection unchecked
      return (Attribute<X>) new AttributeBool(name, Boolean.valueOf(strValue));
    } else if (BigDecimal.class.isAssignableFrom(tClass)) {
      //noinspection unchecked
      return (Attribute<X>) new AttributeDecimal(name, new BigDecimal(strValue));
    } else if (ZonedDateTime.class.isAssignableFrom(tClass)) {
      //noinspection unchecked
      return (Attribute<X>) new AttributeDate(name, ZonedDateTime.parse(strValue, DATE_TIME_MS_TZ_FORMATTER.get()));
    } else if (Set.class.isAssignableFrom(tClass)) {
      String trimmed = strValue.trim();
      if (!trimmed.startsWith("{")) {
        trimmed = "{" + trimmed + "}";
      }
      //noinspection unchecked
      return (Attribute<X>) parse(trimmed);
    } else if (List.class.isAssignableFrom(tClass)) {
      String trimmed = strValue.trim();
      if (!trimmed.startsWith("[")) {
        trimmed = "{" + ReservedSymbols.DEFAULT_ATTR_NAME + ":[" + trimmed + "]}";
      } else {
        trimmed = "{" + ReservedSymbols.DEFAULT_ATTR_NAME + ":" + trimmed + "}";
      }
      AttributeSet set = parse(trimmed);
      return set.getAttribute(ReservedSymbols.DEFAULT_ATTR_NAME);
    } else {
      throw new UnsupportedOperationException("Invalid attribute value for string conversion!");
    }
  }

  private static String beginSeq(Attribute<?> attr) {
    if (attr instanceof AttributeString) {
      return VALUE_SYMBOL;
    } else if (attr instanceof AttributeDate) {
      return VALUE_SYMBOL;
    } else if (attr instanceof AttributeSet) {
      return ATTRIBUTE_DATA_SYMBOL_START;
    } else if (attr instanceof AttributeValueList) {
      return "[";
    }
    return "";
  }

  private static String endSeq(Attribute<?> attr) {
    if (attr instanceof AttributeString) {
      return VALUE_SYMBOL;
    } else if (attr instanceof AttributeDate) {
      return VALUE_SYMBOL;
    } else if (attr instanceof AttributeSet) {
      return ATTRIBUTE_DATA_SYMBOL_END;
    } else if (attr instanceof AttributeValueList) {
      return "]";
    }
    return "";
  }

  private static String marshall(Attribute<?> attribute, boolean nested) {
    if (attribute instanceof AttributeToRemove) {
      if (nested) {
        return null;
      }
      return null;
    } else if (attribute instanceof AttributeString) {
      if (nested) {
        return Strings.jsonEscape(((AttributeString) attribute).getValue());
      }
      return ((AttributeString) attribute).getValue();
    } else if (attribute instanceof AttributeBool) {
      Boolean bool = ((AttributeBool) attribute).getValue();
      if (bool == null) {
        return null;
      }
      return bool.toString();
    } else if (attribute instanceof AttributeDate) {
      ZonedDateTime dateTime = ((AttributeDate) attribute).getValue();
      if (dateTime == null) {
        return null;
      }
      return DATE_TIME_MS_TZ_FORMATTER.get().format(dateTime);
    } else if (attribute instanceof AttributeDecimal) {
      BigDecimal decimal = ((AttributeDecimal) attribute).getValue();
      if (decimal == null) {
        return null;
      }
      return decimal.toPlainString();
    } else if (attribute instanceof AttributeSet) {
      Set<Attribute<?>> attributeSet = ((AttributeSet) attribute).getValue();
      if (attributeSet == null) {
        return null;
      }
      StringBuilder strValue = new StringBuilder();
      for (Iterator<Attribute<?>> subAttrIt = attributeSet.iterator(); subAttrIt.hasNext();) {
        Attribute<?> subAttr = subAttrIt.next();
        strValue.append(VALUE_SYMBOL);
        strValue.append(subAttr.getName().replace(VALUE_SYMBOL, ESCAPE_SYMBOL + VALUE_SYMBOL));
        strValue.append(VALUE_SYMBOL);
        strValue.append(NAME_DELIM);
        strValue.append(beginSeq(subAttr));
        strValue.append(marshall(subAttr, true));
        strValue.append(endSeq(subAttr));
        if (subAttrIt.hasNext()) {
          strValue.append(ATTRIBUTE_DATA_DELIM);
          strValue.append(" ");
        }
      }
      return strValue.toString();
    } else if (attribute instanceof AttributeValueList<?>) {
      List<?> values = ((AttributeValueList<?>) attribute).getValue();
      if (values == null) {
        return null;
      }
      StringBuilder strValue = new StringBuilder();
      for (Iterator<?> valuesIt = values.iterator(); valuesIt.hasNext();) {
        Object val = valuesIt.next();
        if (val == null) {
          continue;
        }
        if (val instanceof String) {
          strValue.append(VALUE_SYMBOL);
          strValue.append(((String) val).replace(VALUE_SYMBOL, ESCAPE_SYMBOL + VALUE_SYMBOL));
          strValue.append(VALUE_SYMBOL);
        } else if (val instanceof Boolean) {
          strValue.append(val);
        } else if (val instanceof BigDecimal) {
          strValue.append(((BigDecimal) val).toPlainString());
        } else if (val instanceof ZonedDateTime) {
          strValue.append(VALUE_SYMBOL);
          strValue.append(DATE_TIME_MS_TZ_FORMATTER.get().format((ZonedDateTime) val));
          strValue.append(VALUE_SYMBOL);
        } else if (val instanceof Attribute<?>) {
          strValue.append(ATTRIBUTE_DATA_SYMBOL_START);
          strValue.append(VALUE_SYMBOL);
          strValue.append(((Attribute<?>) val).getName());
          strValue.append(VALUE_SYMBOL);
          strValue.append(NAME_DELIM);
          strValue.append(beginSeq((Attribute<?>) val));
          strValue.append(marshall((Attribute<?>) val, true));
          strValue.append(endSeq((Attribute<?>) val));
          strValue.append(ATTRIBUTE_DATA_SYMBOL_END);
        }
        if (valuesIt.hasNext()) {
          strValue.append(ATTRIBUTE_DATA_DELIM);
          strValue.append(" ");
        }
      }
      return strValue.toString();
    } else {
      throw new UnsupportedOperationException("Invalid attribute value for string conversion!");
    }
  }

  /**
   * Marshall attribute value to string:
   *
   * The string output is:
   * - AttributeString: unescaped raw string value
   * - AttributeBool: string "true" / "false"
   * - AttributeDate: date string formatted as "true"
   * - AttributeDecimal: string representation of BigDecimal
   * - AttributeValueList: string with serialized items delimited by ","
   * - AttributeSet: string serialized as JSON
   *
   * @param attribute attribute
   * @return string representation of attribute value
   */
  public static String marshall(Attribute<?> attribute) {
    return marshall(attribute, false);
  }
}
