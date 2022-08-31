package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 30. 08. 22.
 */
public class FilteringJsonGenerator extends JsonGeneratorDelegate {

  private final FilteringContext context;

  public FilteringJsonGenerator(JsonGenerator d, FilteringContext context) {
    super(d, false);
    this.context = context;
  }

  private void _writeFieldName() throws IOException {
    String name = context.poll();
    if (name != null) {
      delegate.writeFieldName(name);
    }
  }

  @Override
  public void writeStartArray(Object forValue, int size) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    if (forValue == null) {
      if (size < 0) {
        delegate.writeStartArray();
      } else {
        delegate.writeStartArray(size);
      }
    } else {
      if (size < 0) {
        delegate.writeStartArray(forValue);
      } else {
        delegate.writeStartArray(forValue, size);
      }
    }
  }

  @Override
  public void writeStartArray() throws IOException {
    this.writeStartArray(null, -1);
  }

  @Override
  public void writeStartArray(Object forValue) throws IOException {
    this.writeStartArray(forValue, -1);
  }

  @Override
  public void writeStartArray(int size) throws IOException {
    this.writeStartArray(null, size);
  }

  @Override
  public void writeEndArray() throws IOException {
    if (context.isBlocked()) {
      return;
    }
    delegate.writeEndArray();
  }

  @Override
  public void writeStartObject(Object forValue, int size) throws IOException {
    context.incLevel();
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    if (forValue == null && size < 0) {
      delegate.writeStartObject();
    } else if (forValue != null && size < 0) {
      delegate.writeStartObject(forValue);
    } else {
      delegate.writeStartObject(forValue, size);
    }
  }

  @Override
  public void writeStartObject() throws IOException {
    this.writeStartObject(null, -1);
  }

  @Override
  public void writeStartObject(Object forValue) throws IOException {
    this.writeStartObject(forValue, -1);
  }

  @Override
  public void writeEndObject() throws IOException {
    context.decLevel();
    if (context.isBlocked()) {
      return;
    }
    delegate.writeEndObject();
  }

  @Override
  public void writeFieldName(String name) {
    context.offer(name);
  }

  @Override
  public void writeFieldName(SerializableString name) {
    this.writeFieldName(name.getValue());
  }

  @Override
  public void writeFieldId(long id) {
    this.writeFieldName(Long.toString(id));
  }

  /*--------------------------------------------------------
   *        Write values
   *-------------------------------------------------------*/

  @Override
  public void writeString(String value) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeString(value);
  }

  @Override
  public void writeString(char[] text, int offset, int len) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeString(text, offset, len);
  }

  @Override
  public void writeString(SerializableString value) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeString(value);
  }

  @Override
  public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeRawUTF8String(text, offset, length);
  }

  @Override
  public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeUTF8String(text, offset, length);
  }

  @Override
  public void writeRaw(String text) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeRaw(text);
  }

  @Override
  public void writeRaw(String text, int offset, int len) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeRaw(text);
  }

  @Override
  public void writeRaw(SerializableString text) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeRaw(text);
  }

  @Override
  public void writeRaw(char[] text, int offset, int len) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeRaw(text, offset, len);
  }

  @Override
  public void writeRaw(char c) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeRaw(c);
  }

  @Override
  public void writeRawValue(String text) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeRaw(text);
  }

  @Override
  public void writeRawValue(String text, int offset, int len) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeRaw(text, offset, len);
  }

  @Override
  public void writeRawValue(char[] text, int offset, int len) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeRaw(text, offset, len);
  }

  @Override
  public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeBinary(b64variant, data, offset, len);
  }

  @Override
  public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException {
    if (context.isBlocked()) {
      return -1;
    }
    _writeFieldName();
    return delegate.writeBinary(b64variant, data, dataLength);
  }

  @Override
  public void writeNumber(short v) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeNumber(v);
  }

  @Override
  public void writeNumber(int v) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeNumber(v);
  }

  @Override
  public void writeNumber(long v) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeNumber(v);
  }

  @Override
  public void writeNumber(BigInteger v) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeNumber(v);
  }

  @Override
  public void writeNumber(double v) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeNumber(v);
  }

  @Override
  public void writeNumber(float v) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeNumber(v);
  }

  @Override
  public void writeNumber(BigDecimal v) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeNumber(v);
  }

  @Override
  public void writeNumber(String encodedValue) throws IOException, UnsupportedOperationException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeNumber(encodedValue);
  }

  @Override
  public void writeBoolean(boolean v) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeBoolean(v);
  }

  @Override
  public void writeNull() throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeNull();
  }

  @Override
  public void writeOmittedField(String fieldName) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    _writeFieldName();
    delegate.writeOmittedField(fieldName);
  }

  // 25-Mar-2015, tatu: These are tricky as they sort of predate actual filtering calls.
  //   Let's try to use current state as a clue at least...
  @Override
  public void writeObjectId(Object id) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    delegate.writeObjectId(id);
  }

  @Override
  public void writeObjectRef(Object id) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    delegate.writeObjectRef(id);
  }

  @Override
  public void writeTypeId(Object id) throws IOException {
    if (context.isBlocked()) {
      return;
    }
    delegate.writeTypeId(id);
  }
}
