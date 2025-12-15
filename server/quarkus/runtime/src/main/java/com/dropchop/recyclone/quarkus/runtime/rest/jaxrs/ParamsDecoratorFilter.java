package com.dropchop.recyclone.quarkus.runtime.rest.jaxrs;

import com.dropchop.recyclone.base.api.model.attr.AttributeString;
import com.dropchop.recyclone.base.api.model.invoke.*;
import com.dropchop.recyclone.base.api.model.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.base.api.model.invoke.ResultFilter.LanguageFilter;
import com.dropchop.recyclone.base.api.model.utils.ProfileTimer;
import com.dropchop.recyclone.quarkus.runtime.rest.RestClass;
import com.dropchop.recyclone.quarkus.runtime.rest.RestMethod;
import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.dropchop.recyclone.base.api.model.rest.Constants.Params.Header;
import static com.dropchop.recyclone.base.api.model.rest.Constants.Params.Query;

/**
 * Fills Params object obtained from ExecContext with request parameters
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 29. 12. 21.
 */
@ConstrainedTo(RuntimeType.SERVER)
public class ParamsDecoratorFilter implements ContainerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(ParamsDecoratorFilter.class);

  private final Class<? extends Params> paramType;

  public ParamsDecoratorFilter(RestClass restClass,
                               RestMethod restMethod) {
    String paramsClassName = restMethod.getParamClass();
    if (paramsClassName == null) {
      paramsClassName = restClass.getParamClass();
    }
    if (paramsClassName == null) {
      paramType = null;
    } else {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      try {
        //noinspection unchecked
        paramType = (Class<? extends Params>) cl.loadClass(paramsClassName);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private List<String> getStringList(String queryName, String headerName,
                                     ContainerRequestContext requestContext) {
    MultivaluedMap<String, String> map = requestContext.getUriInfo().getQueryParameters(true);

    if (queryName != null) {
      List<String> tmp = map.get(queryName);
      if (tmp != null && !tmp.isEmpty()) {
        return tmp;
      }
    }

    if (headerName != null) {
      map = requestContext.getHeaders();
      List<String> tmp = map.get(headerName);
      if (tmp != null && !tmp.isEmpty()) {
        if (tmp.size() == 1) {// simple array serialization https://swagger.io/docs/specification/serialization/
          String valueStr = tmp.getFirst();
          return Arrays.asList(valueStr.split(",", -1));
        }
        return tmp;
      }
    }

    return null;
  }

  private String getString(String queryName, String headerName, ContainerRequestContext requestContext) {
    MultivaluedMap<String, String> map = requestContext.getUriInfo().getQueryParameters(true);

    if (queryName != null) {
      String tmp = map.getFirst(queryName);
      if (tmp != null && !tmp.isEmpty()) {
        return tmp;
      }
    }

    if (headerName != null) {
      map = requestContext.getHeaders();
      List<String> values = map.get(headerName);
      if (values != null && !values.isEmpty()) {
        return values.getFirst();
      }
    }

    return null;
  }

  private int getInteger(String name, int defaulTValue, int minValue, MultivaluedMap<String, String> map) {
    String val = map.getFirst(name);
    if (val == null || val.isBlank()) {
      return defaulTValue;
    }
    try {
      int i = Integer.parseInt(val);
      if (i < minValue) {
        return defaulTValue;
      }
      return i;
    } catch (NumberFormatException e) {
      return defaulTValue;
    }
  }

  private void parseLanguage(CommonParams<?, ?, ?, ?> params, String str) {
    str = str.replaceAll(";q=\\d+.\\d+", "");
    String[] parts = str.split("\\.", -1);
    if (parts.length < 1 || parts.length > 2) {
      throw new ServiceException(new StatusMessage(ErrorCode.parameter_validation_error,
        "Unable parse language codes parameter",
        Set.of(new AttributeString(Query.LANG, str))));
    }
    ResultFilter<?, ?> resultFilter = params.getFilter();
    if (resultFilter == null) {
      return;
    }
    LanguageFilter filter = resultFilter.getLang();
    if (filter == null) {
      return;
    }
    if (parts[0] != null && !parts[0].isBlank()) {
      parts[0] = parts[0].trim();
      int idx = parts[0].indexOf(",");
      if (idx > 0) {
        filter.setSearch(parts[0].substring(0, idx));
      } else {
        filter.setSearch(parts[0]);
      }
    }
    if (parts.length > 1 && parts[1] != null && !parts[1].isBlank()) {
      parts[1] = parts[1].trim();
      int idx = parts[1].indexOf(",");
      if (idx > 1) {
        filter.setTranslation(parts[1].substring(1, idx));
      } else {
        filter.setTranslation(parts[1]);
      }
    }
  }

  private void parseContentLevel(CommonParams<?, ?, ?, ?> params, String str) {
    if (str == null || str.isBlank()) {
      return;
    }
    String[] parts = str.split("\\.");
    if (parts.length <= 0 || parts.length > 2) {
      throw new ServiceException(new StatusMessage(ErrorCode.parameter_validation_error,
        "Unable parse content level parameter",
        Set.of(new AttributeString(Query.CLEVEL, str))));
    }
    ResultFilter<?, ?> resultFilter = params.getFilter();
    if (resultFilter == null) {
      return;
    }
    ResultFilter.ContentFilter filter = resultFilter.getContent();
    if (filter == null) {
      return;
    }
    ResultFilterDefaults defaults = params.getFilterDefaults();
    if (defaults == null) {
      return;
    }
    try {
      filter.setTreeLevel(Integer.parseInt(parts[0]));
    } catch (NumberFormatException e) {
      throw new ServiceException(new StatusMessage(ErrorCode.parameter_validation_error,
        "Unable parse content level tree depth parameter",
        Set.of(new AttributeString(Query.CLEVEL, str))), e);
    }

    if (parts.length == 2) {
      List<String> levels = defaults.getAvailableLevelOfContentDetails();
      if (!levels.contains(parts[1])) {
        throw new ServiceException(new StatusMessage(ErrorCode.parameter_validation_error,
          "Unknown content level detail",
          Set.of(new AttributeString(Query.CLEVEL, str))));
      }
      filter.setDetailLevel(parts[1]);
    }
  }

  private void decorate(CommonParams<?, ?, ?, ?> params, ContainerRequestContext requestContext) {
    List<String> tmp = getStringList(Query.MODIFY_POLICY, Header.MODIFY_POLICY, requestContext);
    if (tmp != null) {
      params.setModifyPolicy(tmp);
    }

    ResultFilter<?, ?> resultFilter = params.getFilter();
    if (resultFilter == null) {
      return;
    }
    ResultFilterDefaults defaults = params.getFilterDefaults();
    if (defaults == null) {
      return;
    }
    UriInfo uriInfo = requestContext.getUriInfo();
    int from = getInteger(Query.FROM, defaults.getFrom(), defaults.getFromMin(),
      uriInfo.getQueryParameters());

    resultFilter.setFrom(from);

    int size = getInteger(Query.SIZE, defaults.getSize(), defaults.getSizeMin(),
      uriInfo.getQueryParameters());
    resultFilter.setSize(size);

    String languageStr = getString(Query.LANG, Header.LANG, requestContext);
    if (languageStr != null && !languageStr.isBlank()) {
      parseLanguage(params, languageStr);
    }

    tmp = getStringList(Query.SORT, null, requestContext);
    if (tmp != null) {
      resultFilter.setSort(tmp);
    }

    String tmpLevel = getString(Query.CLEVEL, Header.CLEVEL, requestContext);
    if (tmpLevel != null && !tmpLevel.isBlank()) {
      parseContentLevel(params, tmpLevel);
    }

    tmp = getStringList(Query.STATE, "<<undefined>>", requestContext);
    if (tmp != null && !tmp.isEmpty()) {
      resultFilter.setStates(tmp);
    }

    String contentVersion = getString(null, Header.VERSION, requestContext);
    if (contentVersion != null && !contentVersion.isBlank()) {
      resultFilter.setVersion(contentVersion);
    }

    ResultFilter.ContentFilter contentFilter = resultFilter.getContent();
    if (contentFilter == null) {
      return;
    }

    tmp = getStringList(Query.CFIELDS, Header.CFIELDS, requestContext);
    if (tmp != null) {
      List<String> includes = new ArrayList<>();
      List<String> excludes = new ArrayList<>();
      for (String fieldFilterStr : tmp) {
        if (fieldFilterStr == null || fieldFilterStr.isBlank()) {
          continue;
        }
        if (fieldFilterStr.startsWith("-")) {
          excludes.add(fieldFilterStr.substring(1));
        } else if (fieldFilterStr.startsWith("+")) {
          includes.add(fieldFilterStr.substring(1));
        } else {
          includes.add(fieldFilterStr);
        }
      }
      if (!includes.isEmpty()) {
        contentFilter.setIncludes(includes);
      }
      if (!excludes.isEmpty()) {
        contentFilter.setExcludes(excludes);
      }
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    ProfileTimer timer = new ProfileTimer();
    String path = requestContext.getUriInfo().getPath();
    log.trace("Executing REST params decorator [{}]", path);
    if (paramType == null) {
      log.warn("Missing parameter type for {}!", requestContext.getUriInfo());
      return;
    }
    if (!CommonParams.class.isAssignableFrom(paramType) || CommonParams.class.equals(paramType)) {
      return;
    }
    ExecContext<?> execContext = (ExecContext<?>)requestContext
      .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);
    if (execContext == null) {
      log.warn("Missing {} in {}!", ExecContext.class.getSimpleName(), ContainerRequestContext.class.getSimpleName());
      return;
    }
    Params params = (Params)requestContext.getProperty(InternalContextVariables.RECYCLONE_PARAMS);
    if (params instanceof CommonParams<?, ?, ?, ?> commonParams) {
      decorate(commonParams, requestContext);
      log.debug(
          "Decorated REST request local params from execution context [{}] in [{}]ms.", execContext, timer.stop()
      );
    }
  }
}
