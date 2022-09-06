package com.dropchop.recyclone.rest.jaxrs.filtering;

import com.dropchop.recyclone.model.dto.filtering.CollectionPathSegment;
import com.dropchop.recyclone.model.dto.filtering.FieldFilter;
import com.dropchop.recyclone.model.dto.filtering.FilteringState;
import com.dropchop.recyclone.model.dto.filtering.PathSegment;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;

import static com.dropchop.recyclone.model.dto.filtering.PathSegment.ROOT_OBJECT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 30. 08. 22.
 */
@Slf4j
public class FilteringJsonGenerator extends JsonGeneratorDelegate {

  public interface Invokable {
    void invoke() throws IOException;
  }

  private final FieldFilter filter;
  private final FilteringState state;

  public FilteringJsonGenerator(JsonGenerator d, FieldFilter fieldFilter) {
    super(d, false);
    this.filter = fieldFilter;
    this.state = new FilteringState();
  }

  public boolean continueSerialization() {
    PathSegment current = state.currentSegment();
    if (current == null) {
      return true;
    }
    @SuppressWarnings("UnnecessaryLocalVariable")
    boolean dive = filter.dive(current);
    //log.info("continueSerialization [{}] dive [{}].", current, dive);
    return dive;
  }

  private PathSegment writeEnd(Invokable invokable) throws IOException {
    if (state.dived()) {
      state.pollObject();
      invokable.invoke();
    }
    return state.pollSegment();
  }

  private boolean skipDive(String curr, PathSegment segment, Object forValue) throws IOException {
    //check filter dive
    boolean dive = filter.dive(segment);
    //log.info("Start [{}] dive [{}].", segment, dive);
    if (!dive) {
      state.diveSkipped();
      return true;
    }
    if (curr != null) {
      delegate.writeFieldName(curr);
    }
    state.pushObject(forValue);
    state.divePassed();
    return false;
  }

  /**
   * We are at curr object when we have to decide if we want or write
   * {
   *  "propName": [
   */
  @Override
  public void writeStartArray(Object forValue, int size) throws IOException {
    String curr = state.pollField();
    PathSegment parent = state.currentSegment();
    PathSegment segment = new CollectionPathSegment(parent, curr == null ? ROOT_OBJECT : curr, state.currentObject());
    state.pushSegment(segment);

    if (skipDive(curr, segment, forValue)) {
      return;
    }

    if (forValue == null) {
      if (size < 0) {
        delegate.writeStartArray();
      } else {
        //noinspection deprecation
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
    writeEnd(() -> delegate.writeEndArray());
    //log.info("End array [{}]", segment);
  }

  /**
   * We are at curr object when we have to decide if we want or write
   * {
   *  "propName": {
   */
  @Override
  public void writeStartObject(Object forValue, int size) throws IOException {
    String curr = state.pollField();
    PathSegment parent = state.currentSegment();

    PathSegment segment;
    if (parent instanceof CollectionPathSegment) {
      segment = parent; // we skip creating new PathSegment on start object when in collection
    } else {
      segment = new PathSegment(parent, curr == null ? ROOT_OBJECT : curr, state.currentObject());
    }
    state.pushSegment(segment);

    if (skipDive(curr, segment, forValue)) {
      return;
    }

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
    PathSegment segment = writeEnd(() -> delegate.writeEndObject());
    if (segment != null && segment.parent instanceof CollectionPathSegment collSegment) {
      collSegment.incCurrentIndex();
    }
  }

  public void writeFieldValue(Invokable invokable) throws IOException {
    //String field = fields.pollLast();
    String field = state.pollField();
    PathSegment parent = state.currentSegment();
    /*PathSegment segment;
    if (parent.parent != null &&
      parent.parent instanceof CollectionPathSegment &&
      parent.parent.name.equals(parent.name)) {
      segment = new PathSegment(parent.parent, field, parent.parent.referer);
    } else {
      segment = new PathSegment(parent, field, state.currentObject());
    }*/
    PathSegment segment = new PathSegment(parent, field, state.currentObject());
    //log.info("Property [{}]", segment);
    if (filter.test(segment)) {
      delegate.writeFieldName(field);
      invokable.invoke();
    }
  }

  @Override
  public void writeFieldName(String name) {
    state.pushField(name);
    //fields.offerLast(name);
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
    writeFieldValue(() -> delegate.writeString(value));
  }

  @Override
  public void writeString(char[] text, int offset, int len) throws IOException {
    writeFieldValue(() -> delegate.writeString(text, offset, len));
  }

  @Override
  public void writeString(SerializableString value) throws IOException {
    writeFieldValue(() -> delegate.writeString(value));
  }

  @Override
  public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
    writeFieldValue(() -> delegate.writeRawUTF8String(text, offset, length));
  }

  @Override
  public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
    writeFieldValue(() -> delegate.writeUTF8String(text, offset, length));
  }

