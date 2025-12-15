package com.dropchop.recyclone.base.api.model.invoke;

import com.dropchop.recyclone.base.api.model.base.Model;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter.ContentFilter;
import com.dropchop.recyclone.base.api.model.marker.HasId;

import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 03. 22.
 */
@SuppressWarnings("unused")
public interface ExecContext<ECL extends ExecContext.Listener> extends Model, HasId {

  String MDC_REQUEST_ID = "reqId";
  String MDC_SHORT_REQUEST_ID = "shortReqId";
  String MDC_REQUEST_PATH = "reqPath";
  String MDC_SHORT_REQUEST_PATH = "shortReqPath";
  String MDC_PERSON_ID = "pId";
  String MDC_SHORT_PERSON_ID = "shortPId";
  String MDC_PERSON_NAME = "pName";

  interface ReqAttributeNames {
    String REQ_CLIENT_HOST    = "req.client.host";
    String REQ_CLIENT_ADDRESS = "req.client.address";
    String REQ_METHOD         = "req.method";
    String REQ_PATH           = "req.path";
    String REQ_URI            = "req.uri";
    String REQ_URI_HOST       = "req.uri.host";
    String REQ_URI_QUERY      = "req.uri.query";
    String REQ_URI_FRAGMENT   = "req.uri.fragment";
    String REQ_URI_SCHEME     = "req.uri.scheme";
  }

  interface Listener {
  }

  Long getStartTime();
  void setStartTime(Long startTime);

  default Long getExecTime() {
    Long start = getStartTime();
    if (start == null) {
      return 0L;
    }
    return System.currentTimeMillis() - start;
  }

  List<ECL> getListeners();
  void setListeners(List<ECL> listeners);

  default ExecContext<ECL> listener(ECL listener) {
    if (listener == null) {
      return this;
    }
    this.getListeners().add(listener);
    return this;
  }

  default <EC extends ExecContext<? extends Listener>> EC copyAs(EC context) {
    String id = getId();
    if (id != null) {
      context.setId(id);
    }
    Long startTime = getStartTime();
    if (startTime != null) {
      context.setStartTime(startTime);
    }
    return context;
  }

  default ExecContext<ECL> of(ExecContext<?> sourceContext) {
    if (sourceContext == null) {
      return this;
    }
    String id = sourceContext.getId();
    if (id != null) {
      this.setId(id);
    }
    Long startTime = sourceContext.getStartTime();
    if (startTime != null) {
      this.setStartTime(startTime);
    }
    return this;
  }

  default Params tryGetParams() {
    if (this instanceof ParamsExecContext<?> paramsExecContext) {
      return paramsExecContext.getParams();
    }
    return null;
  }

  default ResultFilter<?, ?> tryGetResultFilter() {
    Params params = tryGetParams();
    if (params instanceof CommonParams<?,?,?,?> commonParams) {
      return commonParams.getFilter();
    }
    return null;
  }

  default ContentFilter tryGetResultContentFilter() {
    ResultFilter<?, ?> resultFilter = tryGetResultFilter();
    if (resultFilter != null) {
      return resultFilter.getContent();
    }
    return null;
  }

  default ResultFilterDefaults tryGetResultFilterDefaults() {
    Params params = tryGetParams();
    if (params instanceof CommonParams<?,?,?,?> commonParams) {
      return commonParams.getFilterDefaults();
    }
    return null;
  }
}
