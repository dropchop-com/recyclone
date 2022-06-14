package com.dropchop.recyclone.rest.jaxrs.server.openapi;

import com.dropchop.recyclone.model.api.rest.Constants.Tags;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.openapi.models.Operation;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 21. 01. 22.
 */
@Slf4j
public class ExternalOpsOnlyUiFilter extends CommonUiFilter {

  @Override
  public Operation filterOperation(Operation operation) {
    List<String> tags = operation.getTags();
    if (tags == null) {
      return null;
    }
    for (String tag : tags) {
      if (tag == null || tag.isBlank()) {
        return super.filterOperation(operation);
      }
      if (Tags.DynamicContext.PUBLIC.equalsIgnoreCase(tag)) {
        return super.filterOperation(operation);
      }
    }
    return null;
  }
}
