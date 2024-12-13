package com.dropchop.recyclone.base.api.model;

import com.dropchop.recyclone.base.api.model.base.State;
import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 8. 03. 22.
 */
class StateTest {

  @Test
  void code() {
    State.Code.Created created = State.Code.created;
    assertEquals(CharBuffer.wrap("created"), CharBuffer.wrap(created));
    assertEquals(CharBuffer.wrap("created"), CharBuffer.wrap(created.toString()));
    assertEquals(CharBuffer.wrap(created), CharBuffer.wrap(created.toString()));
  }
}
