package com.dropchop.recyclone.base.api.model.rest;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.marker.HasId;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 12. 08. 22.
 */
public interface Result<T, R extends ResultStatus<RS>, RS extends ResultStats> extends Model, HasId {

  R getStatus();
  void setStatus(R status);

  List<T> getData();
  void setData(List<T> data);
}
