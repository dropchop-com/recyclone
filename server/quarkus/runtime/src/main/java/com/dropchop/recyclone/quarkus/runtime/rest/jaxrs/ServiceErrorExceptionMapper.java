package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.base.api.jaxrs.concurrency.OptimisticConcurrency;
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
import io.quarkus.security.AuthenticationCompletionException;
import io.quarkus.security.ForbiddenException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Variant;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;

import static com.dropchop.recyclone.base.api.model.invoke.ExecContext.MDC_REQUEST_ID;
import static com.dropchop.recyclone.base.api.model.rest.MediaType.APPLICATION_JSON_DROPCHOP_RESULT;
import static jakarta.ws.rs.core.HttpHeaders.ACCEPT;
import static jakarta.ws.rs.core.HttpHeaders.RETRY_AFTER;
import static jakarta.ws.rs.core.HttpHeaders.VARY;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.SERVICE_UNAVAILABLE;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 12. 21.
 */
@SuppressWarnings("unused")
@ConstrainedTo(RuntimeType.SERVER)
public class ServiceErrorExceptionMapper implements ExceptionMapper<Exception> {

  private static final Logger log = LoggerFactory.getLogger(ServiceErrorExceptionMapper.class);

  private static final MediaType DROPCHOP_TYPE = MediaType.valueOf(APPLICATION_JSON_DROPCHOP_RESULT);
  private static final List<Variant> JSON_VARIANTS =
      Variant.mediaTypes(APPLICATION_JSON_TYPE, DROPCHOP_TYPE).add().build();

  @ConfigProperty(name = "quarkus.recyclone.rest.negotiate-service-errors", defaultValue = "false")
  boolean negotiateServiceErrors;

  @Context
  Request request;

  private int responseStatus(StatusMessage message) {
    if (message == null || message.getCode() == null) {
      return INTERNAL_SERVER_ERROR.getStatusCode();
    }
    ErrorCode code = message.getCode();
    OptionalInt preconditionStatus = OptimisticConcurrency.preconditionStatus(code);
    if (preconditionStatus.isPresent()) {
      return preconditionStatus.getAsInt();
    }
    Response.Status status = switch (code) {
      case authentication_error, authorization_error -> UNAUTHORIZED;
      case insufficient_permissions_error -> FORBIDDEN;
      case data_error, data_missing_error, data_validation_error, parameter_validation_error -> BAD_REQUEST;
      case not_found_error -> NOT_FOUND;
      case quota_error, service_unavailable_error -> SERVICE_UNAVAILABLE;
      default -> INTERNAL_SERVER_ERROR;
    };
    return status.getStatusCode();
  }

  private Response negotiatedResponse(Response.ResponseBuilder builder, int status, StatusMessage message,
                                      Result<?> result) {
    builder.header(VARY, ACCEPT);
    Variant variant = request.selectVariant(JSON_VARIANTS);
    if (variant != null && DROPCHOP_TYPE.equals(variant.getMediaType())) {
      result.getStatus().setMessage(message == null || message.getCode() == null
          ? new StatusMessage(ErrorCode.internal_error, "Internal server error") : message);
      builder.type(DROPCHOP_TYPE).entity(result);
    } else if (status == BAD_REQUEST.getStatusCode()) {
      String text = message.getText() == null ? "Invalid request" : message.getText();
      builder.type(APPLICATION_JSON_TYPE).entity(Map.of("error", text));
    }
    return builder.build();
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
      statusMessage.setDetails(attributes);
    }
    attributes.add(new AttributeString(MDC_REQUEST_ID, requestId));
  }

  public Result<?> toResult(ServiceException e) {
    ResultStatus status = new ResultStatus();
    List<StatusMessage> statusMessages = e.getStatusMessages();
    if (statusMessages == null) {
      statusMessages = List.of();
    }
    if (statusMessages.size() > 1) {
      status.setDetails(statusMessages);
    } else if (statusMessages.size() == 1) {
      status.setMessage(statusMessages.getFirst());
    }
    if (!statusMessages.isEmpty() && statusMessages.getFirst() != null) {
      StatusMessage statusMessage = statusMessages.getFirst();
      injectRequestId(statusMessage);
    }
    status.setCode(ResultCode.error);
    Result<?> result = new Result<>();
    result.setStatus(status);
    return result;
  }

  @Override
  public Response toResponse(Exception e) {
    log.error(e.getMessage(), e);
    Response.ResponseBuilder builder = Response.status(INTERNAL_SERVER_ERROR);
    Result<?> result;
    if (e instanceof ServiceException failure) {
      result = toResult(failure);
      ResultStatus status = result.getStatus();
      StatusMessage statusMessage = status.getMessage();
      if (statusMessage == null && status.getDetails() != null && !status.getDetails().isEmpty()) {
        statusMessage = status.getDetails().getFirst();
      }
      if (statusMessage == null) {
        log.warn("Got ServiceErrorException but without status message [{}]!", result);
      }

      int httpStatus = responseStatus(statusMessage);
      builder.status(httpStatus);
      if (failure.getRetryAfter() != null) {
        builder.header(RETRY_AFTER, failure.getRetryAfter());
      }
      if (negotiateServiceErrors) {
        return negotiatedResponse(builder, httpStatus, statusMessage, result);
      }
    } else {
      ResultStatus status = new ResultStatus();
      StatusMessage statusMessage = new StatusMessage();
      //noinspection IfCanBeSwitch
      if (e instanceof JsonMappingException) {
        statusMessage.setCode(ErrorCode.data_validation_error);
      } else if (e instanceof BadRequestException) {
        statusMessage.setCode(ErrorCode.data_validation_error);
      } else if (e instanceof AuthenticationCompletionException) {
        statusMessage.setCode(ErrorCode.authentication_error);
      } else if (e instanceof AuthenticationException) {
        statusMessage.setCode(ErrorCode.authentication_error);
      } else if (e instanceof ForbiddenException) {
        statusMessage.setCode(ErrorCode.insufficient_permissions_error);
      } else if (e instanceof AuthorizationException) {
        statusMessage.setCode(ErrorCode.authorization_error);
      } else {
        statusMessage.setCode(ErrorCode.internal_error);
      }
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      statusMessage.setDetails(Set.of(new AttributeString("trace", sw.toString())));

      injectRequestId(statusMessage);
      statusMessage.setText(e.getMessage());
      builder.status(responseStatus(statusMessage));
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
