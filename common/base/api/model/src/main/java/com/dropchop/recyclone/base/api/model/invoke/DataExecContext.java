package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.base.Dto;
import com.dropchop.recyclone.base.api.model.invoke.ExecContext.Listener;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 20. 03. 22.
 */
public interface DataExecContext<D extends Dto, ECL extends Listener> extends ExecContext<ECL> {
  List<D> getData();
  void setData(List<D> data);
}
