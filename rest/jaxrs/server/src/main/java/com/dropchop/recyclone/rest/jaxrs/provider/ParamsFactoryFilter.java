package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.*;
import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.model.api.invoke.ResultFilter.LanguageFilter;
import com.dropchop.recyclone.model.api.invoke.ExecContextContainer;
import com.dropchop.recyclone.model.api.invoke.ParamsExecContextContainer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import jakarta.ws.rs.ConstrainedTo;
import jakarta.ws.rs.RuntimeType;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.UriInfo;
import java.util.*;

import static com.dropchop.recyclone.model.api.invoke.ExecContext.MDC_REQUEST_ID;
import static com.dropchop.recyclone.model.api.rest.Constants.Params.Header;
import static com.dropchop.recyclone.model.api.rest.Constants.Params.Query;

/**
 * Creates correct Parameters instance initializes it with default parameters
 * and passes it to context property for further processing.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 29. 12. 21.
 */
@Slf4j
@ConstrainedTo(RuntimeType.SERVER)
public class ParamsFactoryFilter implements ContainerRequestFilter {

  private final Class<? extends Params> parametersClass;

  public <P extends Params> ParamsFactoryFilter(Class<P> parametersClass) {
    log.debug("Construct CommonParamsFilter [{}].", parametersClass);
    this.parametersClass = parametersClass;
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
          String valueStr = tmp.get(0);
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
        return values.get(0);
      }
    }

    return null;
  }

  private int getInteger(String name, int defautlValue, int minValue, MultivaluedMap<String, String> map) {
    String val = map.getFirst(name);
    if (val == null || val.isBlank()) {
      return defautlValue;
    }
    try {
      int i = Integer.parseInt(val);
      if (i < minValue) {
        return defautlValue;
      }
      return i;
    } catch (NumberFormatException e) {
      return defautlValue;
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

    List<String> tmp = getStringList(Query.SORT, null, requestContext);
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
    ExecContextContainer execContextProvider = (ExecContextContainer)requestContext
      .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);
    if (execContextProvider == null) {
      log.warn("Missing {} in {}!", ExecContextContainer.class.getSimpleName(), ContainerRequestContext.class.getSimpleName());
      return;
    }
    if (execContextProvider instanceof ParamsExecContextContainer paramsExecContextProvider) {
      Params p;
      try {
        p = parametersClass.getDeclaredConstructor().newInstance();
      } catch (Exception e) {
        throw new ServiceException(new StatusMessage(ErrorCode.internal_error,
          "Unable to instantiate parameter class [" + this.parametersClass + "]", null));
      }

      if (p instanceof CommonParams) {
        decorate((CommonParams<?, ?, ?, ?>) p, requestContext);
      }
      ExecContext<?> execContext = paramsExecContextProvider.get();
      p.setRequestId(execContext.getId());
      MDC.put(MDC_REQUEST_ID, p.getRequestId());
      paramsExecContextProvider.setParams(p);
      log.debug("Created and registered request local params [{}] with execution context [{}].", p, execContext);
      requestContext.setProperty(InternalContextVariables.RECYCLONE_PARAMS, p);
    }
  }
}
