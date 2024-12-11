package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.base.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 03. 22.
 */
public interface HasState {

  default Map<String, ? extends State.Code> allStateCodesMap() {
    Map<String, State.Code> codeNames = new HashMap<>();
    for (State.Code code : allStateCodes()) {
      codeNames.put(code.toString(), code);
    }
    return codeNames;
  }

  Set<? extends State.Code> allStateCodes();
}
