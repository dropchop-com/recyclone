package com.dropchop.recyclone.base.api.model.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 18. 12. 21.
 */
public enum ErrorCode {
  not_found_error,
  data_error,
  parameter_validation_error,
  data_validation_error,
  data_missing_error,
  authentication_error,
  authorization_error,
  process_error,
  quota_error,
  internal_error,
  unknown_error
}
