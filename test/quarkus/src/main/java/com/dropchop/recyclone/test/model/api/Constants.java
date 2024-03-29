package com.dropchop.recyclone.test.model.api;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 7. 03. 24.
 */
public interface Constants {
  interface Domains {
    interface Test {
      String DUMMY = "test.dummy";
    }
  }

  interface Tags {
    String TEST = "test";
  }

  interface Paths {
    String TEST_SEGMENT = "/test";
    interface Test {
      String DUMMY_SEGMENT = "/dummy";
      String DUMMY = TEST_SEGMENT + DUMMY_SEGMENT;
    }
  }
}
