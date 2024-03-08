package com.dropchop.recyclone.rest.jaxrs.server.openapi;

import com.dropchop.recyclone.model.api.base.State;
import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ResultFilter;
import com.dropchop.recyclone.model.api.invoke.ResultFilterDefaults;
import com.dropchop.recyclone.model.api.rest.Constants;
import jakarta.ws.rs.core.Application;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.OASFactory;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.media.Schema;
import org.eclipse.microprofile.openapi.models.parameters.Parameter;
import org.eclipse.microprofile.openapi.models.tags.Tag;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;

import static com.dropchop.recyclone.model.api.rest.Constants.Params.Query;


/**
 * OpenAPI user interface filter.
 *
 * - removes all dyn- tags.
 * - removes all REST ops which are not from implementation classes
 * - adds common parameters to all methods annotated with tag: dyn-params:fqn.dto.Params
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 23. 01. 22.
 */
@Slf4j
@SuppressWarnings("SameParameterValue")
public class CommonUiFilter implements OASFilter {

  @SuppressWarnings("FieldCanBeLocal")
  public final String CONF_PROP_NAME_DC_APP_CLASS = "dropchop.application.class";

  private final Map<String, Params> paramsInstanceCache = new HashMap<>();

  private Params createInstance(String className) {
    Class<Params> parametersClass;
    try {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      //noinspection unchecked
      parametersClass = (Class<Params>)loader.loadClass(className);
    } catch (Exception e) {
      log.warn("Unable to load [{}] parameters class!", className, e);
      return null;
    }
    Params parameters;
    try {
      parameters = parametersClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      log.warn("Unable to instantiate [{}] parameters class!", className, e);
      return null;
    }
    return parameters;
  }

  private Parameter createParam(Schema schema, Parameter.In in, String name, String descr) {
    Parameter parameter = OASFactory.createParameter();
    parameter.setName(name);
    parameter.setSchema(schema);
    parameter.setIn(in);
    parameter.setRequired(false);
    parameter.setDescription(descr);
    return parameter;
  }

  private Parameter createIntParam(Parameter.In in, String name, String descr, int min) {
    Schema schema = OASFactory.createSchema();
    schema.type(Schema.SchemaType.INTEGER);
    schema.minimum(new BigDecimal(min));
    return createParam(schema, in, name, descr);
  }

  private Parameter createStrParam(Parameter.In in, String name, String descr) {
    Schema schema = OASFactory.createSchema();
    schema.type(Schema.SchemaType.STRING);
    return createParam(schema, in, name, descr);
  }

  private Parameter createStrArrayParam(Parameter.In in, String name, String descr) {
    Schema itemSchema = OASFactory.createSchema();
    itemSchema.type(Schema.SchemaType.STRING);
    Schema schema = OASFactory.createSchema();
    schema.type(Schema.SchemaType.ARRAY);
    schema.setItems(itemSchema);
    return createParam(schema, in, name, descr);
  }

  private Parameter createStrEnumParam(Parameter.In in, String name, String descr, List<String> enums) {
    Schema schema = OASFactory.createSchema();
    schema.type(Schema.SchemaType.STRING);
    for (String en : enums) {
      schema.addEnumeration(en);
    }
    return createParam(schema, in, name, descr);
  }

  private Parameter createStatesParam(Parameter.In in, String descr, Iterable<State.Code> hidden) {
    Schema itemSchema = OASFactory.createSchema();
    itemSchema.type(Schema.SchemaType.STRING);

    Schema schema = OASFactory.createSchema();
    schema.type(Schema.SchemaType.ARRAY);
    schema.items(itemSchema);
    for (State.Code code : hidden) {
      itemSchema.addEnumeration(code.toString());
    }
    return createParam(schema, in, Query.STATE, descr);
  }

  private Parameter createSortParam(Parameter.In in, String descr, String[] sort) {
    Schema itemSchema = OASFactory.createSchema();
    itemSchema.type(Schema.SchemaType.STRING);

    Schema schema = OASFactory.createSchema();
    schema.type(Schema.SchemaType.ARRAY);
    schema.items(itemSchema);
    for (String s : sort) {
      itemSchema.addEnumeration(s);
    }
    return createParam(schema, in, Query.SORT, descr);
  }

