package com.dropchop.recyclone.quarkus.runtime.spi;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;

@SuppressWarnings("unused")
public interface RecycloneApplication {
  RecycloneBuildConfig getBuildConfig();

  RecycloneRuntimeConfig getRuntimeConfig();

  JsonSerializationTypeConfig getJsonSerializationTypeConfig();

  MapperSubTypeConfig getMapperSubTypeConfig();
}
