package com.dropchop.recyclone.model.api.invoke;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
public interface IdentifierParams extends CommonParams {
  List<String> getIdentifiers();
  void setIdentifiers(List<String> identifiers);
}
