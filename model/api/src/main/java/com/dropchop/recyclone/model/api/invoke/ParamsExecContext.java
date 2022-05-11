package com.dropchop.recyclone.model.api.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 22.
 */
public interface ParamsExecContext<P extends Params, L extends ExecContext.Listener> extends ExecContext<L> {
  P getParams();
  void setParams(P params);

  default <C extends ExecContext<? extends Listener>> C copyAs(C targetContext) {
    C context = ExecContext.super.copyAs(targetContext);
    if (context instanceof ParamsExecContext) {
      P params = getParams();
      if (params != null) {
        //noinspection unchecked
        ((ParamsExecContext<P, L>) context).setParams(params);
      }
    }
    return context;
  }

  default ParamsExecContext<P, L> of(ExecContext<?> sourceContext) {
    ExecContext.super.of(sourceContext);
    if (sourceContext instanceof ParamsExecContext) {
      @SuppressWarnings("unchecked")
      P params = ((ParamsExecContext<P, L>) sourceContext).getParams();
      if (params != null) {
        this.setParams(params);
      }
    }
    return this;
  }
}
