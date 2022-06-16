package com.dropchop.recyclone.model.api.invoke;

import com.dropchop.recyclone.model.api.attr.Attribute;
import com.dropchop.recyclone.model.api.attr.AttributeString;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 20. 12. 21.
 */
public class ServiceException extends RuntimeException {

  private List<StatusMessage> statusMessages = new ArrayList<>();

  public ServiceException(StatusMessage statusMessage) {
    this.statusMessages.add(statusMessage);
  }

  public ServiceException(StatusMessage statusMessage, Throwable cause) {
    super(cause);
    Set<Attribute<?>> details = statusMessage.getDetails();
    Set<Attribute<?>> copy = new LinkedHashSet<>();
    if (details == null) {
      statusMessage.setDetails(copy);
    } else {
      copy.addAll(details);
      statusMessage.setDetails(copy);
    }
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    cause.printStackTrace(pw);
    copy.add(new AttributeString("trace", sw.toString()));
    this.statusMessages.add(statusMessage);
  }

  public ServiceException(ErrorCode code, String message, Set<Attribute<?>> details) {
    this(new StatusMessage(code, message, details));
  }

  public ServiceException(ErrorCode code, String message, Set<Attribute<?>> details, Throwable cause) {
    this(new StatusMessage(code, message, details), cause);
  }

  public ServiceException(ErrorCode code, String message) {
    this(new StatusMessage(code, message, null));
  }

  public ServiceException(ErrorCode code, String message, Throwable cause) {
    this(new StatusMessage(code, message, null), cause);
  }

  public ServiceException(List<StatusMessage> statusMessages) {
    if (statusMessages == null) {
      throw new NullPointerException("Missing messages argument");
    }
    this.statusMessages = statusMessages;
  }

  public List<StatusMessage> getStatusMessages() {
    return statusMessages;
  }

  public void setStatusMessages(List<StatusMessage> statusMessages) {
    this.statusMessages = statusMessages;
  }
}
