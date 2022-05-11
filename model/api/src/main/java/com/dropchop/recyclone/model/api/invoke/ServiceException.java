package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.attr.Attribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 12. 21.
 */
public class ServiceException extends RuntimeException {

  private List<StatusMessage> statusMessages = new ArrayList<>();
  private boolean fromWrappedCall = false;

  public ServiceException(StatusMessage statusMessage) {
    this.statusMessages.add(statusMessage);
  }

  public ServiceException(ErrorCode code, String message, Set<Attribute<?>> details) {
    this(new StatusMessage(code, message, details));
  }

  public ServiceException(ErrorCode code, String message) {
    this(new StatusMessage(code, message, null));
  }

  public ServiceException(List<StatusMessage> statusMessages) {
    if (statusMessages == null) {
      throw new NullPointerException("Missing messages argument");
    }
    this.statusMessages = statusMessages;
  }

  public boolean isFromWrappedCall() {
    return fromWrappedCall;
  }

  public void setFromWrappedCall(boolean fromWrappedCall) {
    this.fromWrappedCall = fromWrappedCall;
  }

  public List<StatusMessage> getStatusMessages() {
    return statusMessages;
  }

  public void setStatusMessages(List<StatusMessage> statusMessages) {
    this.statusMessages = statusMessages;
  }
}
