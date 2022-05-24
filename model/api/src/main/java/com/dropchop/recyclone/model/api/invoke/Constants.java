package com.dropchop.recyclone.model.api.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 10. 03. 22.
 */
public interface Constants {

  interface InternalContextVariables {
    String RECYCLONE_PARAMS = "<<RECYCLONE_PARAMS>>";
    String RECYCLONE_DATA = "<<RECYCLONE_DATA>>";
    String RECYCLONE_SECURITY_SUBJECT = "<<RECYCLONE_SECURITY_SUBJECT>>";
    String RECYCLONE_SECURITY_DOMAIN = "<<RECYCLONE_SECURITY_DOMAIN>>";
    String RECYCLONE_SECURITY_ACTION = "<<RECYCLONE_SECURITY_ACTION>>";
    String RECYCLONE_SECURITY_REQUIRED_PERM = "<<RECYCLONE_SECURITY_REQUIRED_PERM>>";
    String RECYCLONE_SECURITY_REQUIRED_PERM_OP= "<<RECYCLONE_SECURITY_REQUIRED_PERM_OP>>";
  }
}
