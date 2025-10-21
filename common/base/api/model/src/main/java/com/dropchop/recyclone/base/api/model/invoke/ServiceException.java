package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
@Setter
@Getter
@SuppressWarnings("unused")
public class ServiceException extends RuntimeException {

  private List<StatusMessage> statusMessages = new ArrayList<>();

  private static String constructMessage(StatusMessage message, Throwable cause) {
    String prefix = ServiceException.class.getSimpleName() + ": ";

    if (message.getText() != null) {
      return prefix + message.getText();
    } else if (cause != null) {
      return prefix + cause.getMessage();
    }

    return prefix + "Unknown error";
  }

  public static Optional<ErrorCode> getLastErrorCode(ServiceException e) {
    List<StatusMessage> messages = e.getStatusMessages();
    if (messages.isEmpty()) {
      return Optional.empty();
    }
    return messages.stream().map(StatusMessage::getCode).findFirst();
  }

  public ServiceException(StatusMessage statusMessage) {
    super(constructMessage(statusMessage, null));
    this.statusMessages.add(statusMessage);
  }

  public ServiceException(StatusMessage statusMessage, Throwable cause) {
    super(constructMessage(statusMessage, cause), cause);

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

  public Optional<ErrorCode> getLastErrorCode() {
    return ServiceException.getLastErrorCode(this);
  }
}
