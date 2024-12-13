package com.dropchop.recyclone.base.api.model.marker.state;

import com.dropchop.recyclone.base.api.model.base.State;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 03. 22.
 */
@SuppressWarnings("unused")
public interface HasStateLog extends HasState {

  List<State> getStates();
  void setStates(List<State> states);

  default void setState(State state) {
    List<State> states = getStates();
    if (states == null) {
      states = new ArrayList<>();
      setStates(states);
    }
    if (state == null) {
      return;
    }
    //if (state.getCode().equals(HasCreated.Code.created.toString()))
    states.add(state);
  }

  default State getState() {
    List<State> states = getStates();
    if (states == null || states.isEmpty()) {
      return null;
    }
    return states.getLast();
  }

  default State getState(String code) {
    List<State> states = getStates();
    if (states == null || states.isEmpty()) {
      return null;
    }
    for (ListIterator<State> i = states.listIterator(states.size()); i.hasPrevious();) {
      State state = i.previous();
      if (state == null) {
        continue;
      }
      String stateCode = state.getCode();
      if ((stateCode == null && code == null) || code.equals(stateCode)) {
        return state;
      }
    }
    return null;
  }

  default State getLastNotHavingAfter(String last, String notHavingAfter) {
    List<State> states = getStates();
    if (states == null || states.isEmpty()) {
      return null;
    }
    for (ListIterator<State> i = states.listIterator(states.size()); i.hasPrevious();) {
      State state = i.previous();
      if (state == null) {
        continue;
      }
      String stateCode = state.getCode();
      if ((stateCode == null && notHavingAfter == null) || notHavingAfter.equals(stateCode)) {
        return null;
      }
      if ((stateCode == null && last == null) || last.equals(stateCode)) {
        return state;
      }
    }
    return null;
  }

  default State getFirst() {
    List<State> states = getStates();
    if (states == null || states.isEmpty()) {
      return null;
    }
    return states.getFirst();
  }

  default State getLast() {
    return getState();
  }

  default State getLast(String code) {
    return getState(code);
  }

  default State getCurrent() {
    return getState();
  }

  default void addState(State state) {
    setState(state);
  }
}
