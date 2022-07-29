package com.dropchop.recyclone.model.api.attr;

import com.dropchop.recyclone.model.api.expr.ReservedSymbols;
import com.dropchop.recyclone.model.api.utils.Iso8601;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 27. 07. 22.
 */
class AttributeMarshallerTest {

  @Test
  void parse() {
    String str = "{color: '#FDFDFD', miki: 12.3, date: '2022-07-12', arr:[1, 2, 3, 4]}";
    AttributeSet set = AttributeMarshaller.parse(str);
    assertEquals(ReservedSymbols.DEFAULT_ATTR_NAME, set.getName());
    assertEquals("#FDFDFD", set.getAttributeValue("color"));
    assertEquals(new BigDecimal("12.3"), set.getAttributeValue("miki"));
    assertEquals(Iso8601.fromIso("2022-07-12"), set.getAttributeValue("date"));
    assertEquals(List.of(
      new BigDecimal(1),
      new BigDecimal(2),
      new BigDecimal(3),
      new BigDecimal(4)
    ), set.getAttributeValue("arr"));
  }

  @Test
  void unmarshall() {
  }

  @Test
  void marshallString() {
    String marshalled = AttributeMarshaller.marshall(new AttributeString("test", "L'Oreal"));
    assertEquals("L'Oreal", marshalled);
  }

  @Test
  void marshallBool() {
    String marshalled = AttributeMarshaller.marshall(new AttributeBool("test", Boolean.FALSE));
    assertEquals("false", marshalled);
  }

  @Test
  void marshallDate() {
    String marshalled = AttributeMarshaller.marshall(new AttributeDate("test", Iso8601.fromIso("2022-07-12")));
    assertEquals("2022-07-12T00:00:00.000+02:00", marshalled);
  }

  @Test
  void marshallListNum() {
    String marshalled = AttributeMarshaller.marshall(new AttributeValueList<>("test", List.of(
      new BigDecimal("1.1"),
      new BigDecimal("2.2"),
      new BigDecimal("3.3"),
      new BigDecimal("4.4")
    )));
    assertEquals("1.1, 2.2, 3.3, 4.4", marshalled);
  }

  @Test
  void marshallListStr() {
    String marshalled = AttributeMarshaller.marshall(new AttributeValueList<>("test", List.of(
      "L'Oreal",
      "L'Oreal"
    )));
    assertEquals("'L\\'Oreal', 'L\\'Oreal'", marshalled);
  }

  @Test
  void marshallSet() {
    String str = "{'color': '#FDFDFD', 'miki': 12.3, 'date': '2022-07-12', " +
      "'arr1':[1, 2, 3, 4.1], 'arr2':['aa', 'ab', 'ac', 'ad'], 'arr3': [true, false]}";
    AttributeSet set = AttributeMarshaller.parse(str);
    set.addAttribute(new AttributeString("test", "L'Oreal"));
    String marshalled = AttributeMarshaller.marshall(set);
    str = "'color':'#FDFDFD', 'miki':12.3, 'date':'2022-07-12T00:00:00.000+02:00', " +
      "'arr1':[1, 2, 3, 4.1], 'arr2':['aa', 'ab', 'ac', 'ad'], 'arr3':[true, false], 'test':'L\\'Oreal'";
    assertEquals(str, marshalled);
  }
}