package com.dropchop.recyclone.model.api.marker.state;

import com.dropchop.recyclone.model.api.base.State;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 03. 22.
 */
public interface HasStateInlinedCommon extends HasStateInlined {

  static Set<? extends State.Code> allStateCodes(Class<?> clazz) {
    Set<State.Code> codes = new HashSet<>();
    if (HasCreated.class.isAssignableFrom(clazz)) {
      codes.add(HasCreated.created);
    }
    if (HasModified.class.isAssignableFrom(clazz)) {
      codes.add(HasModified.modified);
    }
    if (HasDeactivated.class.isAssignableFrom(clazz)) {
      codes.add(HasDeactivated.deactivated);
    }
    if (HasRemoved.class.isAssignableFrom(clazz)) {
      codes.add(HasRemoved.removed);
    }
    if (HasActivated.class.isAssignableFrom(clazz)) {
      codes.add(HasActivated.activated);
    }
    if (HasPublished.class.isAssignableFrom(clazz)) {
      codes.add(HasPublished.published);
    }
    if (HasDistributed.class.isAssignableFrom(clazz)) {
      codes.add(HasDistributed.distributed);
    }
    return codes;
  }

  default Set<? extends State.Code> allStateCodes() {
    return allStateCodes(this.getClass());
  }
}
