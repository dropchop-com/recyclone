package com.dropchop.recyclone.model.api.utils;

import com.dropchop.recyclone.model.api.expr.ParseException;
import com.dropchop.recyclone.model.api.expr.ReservedSymbols;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 27. 07. 22.
 */
class RelaxedJsonTest {

  @Test
  void parseBoolWhitespace() throws ParseException {
    String str = "{bool  :   true}";
    Map<String, Object> root = RelaxedJson.parse(str);
    assertEquals(Boolean.TRUE, root.get("bool"));
  }

  @Test
  void parseBoolList() throws ParseException {
    String str = "{bool: [true, false]}";
    Map<String, Object> root = RelaxedJson.parse(str);
    assertEquals(List.of(Boolean.TRUE, Boolean.FALSE), root.get("bool"));
  }

  @Test
  void parseDifferentQuotes() throws ParseException {
    String str = "{'color': 'FDFDFD'}";
    RelaxedJson.parse(str);
    str = "{\"color\": \"FDFDFD\"}";
    RelaxedJson.parse(str);
    str = "{color: '#FDFDFD', miki: 12.3}";
    RelaxedJson.parse(str);
  }

  @Test
  void parseStringDecimalDateDecimalLists() throws ParseException {
    String str = "{color: '#FDFDFD', miki: 12.3, date: '2022-07-12', arr:[1, 2, 3, 4]}";
    Map<String, Object> root = RelaxedJson.parse(str);
    assertEquals(Iso8601.fromIso("2022-07-12"), root.get("date"));
    assertEquals(new BigDecimal("12.3"), root.get("miki"));
    assertEquals(List.of(
      new BigDecimal(1),
      new BigDecimal(2),
      new BigDecimal(3),
      new BigDecimal(4)
    ), root.get("arr"));
  }

  @Test
  void parseStringDecimalDateDecimalBoolLists() throws ParseException {
    String str = "{color: '#FDFDFD', miki: 12.3, date: '2022-07-12', arr:[1, 2, 3, 4], bool : [ true, \t\nfalse ]}";
    Map<String, Object> root = RelaxedJson.parse(str);
    assertEquals(Iso8601.fromIso("2022-07-12"), root.get("date"));
    assertEquals(new BigDecimal("12.3"), root.get("miki"));
    assertEquals(List.of(Boolean.TRUE, Boolean.FALSE), root.get("bool"));
    assertEquals(List.of(
      new BigDecimal(1),
      new BigDecimal(2),
      new BigDecimal(3),
      new BigDecimal(4)
    ), root.get("arr"));
  }

  @Test
  void parseStringValueRelaxedDecimalDateDecimalBoolLists() throws ParseException {
    String str = "{color: #FDFDFD, miki: 12.3, date: 2022-07-12, arr:[1, 2, 3, 4], bool : [ true, \t\nfalse ]}";
    Map<String, Object> root = RelaxedJson.parse(str);
    assertEquals(Iso8601.fromIso("2022-07-12"), root.get("date"));
    assertEquals(new BigDecimal("12.3"), root.get("miki"));
    assertEquals(List.of(Boolean.TRUE, Boolean.FALSE), root.get("bool"));
    assertEquals(List.of(
      new BigDecimal(1),
      new BigDecimal(2),
      new BigDecimal(3),
      new BigDecimal(4)
    ), root.get("arr"));
  }

  @Test
  void parseRelaxedUnnamedAttributesDecimal() throws ParseException {
    String str = "{color: '#FDFDFD', 12.3, miki: 12.3}";
    Map<String, Object> root = RelaxedJson.parse(str);
    assertEquals(new BigDecimal("12.3"), root.get(ReservedSymbols.DEFAULT_ATTR_NAME));
  }

  @Test
  void parseRelaxedUnnamedAttributesDecimalOverwrite() throws ParseException {
    String str = "{color: '#FDFDFD', miki: 12.3, 12.3, 12.2}";
    Map<String, Object> root = RelaxedJson.parse(str);
    assertEquals(new BigDecimal("12.2"), root.get(ReservedSymbols.DEFAULT_ATTR_NAME));
  }

  @Test
  void parseRelaxedUnnamedAttributesBool() throws ParseException {
    String str = "{true, color: '#FDFDFD'}";
    Map<String, Object> root = RelaxedJson.parse(str);
    assertEquals(Boolean.TRUE, root.get(ReservedSymbols.DEFAULT_ATTR_NAME));
  }

  @Test
  void parseRelaxedUnnamedAttributesDateEmptySubset() throws ParseException {
    String str = "{color: '#FDFDFD', miki: 12.3, '2022-07-12', sub}";
    Map<String, Object> root = RelaxedJson.parse(str);
    assertEquals(Iso8601.fromIso("2022-07-12"), root.get(ReservedSymbols.DEFAULT_ATTR_NAME));
    assertEquals(new HashMap<>(), root.get("sub"));
  }
}