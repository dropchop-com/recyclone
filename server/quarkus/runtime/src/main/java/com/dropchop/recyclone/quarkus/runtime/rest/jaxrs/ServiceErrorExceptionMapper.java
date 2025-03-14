package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.base.api.model.attr.Attribute;
import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.invoke.StatusMessage;
import com.dropchop.recyclone.base.api.model.rest.ResultCode;
import com.dropchop.recyclone.base.dto.model.rest.Result;
import com.dropchop.recyclone.base.dto.model.rest.ResultStatus;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.dropchop.recyclone.base.api.model.invoke.ExecContext.MDC_REQUEST_ID;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
@SuppressWarnings("unused")
@ConstrainedTo(RuntimeType.SERVER)
public class ServiceErrorExceptionMapper implements ExceptionMapper<Exception> {

  private static final Logger log = LoggerFactory.getLogger(ServiceErrorExceptionMapper.class);

  private void setErrorCodeResponseStatus(Response.ResponseBuilder builder, StatusMessage statusMessage) {
    if (statusMessage != null && statusMessage.getCode() != null) {
      if (ErrorCode.internal_error == statusMessage.getCode()) {
        builder.status(Response.Status.INTERNAL_SERVER_ERROR);
      }
      if (ErrorCode.process_error == statusMessage.getCode()) {
        builder.status(Response.Status.INTERNAL_SERVER_ERROR);
      }
      if (ErrorCode.authentication_error == statusMessage.getCode()) {
        builder.status(Response.Status.UNAUTHORIZED);
      }
      if (ErrorCode.authorization_error == statusMessage.getCode()) {
        builder.status(Response.Status.UNAUTHORIZED);
      }
      if (ErrorCode.data_error == statusMessage.getCode()) {
        builder.status(Response.Status.BAD_REQUEST);
      }
      if (ErrorCode.not_found_error == statusMessage.getCode()) {
        builder.status(Response.Status.NOT_FOUND);
      }
      if (ErrorCode.data_missing_error == statusMessage.getCode()) {
        builder.status(Response.Status.BAD_REQUEST);
      }
      if (ErrorCode.data_validation_error == statusMessage.getCode()) {
        builder.status(Response.Status.BAD_REQUEST);
      }
      if (ErrorCode.parameter_validation_error == statusMessage.getCode()) {
        builder.status(Response.Status.BAD_REQUEST);
      }
      if (ErrorCode.quota_error == statusMessage.getCode()) {
        builder.status(Response.Status.SERVICE_UNAVAILABLE);
      }
    }
  }

  private void injectRequestId(StatusMessage statusMessage) {
    String requestId = MDC.get(MDC_REQUEST_ID);
    if (requestId == null || requestId.isBlank()) {
      return;
    }
    Set<Attribute<?>> attributes = statusMessage.getDetails();
    if (attributes == null) {
      attributes = new LinkedHashSet<>();
      statusMessage.setDetails(attributes);
    } else {
      //swap cause attributes could be immutable.
      attributes = new LinkedHashSet<>(attributes);
    }
    attributes.add(new AttributeString(MDC_REQUEST_ID, requestId));
  }

  public Result<?> toResult(ServiceException e) {
    ResultStatus status = new ResultStatus();
    List<StatusMessage> statusMessages = e.getStatusMessages();
    if (statusMessages.size() > 1) {
      status.setDetails(statusMessages);
    } else if (statusMessages.size() == 1) {
      status.setMessage(statusMessages.getFirst());
    }
    if (!statusMessages.isEmpty()) {
      StatusMessage statusMessage = statusMessages.getFirst();
      injectRequestId(statusMessage);
    }
    status.setCode(ResultCode.error);
    Result<?> result = new Result<>();
    result.setStatus(status);
    return result;
  }

  public Response toResponse(Exception e) {
    log.error(e.getMessage(), e);
    Response.ResponseBuilder builder = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
    Result<?> result;
    if (e instanceof ServiceException) {
      result = this.toResult((ServiceException) e);
      ResultStatus status = result.getStatus();
      StatusMessage statusMessage = status.getMessage();
      if (statusMessage == null) {
        log.warn("Got ServiceErrorException but without status message [{}]!", result);
      }

      if (status.getCode().equals(ResultCode.error)) {
        setErrorCodeResponseStatus(builder, statusMessage);
      }
    } else {
      ResultStatus status = new ResultStatus();
      StatusMessage statusMessage = new StatusMessage();
      if (e instanceof JsonMappingException) {
        statusMessage.setCode(ErrorCode.data_validation_error);
      } else {
        statusMessage.setCode(ErrorCode.internal_error);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        statusMessage.setDetails(Set.of(new AttributeString("trace", sw.toString())));
      }
      injectRequestId(statusMessage);
      statusMessage.setText(e.getMessage());
      setErrorCodeResponseStatus(builder, statusMessage);
      status.setCode(ResultCode.error);
      status.setMessage(statusMessage);
      result = new Result<>();
      result.setStatus(status);
    }

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      builder.entity(objectMapper.writeValueAsString(result));
    } catch (Exception ex) {
      log.warn("Unable to serialize [{}]!", result, ex);
    }

    return builder.build();
  }
}
