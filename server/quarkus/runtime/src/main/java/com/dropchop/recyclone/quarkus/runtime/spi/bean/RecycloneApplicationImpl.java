package com.dropchop.recyclone.quarkus.runtime.spi.bean;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 15. 03. 24.
 */
@Singleton
@RegisterForReflection
@SuppressWarnings({"unused", "CdiInjectionPointsInspection"})
public class RecycloneApplicationImpl implements RecycloneApplication {

  private final JsonSerializationTypeConfig jsonSerializationTypeConfig;

  private final MapperSubTypeConfig mapperSubTypeConfig;

  private final LaunchMode launchMode;

  private final RecycloneBuildConfig buildConfig;

  @Inject
  public RecycloneApplicationImpl(RecycloneBuildConfig buildConfig,
                                  LaunchMode launchMode,
                                  JsonSerializationTypeConfig jsonSerializationTypeConfig,
                                  MapperSubTypeConfig mapperSubTypeConfig) {
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
  public JsonSerializationTypeConfig getJsonSerializationTypeConfig() {
    return jsonSerializationTypeConfig;
  }

  @Override
  public MapperSubTypeConfig getMapperSubTypeConfig() {
    return mapperSubTypeConfig;
  }

  @Override
  public LaunchMode getLaunchMode() {
    return launchMode;
  }
}
