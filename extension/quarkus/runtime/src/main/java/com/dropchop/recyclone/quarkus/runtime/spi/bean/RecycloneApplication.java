package com.dropchop.recyclone.quarkus.runtime.spi.bean;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneRuntimeConfig;

@SuppressWarnings("unused")
public interface RecycloneApplication {
  RecycloneBuildConfig getBuildConfig();

  RecycloneRuntimeConfig getRuntimeConfig();

  JsonSerializationTypeConfig getJsonSerializationTypeConfig();

  MapperSubTypeConfig getMapperSubTypeConfig();
}
