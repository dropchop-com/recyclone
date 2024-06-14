package com.dropchop.recyclone.quarkus.runtime.app;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import io.quarkus.runtime.LaunchMode;

@SuppressWarnings("unused")
public interface RecycloneApplication {
  RecycloneBuildConfig getBuildConfig();

  JsonSerializationTypeConfig getJsonSerializationTypeConfig();

  MapperSubTypeConfig getMapperSubTypeConfig();

  LaunchMode getLaunchMode();
}
