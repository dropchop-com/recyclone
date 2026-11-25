package com.dropchop.recyclone.base.api.model.marker;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface HasOptimisticConcurrency {

  /**
   * The caller's expected revision for the conditional write.
   */
  @JsonIgnore
  String getExpectedRevision();

  void setExpectedRevision(String expectedRevision);
}
