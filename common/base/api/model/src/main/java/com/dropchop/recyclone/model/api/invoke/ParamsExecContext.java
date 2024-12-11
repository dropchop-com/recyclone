package com.dropchop.recyclone.model.api.invoke;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 03. 22.
 */
public interface ParamsExecContext<ECL extends ExecContext.Listener> extends ExecContext<ECL> {
  <P extends Params> P getParams();
  <P extends Params> void setParams(P params);

  default <EC extends ExecContext<? extends Listener>> EC copyAs(EC targetContext) {
    EC context = ExecContext.super.copyAs(targetContext);
    if (context instanceof ParamsExecContext) {
      Params params = getParams();
      if (params != null) {
        //noinspection unchecked
        ((ParamsExecContext<ECL>) context).setParams(params);
      }
    }
    return context;
  }

  default ParamsExecContext<ECL> of(ExecContext<?> sourceContext) {
    ExecContext.super.of(sourceContext);
    if (sourceContext instanceof ParamsExecContext) {
      @SuppressWarnings("unchecked")
      Params params = ((ParamsExecContext<ECL>) sourceContext).getParams();
      if (params != null) {
        this.setParams(params);
      }
    }
    return this;
  }

  @Override
  default ParamsExecContext<ECL> listener(ECL listener) {
    ExecContext.super.listener(listener);
    return this;
  }
}
