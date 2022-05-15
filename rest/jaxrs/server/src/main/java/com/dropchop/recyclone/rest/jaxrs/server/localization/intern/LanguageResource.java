package com.dropchop.recyclone.rest.jaxrs.server.localization.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.dto.localization.Language;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.ServiceSelector;
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
@Path(Paths.INTERNAL + Paths.Localization.LANGUAGE)
@RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.VIEW)
public class LanguageResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.localization.LanguageResource {

  @Inject
  ServiceSelector selector;

  @Override
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.CREATE)
  public Result<Language> create(List<Language> languages) {
    return selector.select(LanguageService.class).create(languages);
  }

  @Override
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.DELETE)
  public Result<Language> delete(List<Language> languages) {
    return selector.select(LanguageService.class).delete(languages);
  }

  @Override
  @RequiresPermissions(Domains.Localization.LANGUAGE + PERM_DELIM + Actions.UPDATE)
  public Result<Language> update(List<Language> languages) {
    return selector.select(LanguageService.class).update(languages);
  }
}