  @Override
  public void writeRaw(String text) throws IOException {
    writeFieldValue(() -> delegate.writeRaw(text));
  }

  @Override
  public void writeRaw(String text, int offset, int len) throws IOException {
    writeFieldValue(() -> delegate.writeRaw(text, offset, len));
  }

  @Override
  public void writeRaw(SerializableString text) throws IOException {
    writeFieldValue(() -> delegate.writeRaw(text));
  }

  @Override
  public void writeRaw(char[] text, int offset, int len) throws IOException {
    writeFieldValue(() -> delegate.writeRaw(text, offset, len));
  }

  @Override
  public void writeRaw(char c) throws IOException {
    writeFieldValue(() -> delegate.writeRaw(c));
  }

  @Override
  public void writeRawValue(String text) throws IOException {
    writeFieldValue(() -> delegate.writeRawValue(text));
  }

  @Override
  public void writeRawValue(String text, int offset, int len) throws IOException {
    writeFieldValue(() -> delegate.writeRawValue(text, offset, len));
  }

  @Override
  public void writeRawValue(char[] text, int offset, int len) throws IOException {
    writeFieldValue(() -> delegate.writeRawValue(text, offset, len));
  }

  @Override
  public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
    writeFieldValue(() -> delegate.writeBinary(b64variant, data, offset, len));
  }

  @Override
  public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException {
    int[] ret = new int[1];
    writeFieldValue(
      () -> ret[0] = delegate.writeBinary(b64variant, data, dataLength)
    );
    return ret[0];
  }

  @Override
  public void writeNumber(short v) throws IOException {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(int v) throws IOException {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(long v) throws IOException {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(BigInteger v) throws IOException {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(double v) throws IOException {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(float v) throws IOException {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(BigDecimal v) throws IOException {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(String encodedValue) throws IOException, UnsupportedOperationException {
    writeFieldValue(() -> delegate.writeNumber(encodedValue));
  }

  @Override
  public void writeBoolean(boolean v) throws IOException {
    writeFieldValue(() -> delegate.writeBoolean(v));
  }

  @Override
  public void writeNull() throws IOException {
    writeFieldValue(() -> delegate.writeNull());
  }

  @Override
  public void writeOmittedField(String fieldName) throws IOException {
    writeFieldValue(() -> delegate.writeOmittedField(fieldName));
  }

  // 25-Mar-2015, tatu: These are tricky as they sort of predate actual filtering calls.
  //   Let's try to use current state as a clue at least...
  @Override
  public void writeObjectId(Object id) throws IOException {
    writeFieldValue(() -> delegate.writeObjectId(id));
  }

  @Override
  public void writeObjectRef(Object id) throws IOException {
    writeFieldValue(() -> delegate.writeObjectRef(id));
  }

  @Override
  public void writeTypeId(Object id) throws IOException {
    writeFieldValue(() -> delegate.writeTypeId(id));
  }
}
