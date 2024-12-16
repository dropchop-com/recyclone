package com.dropchop.recyclone.base.api.repo.utils;

public class SearchFields {

  public enum Common {
    UUID("uuid"),
    CREATED("created"),
    UPDATED("updated"),
    COUNTRY("country"),
    LANGUAGE("language");


    private final String title;


    Common(final String title) {
      this.title = title;
    }


    @Override
    public String toString() {
      return this.title;
    }
  }


  public enum User {
    LOGIN_NAME("loginName"),
    TOKEN("token"),
    ;

    private final String title;


    User(final String title) {
      this.title = title;
    }


    @Override
    public String toString() {
      return this.title;
    }
  }
}

