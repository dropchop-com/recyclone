package com.dropchop.recyclone.rest.jackson;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.api.filtering.CollectionPathSegment;
import com.dropchop.recyclone.model.api.filtering.FieldFilter;
import com.dropchop.recyclone.model.api.filtering.FilteringState;
import com.dropchop.recyclone.model.api.filtering.PathSegment;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;

import static com.dropchop.recyclone.model.api.filtering.PathSegment.ROOT_OBJECT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 30. 08. 22.
 */
@Slf4j
public class FilteringJsonGenerator extends JsonGeneratorDelegate {

  public interface Invokable {
    void invoke() throws IOException;
  }

  public static class WriteState {
    public final Invokable invokable;

    public WriteState(Invokable invokable) {
      this.invokable = invokable;
    }

    public Boolean matchingStart(WriteState state) {
      return null;
    }
  }

  public static class FieldNameState extends WriteState {
    public final String field;
    public FieldNameState(String field, Invokable invokable) {
      super(invokable);
      this.field = field;
    }
  }

  public static class ObjectStartState extends WriteState {

    public ObjectStartState(Invokable invokable) {
      super(invokable);
    }
  }

  public static class ObjectEndState extends WriteState {
    public ObjectEndState(Invokable invokable) {
      super(invokable);
    }

    public Boolean matchingStart(WriteState state) {
      return state instanceof ObjectStartState;
    }
  }

  public static class ArrayStartState extends WriteState {

    public ArrayStartState(Invokable invokable) {
      super(invokable);
    }
  }

  public static class ArrayEndState extends WriteState {
    public ArrayEndState(Invokable invokable) {
      super(invokable);
    }

    public Boolean matchingStart(WriteState state) {
      return state instanceof ArrayStartState;
    }
  }

  private final FieldFilter filter;
  private final FilteringState state;
  private final LinkedList<WriteState> writeState = new LinkedList<>();

  public FilteringJsonGenerator(JsonGenerator d, FieldFilter fieldFilter) {
    super(d, false);
    this.filter = fieldFilter;
    this.state = new FilteringState();
  }

  public void outputDelayedWriteState() throws IOException {
    for (WriteState ws : writeState) {
      ws.invokable.invoke();
    }
  }

  @SuppressWarnings("unused")
  public boolean continueSerialization(Object o) {
    PathSegment current = state.currentSegment();
    if (current == null) { // root
      return true;
    }
    //noinspection StatementWithEmptyBody
    if (o instanceof Model) {
      //log.info("Continue serialization [{}] -> [{}] dive [{}].", o, current, current.dive());
    }
    return current.dive();
  }

  private PathSegment writeEnd(WriteState ws) {
    if (state.dived()) {
      state.pollObject();
      WriteState last = writeState.peekLast();
      Boolean matching = ws.matchingStart(last);
      if (matching != null && matching) {// empty object or array
        writeState.pollLast(); //remove start and don't add end
        last = writeState.peekLast();
        if (last instanceof FieldNameState) { // remove field name write state
          writeState.pollLast();
        }
      } else {
        writeState.add(ws);
      }
    }
    return state.pollSegment();
  }

