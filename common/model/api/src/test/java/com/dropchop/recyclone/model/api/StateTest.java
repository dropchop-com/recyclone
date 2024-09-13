package com.dropchop.recyclone.model.api;

import com.dropchop.recyclone.model.api.base.State;
import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 8. 03. 22.
 */
class StateTest {

  @Test
  void code() throws Exception {
    State.Code.Created created = State.Code.created;
    assertEquals(CharBuffer.wrap("created"), CharBuffer.wrap(created));
    assertEquals(CharBuffer.wrap("created"), CharBuffer.wrap(created.toString()));
    assertEquals(CharBuffer.wrap(created), CharBuffer.wrap(created.toString()));
  }

}