package com.dropchop.recyclone.quarkus.runtime.cache;

import io.smallrye.common.constraint.NotNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Samo Pritržnik <samo.pritrznik@dropchop.com> on 17. 7. 25.
 */
public class CacheLoadingThreadFactory implements ThreadFactory {

  private final AtomicInteger threadNumber = new AtomicInteger(1);

  @Override
  public Thread newThread(Runnable r) {
    String namePrefix = "cache-loader-";
    Thread thread = new Thread(r, namePrefix + threadNumber.getAndIncrement());
    thread.setDaemon(false);
    thread.setPriority(Thread.NORM_PRIORITY);
    return thread;
  }

}
