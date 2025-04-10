package com.dropchop.recyclone.base.es.repo.config;

import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.model.marker.HasUuidV1;
import com.dropchop.recyclone.base.api.model.marker.state.HasCreated;
import com.dropchop.recyclone.base.api.model.marker.state.HasModified;
import com.dropchop.recyclone.base.api.model.marker.state.HasPublished;
import com.dropchop.recyclone.base.api.model.utils.Uuid;
import com.dropchop.recyclone.base.es.model.query.QueryNodeObject;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@SuperBuilder
public class DateBasedIndexConfig extends IngestPipelineIndexConfig
    implements HasEntityBasedWriteIndex, HasQueryBasedReadIndex {

  public interface PostfixGenerator extends Function<ZonedDateTime, String> {}

  public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("-yyyy-MM-01");

  private PostfixGenerator indexPostfix;

  protected <E extends Entity> ZonedDateTime getDateTimeFromEntity(E entity) {
    Class<? extends Entity> entityClass = entity.getClass();
    ZonedDateTime dateTime;
    if (HasCreated.class.isAssignableFrom(entityClass)) {
      dateTime = ((HasCreated)entity).getCreated();
    } else if (HasModified.class.isAssignableFrom(entityClass)) {
      dateTime = ((HasModified)entity).getModified();
    } else if (HasPublished.class.isAssignableFrom(entityClass)) {
      dateTime = ((HasPublished)entity).getPublished();
    } else if (HasUuidV1.class.isAssignableFrom(entityClass)) {
      UUID uuid = ((HasUuidV1)entity).getUuid();
      dateTime = Uuid.toInstant(uuid).atZone(ZoneId.systemDefault());
    } else {
      dateTime = null;
    }
    if (dateTime == null) {
      log.warn(
          "No suitable marker interface [{}] found for entity [{}] in index config [{}]!",
          List.of(
              HasCreated.class.getSimpleName(),
              HasModified.class.getSimpleName(),
              HasPublished.class.getSimpleName(),
              HasUuidV1.class.getSimpleName()
          ),
          entityClass,
          this.getClass()
      );
      return null;
    }
    return dateTime;
  }

  @Override
  public <E extends Entity> String getWriteIndex(E entity) {
    if (entity == null) {
      return null;
    }
    if (indexPostfix == null) {
      log.warn(
          "No index postfix generator configured for entity [{}] in index config [{}]!",
          entity.getClass(), this.getClass()
      );
      return null;
    }

    String indexPrefix = this.getDefaultIndexName();
    ZonedDateTime dateTime = this.getDateTimeFromEntity(entity);
    if (dateTime == null) { // null value warn ignore intentional
      return this.getRootAlias();
    }

    return indexPrefix + this.indexPostfix.apply(dateTime);
  }

  @SuppressWarnings("unused")
  protected ZonedDateTime getDateTimeFromQuery(QueryNodeObject query) {
    UUID uuid = query.getNestedValue(UUID.class, "uuid");
    if (uuid != null) {
      return Uuid.toInstant(uuid).atZone(ZoneId.systemDefault());
    }
    ZonedDateTime dateTime = query.getNestedValue(ZonedDateTime.class, "created");
    if (dateTime != null) {
      return dateTime;
    }
    dateTime = query.getNestedValue(ZonedDateTime.class, "modified");
    if (dateTime != null) {
      return dateTime;
    }
    dateTime = query.getNestedValue(ZonedDateTime.class, "published");
    return dateTime;
  }

  @Override
  public String getReadIndexName(QueryNodeObject query) {
    if (indexPostfix == null) {
      log.warn(
          "No index name generator configured for search in index config [{}]!",
          this.getClass()
      );
      return null;
    }
    return getRootAlias();

    //TODO: support intervals ...
    /*ZonedDateTime dateTime = this.getDateTimeFromQuery(query);
    if (dateTime == null) { // null value warn ignore intentional
      return getRootAlias();
    }

    String indexPrefix = this.getDefaultIndexName();
    return indexPrefix + this.indexPostfix.apply(dateTime);*/
  }
}
