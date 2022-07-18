package com.dropchop.recyclone.model.api.invoke;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
public interface TypeParams extends CommonParams {
  List<String> getTypes();
  void setTypes(List<String> types);
}