  private boolean skipDive(String curr, PathSegment segment, Object forValue) {
    //check filter dive
    boolean dive = filter.dive(segment);
    if (!dive) {
      segment.dive(false);
      state.diveSkipped();
      return true;
    }
    if (curr != null) {
      writeState.add(new FieldNameState(curr, () -> delegate.writeFieldName(curr)));
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
  public void writeStartArray(Object forValue, int size) {
    String curr = state.pollField();
    PathSegment parent = state.currentSegment();
    PathSegment segment = new CollectionPathSegment(parent, curr == null ? ROOT_OBJECT : curr, state.currentObject());
    state.pushSegment(segment);

    log.info("Start array [{}] -> [{}] dive [{}].", forValue, segment, filter.dive(segment));
    if (skipDive(curr, segment, forValue)) {
      return;
    }

    if (forValue == null) {
      if (size < 0) {
        writeState.add(new ArrayStartState(() -> delegate.writeStartArray()));
      } else {
        //noinspection deprecation
        writeState.add(new ArrayStartState(() -> delegate.writeStartArray(size)));
      }
    } else {
      if (size < 0) {
        writeState.add(new ArrayStartState(() -> delegate.writeStartArray(forValue)));
      } else {
        writeState.add(new ArrayStartState(() -> delegate.writeStartArray(forValue, size)));
      }
    }
  }

  @Override
  public void writeStartArray() {
    this.writeStartArray(null, -1);
  }

  @Override
  public void writeStartArray(Object forValue) {
    this.writeStartArray(forValue, -1);
  }

  @Override
  public void writeStartArray(int size) {
    this.writeStartArray(null, size);
  }

  @Override
  public void writeEndArray() {
    @SuppressWarnings("unused")
    PathSegment segment = writeEnd(new ArrayEndState(() -> delegate.writeEndArray()));
    //log.info("End array [{}] valid.", segment);
  }

  /**
   * We are at curr object when we have to decide if we want or write
   * {
   *  "propName": {
   */
  @Override
  public void writeStartObject(Object forValue, int size) {
    String curr = state.pollField();
    PathSegment parent = state.currentSegment();

    PathSegment segment;
    if (parent instanceof CollectionPathSegment) {
      segment = parent; // we skip creating new PathSegment on start object when in collection
    } else {
      segment = new PathSegment(parent, curr == null ? ROOT_OBJECT : curr, state.currentObject());
    }
    state.pushSegment(segment);

    //log.info("Start object [{}] -> [{}] dive [{}].", forValue, segment, filter.dive(segment));
    if (skipDive(curr, segment, forValue)) {
      return;
    }

    if (forValue == null && size < 0) {
      writeState.add(new ObjectStartState(() -> delegate.writeStartObject()));
    } else if (forValue != null && size < 0) {
      writeState.add(new ObjectStartState(() -> delegate.writeStartObject(forValue)));
    } else {
      writeState.add(new ObjectStartState(() -> delegate.writeStartObject(forValue, size)));
    }
  }

  @Override
  public void writeStartObject() {
    this.writeStartObject(null, -1);
  }

  @Override
  public void writeStartObject(Object forValue) {
    this.writeStartObject(forValue, -1);
  }

  @Override
  public void writeEndObject() {
    PathSegment segment = writeEnd(new ObjectEndState(() -> delegate.writeEndObject()));
    //log.info("End object [{}].", segment);
    if (segment != null && segment.parent instanceof CollectionPathSegment collSegment) {
      collSegment.incCurrentIndex();
    }
  }

  public void writeFieldValue(Invokable invokable) {
    String field = state.pollField();
    PathSegment parent = state.currentSegment();
    PathSegment segment = new PathSegment(parent, field, state.currentObject());
    boolean test = filter.test(segment);
    log.info("Property [{}] test [{}]", segment, test);
    if (test) {
      writeState.add(new FieldNameState(field, () -> delegate.writeFieldName(field)));
      writeState.add(new WriteState(invokable));
    }
  }

  @Override
  public void writeFieldName(String name) {
    PathSegment current = state.currentSegment();
    if (current == null || current.dive()) { // root or valid
      //log.info("Field name [{}].", name);
      state.pushField(name);
    }
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
  public void writeString(String value) {
    writeFieldValue(() -> delegate.writeString(value));
  }

  @Override
  public void writeString(char[] text, int offset, int len) {
    writeFieldValue(() -> delegate.writeString(text, offset, len));
  }

  @Override
  public void writeString(SerializableString value) {
    writeFieldValue(() -> delegate.writeString(value));
  }

  @Override
  public void writeRawUTF8String(byte[] text, int offset, int length) {
    writeFieldValue(() -> delegate.writeRawUTF8String(text, offset, length));
  }

  @Override
  public void writeUTF8String(byte[] text, int offset, int length) {
    writeFieldValue(() -> delegate.writeUTF8String(text, offset, length));
  }

  @Override
  public void writeRaw(String text) {
    writeFieldValue(() -> delegate.writeRaw(text));
  }

  @Override
  public void writeRaw(String text, int offset, int len) {
    writeFieldValue(() -> delegate.writeRaw(text, offset, len));
  }

  @Override
  public void writeRaw(SerializableString text) {
    writeFieldValue(() -> delegate.writeRaw(text));
  }

  @Override
  public void writeRaw(char[] text, int offset, int len) {
    writeFieldValue(() -> delegate.writeRaw(text, offset, len));
  }

  @Override
  public void writeRaw(char c) {
    writeFieldValue(() -> delegate.writeRaw(c));
  }

  @Override
  public void writeRawValue(String text) {
    writeFieldValue(() -> delegate.writeRawValue(text));
  }

  @Override
  public void writeRawValue(String text, int offset, int len) {
    writeFieldValue(() -> delegate.writeRawValue(text, offset, len));
  }

  @Override
  public void writeRawValue(char[] text, int offset, int len) {
    writeFieldValue(() -> delegate.writeRawValue(text, offset, len));
  }

  @Override
  public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) {
    writeFieldValue(() -> delegate.writeBinary(b64variant, data, offset, len));
  }

  @Override
  public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) {
    int[] ret = new int[1];
    writeFieldValue(
      () -> ret[0] = delegate.writeBinary(b64variant, data, dataLength)
    );
    return ret[0];
  }

  @Override
  public void writeNumber(short v) {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(int v) {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(long v) {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(BigInteger v) {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(double v) {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(float v) {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(BigDecimal v) {
    writeFieldValue(() -> delegate.writeNumber(v));
  }

  @Override
  public void writeNumber(String encodedValue) throws UnsupportedOperationException {
    writeFieldValue(() -> delegate.writeNumber(encodedValue));
  }

  @Override
  public void writeBoolean(boolean v) {
    writeFieldValue(() -> delegate.writeBoolean(v));
  }

  @Override
  public void writeNull() {
    writeFieldValue(() -> delegate.writeNull());
  }

  @Override
  public void writeOmittedField(String fieldName) {
    writeFieldValue(() -> delegate.writeOmittedField(fieldName));
  }

  // 25-Mar-2015, tatu: These are tricky as they sort of predate actual filtering calls.
  //   Let's try to use current state as a clue at least...
  @Override
  public void writeObjectId(Object id) {
    writeFieldValue(() -> delegate.writeObjectId(id));
  }

  @Override
  public void writeObjectRef(Object id) {
    writeFieldValue(() -> delegate.writeObjectRef(id));
  }

  @Override
  public void writeTypeId(Object id) {
    writeFieldValue(() -> delegate.writeTypeId(id));
  }
}
