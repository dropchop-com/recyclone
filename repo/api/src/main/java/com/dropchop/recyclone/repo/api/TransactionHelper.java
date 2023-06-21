package com.dropchop.recyclone.repo.api;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.function.Function;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 8. 06. 23.
 */
@ApplicationScoped
@SuppressWarnings("unused")
public class TransactionHelper {

  @Transactional
  @SuppressWarnings("UnusedReturnValue")
  public <T, R> R transact(Function<T, R> function, T t) {
    return function.apply(t);
  }


  @Transactional
  @SuppressWarnings("UnusedReturnValue")
  public void transact(Runnable runnable) {
    runnable.run();
  }
}
