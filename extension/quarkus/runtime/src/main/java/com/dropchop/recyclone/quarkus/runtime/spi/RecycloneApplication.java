package com.dropchop.recyclone.quarkus.runtime.spi;

import com.dropchop.recyclone.model.api.filtering.JsonSerializationTypeConfig;
import com.dropchop.recyclone.model.api.filtering.MapperSubTypeConfig;
import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 15. 03. 24.
 */
@Singleton
@RegisterForReflection
public class RecycloneApplication {

  private final RestResourceConfig restResourceConfig;

  private final JsonSerializationTypeConfig jsonSerializationTypeConfig;

  private final MapperSubTypeConfig mapperSubTypeConfig;

  private final LaunchMode launchMode;


  @Inject
  RecycloneConfig appConfig;


  public RecycloneApplication(RestResourceConfig restResourceConfig,
                              JsonSerializationTypeConfig jsonSerializationTypeConfig,
                              MapperSubTypeConfig mapperSubTypeConfig, LaunchMode launchMode) {
    this.restResourceConfig = restResourceConfig;
    this.jsonSerializationTypeConfig = jsonSerializationTypeConfig;
    this.mapperSubTypeConfig = mapperSubTypeConfig;
    this.launchMode = launchMode;
  }

  public RecycloneConfig getAppConfig() {
    return appConfig;
  }

  public RestResourceConfig getRestResourceConfig() {
    return restResourceConfig;
  }

  public JsonSerializationTypeConfig getJsonSerializationTypeConfig() {
    return jsonSerializationTypeConfig;
  }

  public MapperSubTypeConfig getMapperSubTypeConfig() {
    return mapperSubTypeConfig;
  }

  public LaunchMode getLaunchMode() {
    return launchMode;
  }
}
