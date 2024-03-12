package com.dropchop.recyclone.extension.quarkus.swagger;

import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.jboss.logging.Logger;

import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 24.
 */
@SuppressWarnings("SameParameterValue")
public class TestFilter implements OASFilter {

  private static final Logger log = Logger.getLogger(TestFilter.class);

  final Map<String, String> map;

  public TestFilter(Map<String, String> neki) {
    map = neki;
  }

  @Override
  public void filterOpenAPI(OpenAPI openAPI) {
    openAPI.getInfo().setTitle(openAPI.getInfo().getTitle() + " %s".formatted(map.get("title")));
  }
}
