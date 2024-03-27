package com.dropchop.recyclone.quarkus.runtime.spi.bean;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneRuntimeConfig;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 15. 03. 24.
 */
@SuppressWarnings("unused")
@RegisterForReflection
public class RecycloneApplicationImpl implements RecycloneApplication {

  private final JsonSerializationTypeConfig jsonSerializationTypeConfig;

  private final MapperSubTypeConfig mapperSubTypeConfig;

  private final LaunchMode launchMode;

  private final RecycloneBuildConfig buildConfig;

  private final RecycloneRuntimeConfig runtimeConfig;


  public RecycloneApplicationImpl(RecycloneRuntimeConfig runtimeConfig,
                                  RecycloneBuildConfig buildConfig,
                                  LaunchMode launchMode,
                                  JsonSerializationTypeConfig jsonSerializationTypeConfig,
                                  MapperSubTypeConfig mapperSubTypeConfig) {
    this.runtimeConfig = runtimeConfig;
    this.buildConfig = buildConfig;
    this.jsonSerializationTypeConfig = jsonSerializationTypeConfig;
    this.mapperSubTypeConfig = mapperSubTypeConfig;
    this.launchMode = null;
  }

  @Override
  public RecycloneBuildConfig getBuildConfig() {
    return buildConfig;
  }

  @Override
  public RecycloneRuntimeConfig getRuntimeConfig() {
    return runtimeConfig;
  }

  @Override
  public JsonSerializationTypeConfig getJsonSerializationTypeConfig() {
    return jsonSerializationTypeConfig;
  }

  @Override
  public MapperSubTypeConfig getMapperSubTypeConfig() {
    return mapperSubTypeConfig;
  }

  public LaunchMode getLaunchMode() {
    return launchMode;
  }
}
