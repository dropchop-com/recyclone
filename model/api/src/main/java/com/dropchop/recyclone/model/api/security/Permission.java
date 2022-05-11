package com.dropchop.recyclone.model.api.security;

import com.dropchop.recyclone.model.api.localization.TitleTranslation;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 01. 22.
 */
public interface Permission<T extends TitleTranslation, A extends Action<T>, D extends Domain<T, A>> {
  String CODE_ALL = "*";

  D getDomain();
  void setDomain(D domain);

  A getAction();
  void setAction(A action);

  List<String> getInstances();
  void setInstances(List<String> instances);

  /*
  static String toCode(Permission<?, ?, ?> permission) {
    StringBuilder builder = new StringBuilder();
    SortedSet<Domain<?, ?, ?>> domains = permission.getDomains();
    if (domain != null) {
      String tmp = domain.getCode();
      if (tmp != null && tmp.equals(Domain))
    }
  }
  */
}
