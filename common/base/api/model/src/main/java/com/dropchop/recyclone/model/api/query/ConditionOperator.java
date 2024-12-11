package com.dropchop.recyclone.model.api.query;

import com.dropchop.recyclone.model.api.query.operator.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 19. 09. 24.
 */
@SuppressWarnings("unused")
public interface ConditionOperator {

  static Map<String, Class<? extends ConditionOperator>> supported() {
    return Map.of(
        "eq", Eq.class,
        "lt", Lt.class,
        "gt", Gt.class,
        "lte", Lte.class,
        "gte", Gte.class,
        "in", In.class,
        "gtelt", ClosedOpenInterval.class,
        "gtlt", OpenInterval.class,
        "gtelte", ClosedInterval.class,
        "gtlte", OpenClosedInterval.class
    );
  }

  static <T> Eq<T> eq(T value) {
    return new Eq<>(value);
  }

  static <T> Gt<T> gt(T value) {
    return new Gt<>(value);
  }

  static <T> Lt<T> lt(T value) {
    return new Lt<>(value);
  }

  static <T> Gte<T> gte(T value) {
    return new Gte<>(value);
  }

  static <T> Lte<T> lte(T value) {
    return new Lte<>(value);
  }

  static <T> ClosedOpenInterval<T> gteLt(T value1, T value2) {
    return new ClosedOpenInterval<>(value1, value2);
  }

  static <T> ClosedInterval<T> gteLte(T value1, T value2) {
    return new ClosedInterval<>(value1, value2);
  }

  static <T> OpenClosedInterval<T> gtLte(T value1, T value2) {
    return new OpenClosedInterval<>(value1, value2);
  }

  static <T> OpenInterval<T> gtLt(T value1, T value2) {
    return new OpenInterval<>(value1, value2);
  }

  static <T> In<T> in(Collection<T> values) {
    return new In<>(values);
  }

  @SafeVarargs
  static <T> In<T> in(T... values) {
    return new In<>(new ArrayList<>(Arrays.asList(values)));
  }
}
