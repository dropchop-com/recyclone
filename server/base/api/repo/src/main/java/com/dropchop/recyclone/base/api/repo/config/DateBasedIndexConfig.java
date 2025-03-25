package com.dropchop.recyclone.base.api.repo.config;

import com.dropchop.recyclone.base.api.model.base.Entity;
import com.dropchop.recyclone.base.api.repo.mapper.QueryNodeObject;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class DateBasedIndexConfig extends DefaultIndexConfig implements HasEntityBasedWriteIndex, HasQueryBasedReadIndex {

  private String dateFormat;

  @Override
  public <E extends Entity> String getWriteIndex(E entity) {
    return "";
  }

  @Override
  public String getReadIndexName(QueryNodeObject query) {
    return "";
  }

  //private final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  /*
  public DateBasedIndexConfig(Class<?> rootClass, String ingestPipeline, Collection<String> aliases, String prefix) {
    super(rootClass, ingestPipeline, aliases, prefix);
  }

  public DateBasedIndexConfig(Class<?> rootClass, String ingestPipeline, String prefix) {
    super(rootClass, ingestPipeline, prefix);
  }

  public DateBasedIndexConfig(Class<?> rootClass, String ingestPipeline) {
    super(rootClass, ingestPipeline);
  }

  public DateBasedIndexConfig(Class<?> rootClass) {
    super(rootClass);
  }

  public DateBasedIndexConfig() {
  }
  */



  /*
  public List<String> getMonthBasedIndexName(ZonedDateTime from, ZonedDateTime to, Class<?> root) {
    String entityName = getIndexName(root);
    List<String> monthBasedIndexName = new ArrayList<>();

    while(from.isBefore(to)) {
      int month = from.getMonthValue();
      monthBasedIndexName.add(entityName + "-" + from.getYear() + "-" + (month < 10 ? "0" + month : month)  + "-01");
      from = from.plusMonths(1);
    }

    return monthBasedIndexName;
  }

  public List<String> getMonthBasedIndexName(ZonedDateTime month, Class<?> root) {
    return getMonthBasedIndexName(month, month.plusMonths(1), root);
  }
  */
}
