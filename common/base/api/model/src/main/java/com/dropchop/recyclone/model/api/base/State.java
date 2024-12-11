package com.dropchop.recyclone.model.api.base;

import com.dropchop.recyclone.model.api.marker.HasCode;
import com.dropchop.recyclone.model.api.marker.state.HasCreated;
import com.dropchop.recyclone.model.api.marker.state.HasCreatedBy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 01. 22.
 */
@SuppressWarnings("unused")
public interface State extends Model, HasCode, HasCreated, HasCreatedBy {

  interface Code extends Model, CharSequence {

    Map<String, Code> CODE_REGISTRY = new ConcurrentHashMap<>();

    static void register(Code code) {
      CODE_REGISTRY.put(code.toString(), code);
    }

    static Code get(String code) {
      return CODE_REGISTRY.get(code);
    }

    @Override
    default int length() {
      return this.getClass().getSimpleName().toLowerCase().length();
    }

    @Override
    default char charAt(int i) {
      return this.getClass().getSimpleName().toLowerCase().charAt(i);
    }

    @Override
    default CharSequence subSequence(int i, int i1) {
      return this.getClass().getSimpleName().toLowerCase().subSequence(i, i1);
    }

    default boolean equals(Code other) {
      return this.toString().equals(other.toString());
    }


    class BaseCode implements Code {

      private final String code = this.getClass().getSimpleName().toLowerCase();

      public BaseCode() {
        register(this);
      }

      public String toString() {
        return code;
      }

      @Override
      public int length() {
        return code.length();
      }

      @Override
      public char charAt(int i) {
        return code.charAt(i);
      }

      @Override
      public  CharSequence subSequence(int i, int i1) {
        return code.subSequence(i, i1);
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (!(o instanceof BaseCode baseCode)) {
          return false;
        }
        return code.equals(baseCode.code);
      }

      @Override
      public int hashCode() {
        return Objects.hash(code);
      }
    }

    class Created extends BaseCode {
    }

    class Modified extends BaseCode {
    }

    class Removed extends BaseCode {
    }

    class Deactivated extends BaseCode {
    }

    class Activated extends BaseCode {
    }

    class Distributed extends BaseCode {
    }

    class Published extends BaseCode {
    }

    Created created = new Created();
    Modified modified = new Modified();
    Removed removed = new Removed();
    Deactivated deactivated = new Deactivated();
    Activated activated = new Activated();
    Distributed distributed = new Distributed();
    Published published = new Published();
  }

  String getSubject();
  void setSubject();

  String getPreviousValue();
  void setPreviousValue();

  String getCurrentValue();
  void setCurrentValue();
}
