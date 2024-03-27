package com.dropchop.recyclone.quarkus.runtime.rest;

import com.dropchop.recyclone.model.api.base.State;
import com.dropchop.recyclone.model.api.invoke.CommonParams;
import com.dropchop.recyclone.model.api.invoke.Params;
import com.dropchop.recyclone.model.api.invoke.ResultFilter;
import com.dropchop.recyclone.model.api.invoke.ResultFilterDefaults;
import com.dropchop.recyclone.model.api.rest.Constants;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig.Rest;
import io.smallrye.openapi.api.models.OperationImpl;
import io.smallrye.openapi.api.models.info.ContactImpl;
import io.smallrye.openapi.api.models.info.InfoImpl;
import io.smallrye.openapi.api.models.info.LicenseImpl;
import io.smallrye.openapi.api.models.security.SecurityRequirementImpl;
import io.smallrye.openapi.api.models.security.SecuritySchemeImpl;
import org.eclipse.microprofile.openapi.OASFactory;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.Components;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.Operation;
import org.eclipse.microprofile.openapi.models.info.Contact;
import org.eclipse.microprofile.openapi.models.info.Info;
import org.eclipse.microprofile.openapi.models.info.License;
import org.eclipse.microprofile.openapi.models.media.Schema;
import org.eclipse.microprofile.openapi.models.parameters.Parameter;
import org.eclipse.microprofile.openapi.models.responses.APIResponse;
import org.eclipse.microprofile.openapi.models.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.models.security.SecurityScheme;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 11. 03. 24.
 */
@SuppressWarnings("SameParameterValue")
public class OasFilter implements OASFilter {

  private static final Logger log = Logger.getLogger(OasFilter.class);

  private final RecycloneBuildConfig buildConfig;
  private final Map<String, OasMapping> operationMapping;

  private final Map<String, Params> paramsInstanceCache = new HashMap<>();

  public OasFilter(RecycloneBuildConfig buildConfig, Map<String, OasMapping> operationMapping) {
    this.buildConfig = buildConfig;
    this.operationMapping = operationMapping;
  }

  private License getOrCreateLicense(Info info) {
    License license = info.getLicense();
    if (license == null) {
      license = new LicenseImpl();
      info.setLicense(license);
    }
    return license;
  }

  private Contact getOrCreateContact(Info info) {
    Contact contact = info.getContact();
    if (contact == null) {
      contact = new ContactImpl();
      info.setContact(contact);
    }
    return contact;
  }

  private Info getOrCreateInfo(OpenAPI openAPI) {
    Info info = openAPI.getInfo();
    if (info == null) {
      info = new InfoImpl();
      openAPI.setInfo(info);
      getOrCreateContact(info);
      getOrCreateLicense(info);
    }
    return info;
  }

  private List<SecurityRequirement> createSecurityRequirements(Map<String, Rest.Security> security) {
    List<SecurityRequirement> securityRequirements = new ArrayList<>(security.size());
    for (Map.Entry<String, Rest.Security> entry : security.entrySet()) {
      String name = entry.getKey();
      SecurityRequirement securityRequirement = new SecurityRequirementImpl();
      securityRequirement.addScheme(name);
      securityRequirements.add(securityRequirement);
    }
    return securityRequirements;
  }

  private void createSecurity(OpenAPI openAPI, Map<String, Rest.Security> security) {
    List<SecurityRequirement> securityRequirements = openAPI.getSecurity();
    if (securityRequirements == null) {
      securityRequirements = new ArrayList<>(security.size());
      openAPI.setSecurity(securityRequirements);
    }
    for (Map.Entry<String, Rest.Security> entry : security.entrySet()) {
      String name = entry.getKey();
      SecurityRequirement securityRequirement = new SecurityRequirementImpl();
      securityRequirement.addScheme(name);
      securityRequirements.add(securityRequirement);
      Rest.Security restSecurity = entry.getValue();

      Components components = openAPI.getComponents();
      if (components != null && restSecurity != null) {
        SecurityScheme securityScheme = new SecuritySchemeImpl();
        if (restSecurity.type.isPresent()) {
          if (restSecurity.type.get().equalsIgnoreCase("http")) {
            securityScheme.setType(SecurityScheme.Type.HTTP);
          } else if (restSecurity.type.get().equalsIgnoreCase("apikey")) {
            securityScheme.setType(SecurityScheme.Type.APIKEY);
            restSecurity.apiKeyName.ifPresent(securityScheme::setName);
          }
        }
        if (restSecurity.in.isPresent()) {
          if (restSecurity.type.get().equalsIgnoreCase("query")) {
            securityScheme.setIn(SecurityScheme.In.QUERY);
          } else if (restSecurity.type.get().equalsIgnoreCase("cookie")) {
            securityScheme.setIn(SecurityScheme.In.COOKIE);
          } else if (restSecurity.type.get().equalsIgnoreCase("header")) {
            securityScheme.setIn(SecurityScheme.In.HEADER);
          }
        }

        restSecurity.scheme.ifPresent(securityScheme::setScheme);
        restSecurity.bearerFormat.ifPresent(securityScheme::setBearerFormat);
        components.addSecurityScheme(name, securityScheme);
      }
    }
  }

