package com.dropchop.recyclone.model.api.utils;

public class ProfileTimer {
  private long absoluteStart = 0;
  private long start = 0;

  public ProfileTimer(boolean start) {
    if (start) {
      this.start();
    }
  }

  public ProfileTimer() {
    this(true);
  }

  public void start() {
    if (absoluteStart == 0) {
      this.absoluteStart = System.currentTimeMillis();
    }
    this.start = System.currentTimeMillis();
  }

  public long mark() {
    long mark = System.currentTimeMillis() - this.start;
    this.start = System.currentTimeMillis();
    return mark;
  }

  public long stop() {
    return System.currentTimeMillis() - this.absoluteStart;
  }
}
