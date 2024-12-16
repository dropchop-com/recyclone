package com.dropchop.recyclone.quarkus.runtime.app;

import com.dropchop.recyclone.base.api.model.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.base.api.model.filtering.MapperSubTypeConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 15. 03. 24.
 */
@ApplicationScoped
@RegisterForReflection
@SuppressWarnings({"unused"})
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