  @Override
  public void filterOpenAPI(OpenAPI openAPI) {
    //openAPI.getInfo().setTitle(openAPI.getInfo().getTitle() + " %s".formatted("NIKOLA"));
    Info info = getOrCreateInfo(openAPI);
    this.buildConfig.rest.info.title.ifPresent(info::setTitle);
    this.buildConfig.rest.info.version.ifPresent(info::setVersion);
    Contact contact = getOrCreateContact(info);
    this.buildConfig.rest.info.contact.name.ifPresent(contact::setName);
    this.buildConfig.rest.info.contact.email.ifPresent(contact::setEmail);
    this.buildConfig.rest.info.contact.url.ifPresent(contact::setUrl);
    License license = getOrCreateLicense(info);
    this.buildConfig.rest.info.license.name.ifPresent(license::setName);
    this.buildConfig.rest.info.license.url.ifPresent(license::setUrl);

    Components components = openAPI.getComponents();
    if (components != null) {
      Set<String> remove = new HashSet<>();
      for (Map.Entry<String, Schema> entry : components.getSchemas().entrySet()) {
        if (entry.getKey().endsWith("1")) {
          remove.add(entry.getKey());
        }
      }
      for (String toRemove : remove) {
        components.removeSchema(toRemove);
      }
    }

    Map<String, Rest.Security> security = this.buildConfig.rest.security;
    if (!security.isEmpty()) {
      this.createSecurity(openAPI, security);
    }
  }

  @Override
  public Schema filterSchema(Schema schema) {
    String ref = schema.getRef();
    if (ref != null && ref.endsWith("1")) {
      schema.setRef(ref.substring(0, ref.length() - 1));
    }
    return schema;
  }

  /**
   * Remove badly resolved classes
   */
  @Override
  public APIResponse filterAPIResponse(APIResponse apiResponse) {
    String ref = apiResponse.getRef();
    if (ref != null && ref.endsWith("1")) {
      apiResponse.setRef(ref.substring(0, ref.length() - 1));
    }
    return apiResponse;
  }

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

  @SuppressWarnings("unused")
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
    return createParam(schema, in, Constants.Params.Query.STATE, descr);
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
    return createParam(schema, in, Constants.Params.Query.SORT, descr);
  }

  private String splitCamelCase(String s) {
    int idx = s.lastIndexOf('.');
    if (idx >= 0) {
      s = s.substring(idx + 1);
    }
    return s.replaceAll(
        String.format("%s|%s|%s",
            "(?<=[A-Z])(?=[A-Z][a-z])",
            "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"),
        " ");
  }

  @Override
  public Operation filterOperation(Operation operation) {
    if (!(operation instanceof OperationImpl op)) {
      return operation;
    }
    Map<String, Rest.Security> security = this.buildConfig.rest.security;
    if (!security.isEmpty()) {
      List<SecurityRequirement> securityRequirements = createSecurityRequirements(security);
      op.setSecurity(securityRequirements);
    }
    OasMapping config = this.operationMapping.get(op.getMethodRef());
    if (config == null) {
      return operation;
    }

    if (config.isSkip()) {
      return null;
    }

    String defaultName = splitCamelCase(config.getClassName());
    List<String> tags = operation.getTags();
    if (tags == null || tags.isEmpty()) {
      operation.setTags(new ArrayList<>(config.getTags()));
    } else if (tags.size() == 1 && tags.contains(defaultName)) {
      operation.setTags(new ArrayList<>(config.getTags()));
    }

    if (config.getParamClassName() == null) {
      return operation;
    }

    Params dtoParameters = paramsInstanceCache.computeIfAbsent(config.getParamClassName(), this::createInstance);
    if (!(dtoParameters instanceof CommonParams<?, ?, ?, ?> commonParams)) {
      return operation;
    }

    ResultFilterDefaults defaults = commonParams.getFilterDefaults();

    List<Parameter> newParameters = new ArrayList<>();
    List<Parameter> parameters = operation.getParameters();
    if (parameters != null) {
      newParameters.addAll(parameters);
    }
    newParameters.add(createIntParam(
        Parameter.In.QUERY, Constants.Params.Query.FROM,
        "Starting offset of returned data. For example: " + defaults.getFrom(),
        defaults.getFromMin()
    ));
    newParameters.add(createIntParam(
        Parameter.In.QUERY, Constants.Params.Query.SIZE,
        "Size of returned data. For example: " + defaults.getSize(),
        defaults.getSizeMin()
    ));
    newParameters.add(createStrParam(
        Parameter.In.QUERY, Constants.Params.Query.LANG,
        "Optional dot delimited language codes <b>[search_lang.translate_lang]</b> for search and " +
            "returned content which can be translated (i.e.: title, body...). <br />" +
            "For example: English search and Slovene translated response <b>en.sl</b> or " +
            "<b>.sl</b> for default and Slovene translated response. " +
            "<br /><b>Accept-Language</b> header can also be used in the same way."
    ));

    ResultFilter<?, ?> resultFilter = commonParams.getFilter();
    if (resultFilter == null) {
      log.warnv("[{}] instance is null in [{}]!", ResultFilter.class, CommonParams.class);
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
        Parameter.In.QUERY, Constants.Params.Query.CFIELDS,
        "JSON paths, prefixed with <b>[+/-]</b> describing <b>exclusively included</b> or <b>excluded</b> fields in JSON response object." +
            "<br /> Includes always precede excludes. If prefix is omitted exclusively included JSON path is assumed. " +
            "<br /> <b>X-Content-Fields</b> header can also be used in the same way."
    ));
    newParameters.add(createStrParam(
        Parameter.In.QUERY, Constants.Params.Query.CLEVEL,
        "Level and detail of objects to output in <b>[N].[detail_level]</b> string notation, Where N is tree depth and " +
            "<br />detail_level can be for instance one of " + defaults.getAvailableLevelOfContentDetails() + ". " +
            "<br /> <b>X-Content-Level</b> header can also be used in the same way."
    ));

    operation.setParameters(newParameters);

    return operation;
  }
}
