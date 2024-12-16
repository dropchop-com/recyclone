package com.dropchop.recyclone.quarkus.it.model.dto.invoke;

import com.dropchop.recyclone.base.dto.model.invoke.QueryParams;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 7. 10. 24.
 */
public class DummyQueryParams extends QueryParams {

  @Override
  public List<String> getAvailableFields() {
    return List.of(
        "title",
        "description",
        "lang",
        "translations.title",
        "translations.description",
        "created",
        "modified",
        "deactivated"
    );
  }
}
