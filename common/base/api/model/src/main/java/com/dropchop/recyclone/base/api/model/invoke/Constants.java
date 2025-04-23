package com.dropchop.recyclone.base.api.model.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 10. 03. 22.
 */
public interface Constants {

  interface InternalContextVariables {
    String RECYCLONE_EXEC_CONTEXT_PROVIDER = "<<RECYCLONE_EXEC_CONTEXT_PROVIDER>>";
    String RECYCLONE_PARAMS = "<<RECYCLONE_PARAMS>>";
  }

  interface ModifyPolicy {
    String WAIT_FOR = "wait";
    String RELOAD_AFTER = "reload_after";
  }
}
