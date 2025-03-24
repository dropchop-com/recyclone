package com.dropchop.recyclone.base.api.repo.config;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public interface DateBasedIndexConfig extends ElasticIndexConfig {

  default List<String> getMonthBasedIndexName(ZonedDateTime from, ZonedDateTime to, Class<?> root) {
    String entityName = getIndexName(root);
    List<String> monthBasedIndexName = new ArrayList<>();

    while(from.isBefore(to)) {
      int month = from.getMonthValue();
      monthBasedIndexName.add(entityName + "-" + from.getYear() + "-" + (month < 10 ? "0" + month : month)  + "-01");
      from = from.plusMonths(1);
    }

    return monthBasedIndexName;
  }

  default List<String> getMonthBasedIndexName(ZonedDateTime month, Class<?> root) {
    return getMonthBasedIndexName(month, month.plusMonths(1), root);
  }
}
