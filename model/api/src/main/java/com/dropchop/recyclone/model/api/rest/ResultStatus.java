package com.dropchop.recyclone.model.api.rest;

import com.dropchop.recyclone.model.api.invoke.StatusMessage;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 12. 08. 22.
 */
public interface ResultStatus<RS extends ResultStats> {

  ResultCode getCode();
  void setCode(ResultCode code);

  long getTime();
  void setTime(long time);

  long getTotal();
  void setTotal(long total);

  RS getStats();
  void setStats(RS stats);

  StatusMessage getMessage();
  void setMessage(StatusMessage message);

  List<StatusMessage> getDetails();
  void setDetails(List<StatusMessage> details);
}
