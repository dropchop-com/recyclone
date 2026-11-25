package com.dropchop.recyclone.base.api.jaxrs.concurrency;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.marker.HasOptimisticConcurrency;
import jakarta.ws.rs.core.Response;

import java.util.OptionalInt;
import java.util.regex.Pattern;

public final class OptimisticConcurrency {

  private static final Pattern STRONG_TAG = Pattern.compile("\"[\\x21\\x23-\\x7e\\x80-\\xff]*\"");

  private OptimisticConcurrency() {
  }

  /**
   * Binds a single strong entity tag to the write parameters.
   */
  public static void bindIfMatch(HasOptimisticConcurrency params, String ifMatch) {
    if (ifMatch == null) {
      params.setExpectedRevision(null);
      return;
    }

    String tag = ifMatch.trim();
    if (!STRONG_TAG.matcher(tag).matches()) {
      throw new ServiceException(ErrorCode.precondition_validation_error,
          "If-Match must contain one strong entity tag");
    }
    params.setExpectedRevision(tag.substring(1, tag.length() - 1));
  }

  public static String requireExpectedRevision(HasOptimisticConcurrency params) {
    String revision = params.getExpectedRevision();
    if (revision == null) {
      throw new ServiceException(ErrorCode.precondition_required_error, "If-Match is required");
    }
    return revision;
  }

  public static OptionalInt preconditionStatus(ErrorCode code) {
    return switch (code) {
      case precondition_required_error -> OptionalInt.of(428);
      case precondition_validation_error -> OptionalInt.of(Response.Status.BAD_REQUEST.getStatusCode());
      case precondition_failed_error -> OptionalInt.of(Response.Status.PRECONDITION_FAILED.getStatusCode());
      default -> OptionalInt.empty();
    };
  }
}
