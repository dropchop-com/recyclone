package com.dropchop.recyclone.model.api.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 22.
 */
public interface ParamsExecContext<L extends ExecContext.Listener> extends ExecContext<L> {
  <P extends Params> P getParams();
  <P extends Params> void setParams(P params);

  default <C extends ExecContext<? extends Listener>> C copyAs(C targetContext) {
    C context = ExecContext.super.copyAs(targetContext);
    if (context instanceof ParamsExecContext) {
      Params params = getParams();
      if (params != null) {
        //noinspection unchecked
        ((ParamsExecContext<L>) context).setParams(params);
      }
    }
    return context;
  }

  default ParamsExecContext<L> of(ExecContext<?> sourceContext) {
    ExecContext.super.of(sourceContext);
    if (sourceContext instanceof ParamsExecContext) {
      @SuppressWarnings("unchecked")
      Params params = ((ParamsExecContext<L>) sourceContext).getParams();
      if (params != null) {
        this.setParams(params);
      }
    }
    return this;
  }

  @Override
  default ParamsExecContext<L> listener(L listener) {
    ExecContext.super.listener(listener);
    return this;
  }
}