  @Override
  public Tag filterTag(Tag tag) {
    if (tag == null) {
      return tag;
    }
    String name = tag.getName();
    if (name == null || name.isBlank()) {
      return tag;
    }
    if (name.startsWith(Constants.Tags.DYNAMIC_PREFIX)) {
      return null;
    }
    return tag;
  }

  private final Set<Class<?>> registeredClasses = new LinkedHashSet<>();

  private Set<Class<?>> getApplicationClasses() {

    Config conf = ConfigProvider.getConfig();
    String appClassName = conf.getConfigValue(CONF_PROP_NAME_DC_APP_CLASS).getValue();
    if (appClassName == null) {
      log.warn("Missing [{}] config property unable to properly filter operations!",
        CONF_PROP_NAME_DC_APP_CLASS);
      return null;
    }
    try {
      @SuppressWarnings("unchecked")
      Class<Application> appClass = (Class<Application>)Thread.currentThread()
        .getContextClassLoader().loadClass(appClassName);
      Application app = appClass.getDeclaredConstructor().newInstance();
      return app.getClasses();
    } catch (Exception e) {
      log.warn("Unable to instantiate javax.ws.rs.core.Application [{}]! Unable to properly filter operations!",
        appClassName, e);
    }
    return null;
  }

  @Override
  public Operation filterOperation(Operation operation) {
    //filter out non-impl operations, or we get duplicated swagger ui output
    String opId = operation.getOperationId();
    if (opId == null) {
      return null;
    }
    if (!opId.contains(".server.") && !opId.contains(".impl.")) {
      return null;
    }

    try {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      Class<?> c = cl.loadClass("com.dropchop.recyclone.test.rest.jaxrs.api.DummyResource");
      Method m = c.getMethod("get");
      Annotation[] annotations = m.getDeclaredAnnotations();
      int brek = 100;
    } catch (Exception e) {

    }

    try {
      int idx = opId.lastIndexOf('_');
      if (idx > -1) {
        Class<?> c = Thread.currentThread()
          .getContextClassLoader().loadClass(opId.substring(0, idx));
        if (c.isInterface()) {
          return null;
        }
        // look at all registered classes from Application class and cache them
        synchronized (this) {
          if (this.registeredClasses.isEmpty()) {
            Set<Class<?>> registeredClasses = getApplicationClasses();
            if (registeredClasses != null) {
              this.registeredClasses.addAll(registeredClasses);
            }
          }
        }
        if (!this.registeredClasses.isEmpty() && !this.registeredClasses.contains(c)) {
          return null;
        }
      }
    } catch (ClassNotFoundException e) {
      log.warn("Unable to get resource class for operation [{}]!", opId);
      return operation;
    }

    List<String> tags = operation.getTags();
    if (tags == null) {
      return operation;
    }

    //attach common params
    for (String tag : tags) {
      if (tag == null || tag.isBlank()) {
        return operation;
      }

      //remove internal dynamic tag
      if (tag.startsWith(Constants.Tags.DYNAMIC_PREFIX)) {
        List<String> newTags = new ArrayList<>(operation.getTags());
        newTags.remove(tag);
        operation.setTags(newTags);
      }

      if (tag.startsWith(Constants.Tags.DYNAMIC_PREFIX + "params")) {
        String[] descr = tag.split(":", 2);
        if (descr.length != 2) {
          log.warn("Missing DTO Parameters class in [dyn-params] OpenAPI tag annotation for operation [{}]!",
            operation.getOperationId());
        }

        Params dtoParameters = paramsInstanceCache.computeIfAbsent(descr[1], this::createInstance);
        if (!(dtoParameters instanceof CommonParams commonParams)) {
          return operation;
        }

        ResultFilterDefaults defaults = commonParams.getFilterDefaults();

        List<Parameter> newParameters = new ArrayList<>();
        List<Parameter> parameters = operation.getParameters();
        if (parameters != null) {
          newParameters.addAll(parameters);
        }
        newParameters.add(createIntParam(
          Parameter.In.QUERY, Query.FROM,
          "Starting offset of returned data. For example: " + defaults.getFrom(),
          defaults.getFromMin()
        ));
        newParameters.add(createIntParam(
          Parameter.In.QUERY, Query.SIZE,
          "Size of returned data. For example: " + defaults.getSize(),
          defaults.getSizeMin()
        ));
        newParameters.add(createStrParam(
          Parameter.In.QUERY, Query.LANG,
          "Optional dot delimited language codes <b>[search_lang.translate_lang]</b> for search and " +
            "returned content which can be translated (i.e.: title, body...). <br />" +
            "For example: English search and Slovene translated response <b>en.sl</b> or " +
            "<b>.sl</b> for default and Slovene translated response. " +
            "<br /><b>Accept-Language</b> header can also be used in the same way."
        ));
        /*newParameters.add(createStrParam(
          Parameter.In.HEADER, Header.LANG,
          "Optional dot delimited language codes <b>[search_lang.translate_lang]</b> for search and " +
            "returned content which can be translated (i.e.: title, body...). <br />" +
            "For example: English search and Slovene translated response <b>en.sl</b> or " +
            "<b>.sl</b> for default and Slovene translated response."
        ));*/

        ResultFilter<?, ?> resultFilter = commonParams.getFilter();
        if (resultFilter == null) {
          log.warn("[{}] instance is null in [{}]!", ResultFilter.class, CommonParams.class);
          return operation;
        }

        Collection<State.Code> hiddenStates = defaults.getAvailableHiddenStates();
        if (hiddenStates != null && !hiddenStates.isEmpty()) {
          newParameters.add(createStatesParam(
            Parameter.In.QUERY,
            "Also include resources with selected state which are not normally shown in output.",
            hiddenStates
          ));
        }

        String[] sortFields = defaults.getAvailableSortFields();
        if (sortFields != null && sortFields.length > 0) {
          newParameters.add(createSortParam(
            Parameter.In.QUERY,
            "Sort result by field name, prefixed with <b>[+/-]</b> for ascending / descending order. " +
              "<br /> If prefix is omitted ascending sort order is assumed",
            sortFields
          ));
        }

        newParameters.add(createStrArrayParam(
          Parameter.In.QUERY, Query.CFIELDS,
          "JSON paths, prefixed with <b>[+/-]</b> describing <b>exclusively included</b> or <b>excluded</b> fields in JSON response object." +
            "<br /> Includes always precede excludes. If prefix is omitted exclusively included JSON path is assumed. " +
            "<br /> <b>X-Content-Fields</b> header can also be used in the same way."
        ));
        newParameters.add(createStrParam(
          Parameter.In.QUERY, Query.CLEVEL,
          "Level and detail of objects to output in <b>[N].[detail_level]</b> string notation, Where N is tree depth and " +
            "<br />detail_level can be for instance one of " + defaults.getAvailableLevelOfContentDetails() + ". " +
            "<br /> <b>X-Content-Level</b> header can also be used in the same way."
        ));
        /*newParameters.add(createStrArrayParam(
          Parameter.In.HEADER, Header.CFIELDS,
          "JSON paths, prefixed with <b>[+/-]</b> describing <b>exclusively included</b> or <b>excluded</b> fields in JSON response object." +
            "<br /> Includes always precede excludes. If prefix is omitted exclusively included JSON path is assumed."
        ));
        newParameters.add(createStrParam(
          Parameter.In.HEADER, Header.CLEVEL,
          "Level and detail of objects to output in <b>[N].[detail_level]</b> string notation, Where N is tree depth and " +
            "<br />detail_level can be for instance one of " + defaults.getAvailableLevelOfContentDetails() + "."
        ));*/

        /*newParameters.add(createStrEnumParam(
          Parameter.In.HEADER, Header.VERSION,
          "Requested content response version.",
          defaults.getAvailableVersions()
        ));*/
        operation.setParameters(newParameters);
      }
    }

    return operation;
  }
}
