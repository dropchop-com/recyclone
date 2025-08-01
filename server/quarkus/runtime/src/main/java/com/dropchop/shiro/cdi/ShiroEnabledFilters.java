package com.dropchop.shiro.cdi;

import com.dropchop.shiro.filter.ShiroFilter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 01. 08. 2025
 */
public class ShiroEnabledFilters extends ArrayList<Class<? extends ShiroFilter>> {
  @SafeVarargs
  public static ShiroEnabledFilters of(Class<? extends ShiroFilter> ... filters) {
    ShiroEnabledFilters result = new ShiroEnabledFilters();
    result.addAll(Arrays.asList(filters));
    return result;
  }

  public ShiroEnabledFilters append(Class<? extends ShiroFilter> aClass) {
    super.add(aClass);
    return this;
  }
}
