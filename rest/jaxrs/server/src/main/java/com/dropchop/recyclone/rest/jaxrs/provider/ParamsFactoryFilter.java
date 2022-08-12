package com.dropchop.recyclone.rest.jaxrs.provider;

import com.dropchop.recyclone.model.api.attr.AttributeString;
import com.dropchop.recyclone.model.api.invoke.*;
import com.dropchop.recyclone.model.api.invoke.Constants.InternalContextVariables;
import com.dropchop.recyclone.service.api.invoke.ExecContextProvider;
import com.dropchop.recyclone.service.api.invoke.ParamsExecContextProvider;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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

  private void parseLanguage(CommonParams params, String str) {
    str = str.replaceAll(";q=\\d+.\\d+", "");
    String[] parts = str.split("\\.", -1);
    if (parts.length < 1 || parts.length > 2) {
      throw new ServiceException(new StatusMessage(ErrorCode.parameter_validation_error,
        "Unable parse language codes parameter",
        Set.of(new AttributeString(CommonParams.LANG_QUERY, str))));
    }
    if (parts[0] != null && !parts[0].isBlank()) {
      parts[0] = parts[0].trim();
      int idx = parts[0].indexOf(",");
      if (idx > 0) {
        params.setLang(parts[0].substring(0, idx));
      } else {
        params.setLang(parts[0]);
      }
    }
    if (parts.length > 1 && parts[1] != null && !parts[1].isBlank()) {
      parts[1] = parts[1].trim();
      int idx = parts[1].indexOf(",");
      if (idx > 1) {
        params.setTranslationLang(parts[1].substring(1, idx));
      } else {
        params.setTranslationLang(parts[1]);
      }
    }
  }

  private void parseContentLevel(CommonParams params, String str) {
    if (str == null || str.isBlank()) {
      return;
    }
    String[] parts = str.split("\\.");
    if (parts.length <= 0 || parts.length > 2) {
      throw new ServiceException(new StatusMessage(ErrorCode.parameter_validation_error,
        "Unable parse content level parameter",
        Set.of(new AttributeString(CommonParams.CLEVEL_QUERY, str))));
    }

    try {
      params.setContentTreeLevel(Integer.parseInt(parts[0]));
    } catch (NumberFormatException e) {
      throw new ServiceException(new StatusMessage(ErrorCode.parameter_validation_error,
        "Unable parse content level tree depth parameter",
        Set.of(new AttributeString(CommonParams.CLEVEL_QUERY, str))), e);
    }

    if (parts.length == 2) {
      try {
        params.setContentDetailLevel(parts[1]);
      } catch (NumberFormatException e) {
        throw new ServiceException(new StatusMessage(ErrorCode.parameter_validation_error,
          "Unable parse content level tree depth parameter",
          Set.of(new AttributeString(CommonParams.CLEVEL_QUERY, str))), e);
      }
    }
  }

  private void decorate(CommonParams params, ContainerRequestContext requestContext) {
    UriInfo uriInfo = requestContext.getUriInfo();
    int from = getInteger(CommonParams.FROM_QUERY, CommonParams.FROM_QUERY_DEFAULT, CommonParams.FROM_QUERY_MIN,
      uriInfo.getQueryParameters());
    params.setFrom(from);

    int size = getInteger(CommonParams.SIZE_QUERY, CommonParams.SIZE_QUERY_DEFAULT, CommonParams.SIZE_QUERY_MIN,
      uriInfo.getQueryParameters());
    params.setSize(size);

    String languageStr = getString(CommonParams.LANG_QUERY, CommonParams.LANG_HEADER, requestContext);
    if (languageStr != null && !languageStr.isBlank()) {
      parseLanguage(params, languageStr);
    }

    List<String> tmp = getStringList(CommonParams.SORT_QUERY, null, requestContext);
    if (tmp != null) {
      params.setSort(tmp);
    }

    tmp = getStringList(CommonParams.CFIELDS_QUERY, CommonParams.CFIELDS_HEADER, requestContext);
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
        params.setContentIncludes(includes);
      }
      if (!excludes.isEmpty()) {
        params.setContentExcludes(excludes);
      }
    }

    String tmpLevel = getString(CommonParams.CLEVEL_QUERY, CommonParams.CLEVEL_HEADER, requestContext);
    if (tmpLevel != null && !tmpLevel.isBlank()) {
      parseContentLevel(params, tmpLevel);
    }

    tmp = getStringList(CommonParams.STATE_QUERY, "<<undefined>>", requestContext);
    if (tmp != null && !tmp.isEmpty()) {
      params.setStates(tmp);
    }

    String contentVersion = getString(null, CommonParams.VERSION_HEADER, requestContext);
    if (contentVersion != null && !contentVersion.isBlank()) {
      params.setVersion(contentVersion);
    }
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    ExecContextProvider execContextProvider = (ExecContextProvider)requestContext
      .getProperty(InternalContextVariables.RECYCLONE_EXEC_CONTEXT_PROVIDER);
    if (execContextProvider == null) {
      log.warn("Missing {} in {}!", ExecContextProvider.class.getSimpleName(), ContainerRequestContext.class.getSimpleName());
      return;
    }
    if (execContextProvider instanceof ParamsExecContextProvider paramsExecContextProvider) {
      Params p;
      try {
        p = parametersClass.getDeclaredConstructor().newInstance();
      } catch (Exception e) {
        throw new ServiceException(new StatusMessage(ErrorCode.internal_error,
          "Unable to instantiate parameter class [" + this.parametersClass + "]", null));
      }

      if (p instanceof CommonParams) {
        decorate((CommonParams) p, requestContext);
      }

      log.debug("Created request local [{}].", p);
      paramsExecContextProvider.setParams(p);
    }
  }
}
