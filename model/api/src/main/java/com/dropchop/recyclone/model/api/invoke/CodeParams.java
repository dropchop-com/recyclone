package com.dropchop.recyclone.model.api.invoke;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 01. 22.
 */
public interface CodeParams extends CommonParams {
  List<String> getCodes();
  void setCodes(List<String> identifiers);
}
