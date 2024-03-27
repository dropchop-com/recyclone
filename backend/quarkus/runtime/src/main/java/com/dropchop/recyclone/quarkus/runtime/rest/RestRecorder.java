package com.dropchop.recyclone.quarkus.runtime.rest;

import com.dropchop.recyclone.quarkus.runtime.spi.bean.RecycloneApplicationImpl;
import com.dropchop.recyclone.quarkus.runtime.spi.RecycloneApplicationFactory;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.annotations.Recorder;

import java.util.function.Supplier;

@Recorder
public class RestRecorder {

  public Supplier<RecycloneApplicationImpl> createApp() {
    RecycloneApplicationFactory producer = Arc.container().instance(RecycloneApplicationFactory.class).get();
    return producer::getApplication;
  }
}
