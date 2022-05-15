package com.dropchop.recyclone.rest.jaxrs.server.localization;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.dto.invoke.CodeParams;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.CommonExecContext;
import com.dropchop.recyclone.service.api.localization.LanguageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import java.util.List;

import static com.dropchop.recyclone.model.api.security.Constants.PERM_DELIM;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 22. 01. 22.
 */
@Slf4j
@RequestScoped
@Path(Paths.PUBLIC + Paths.Localization.LANGUAGE)
@RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.VIEW)
public class LanguageResource implements
  com.dropchop.recyclone.rest.jaxrs.api.localization.LanguageResource {

  @Inject
  ServiceSelector selector;

  @Inject
  @SuppressWarnings("CdiInjectionPointsInspection")
  CommonExecContext<CodeParams, Language> ctx;

  @Override
  public Result<Language> get() {
    return selector.select(LanguageService.class).search();
  }

  @Override
  public Result<Language> getByCode(String code) {
    CodeParams params = ctx.getParams();
    params.setCodes(List.of(code));
    return selector.select(LanguageService.class).search();
  }

  @Override
  public Result<Language> search(CodeParams parameters) {
    CodeParams thContext = ctx.getParams();
    log.info("search() [{}] [{}] vs [{}]", thContext == parameters, thContext, parameters);
    return selector.select(LanguageService.class).search();
  }
}
