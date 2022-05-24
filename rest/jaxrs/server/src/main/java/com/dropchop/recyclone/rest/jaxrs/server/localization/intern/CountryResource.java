package com.dropchop.recyclone.rest.jaxrs.server.localization.intern;

import com.dropchop.recyclone.model.api.rest.Constants.Paths;
import com.dropchop.recyclone.model.api.security.Constants.Actions;
import com.dropchop.recyclone.model.api.security.Constants.Domains;
import com.dropchop.recyclone.model.dto.localization.Country;
import com.dropchop.recyclone.model.dto.rest.Result;
import com.dropchop.recyclone.service.api.ServiceSelector;
import com.dropchop.recyclone.service.api.localization.CountryService;
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
@Path(Paths.INTERNAL + Paths.Localization.COUNTRY)
@RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.VIEW)
public class CountryResource implements
  com.dropchop.recyclone.rest.jaxrs.api.intern.localization.CountryResource {

  @Inject
  ServiceSelector selector;

  @Override
  @RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.CREATE)
  public Result<Country> create(List<Country> countries) {
    return selector.select(CountryService.class).create(countries);
  }

  @Override
  @RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.DELETE)
  public Result<Country> delete(List<Country> countries) {
    return selector.select(CountryService.class).delete(countries);
  }

  @Override
  @RequiresPermissions(Domains.Localization.COUNTRY + PERM_DELIM + Actions.UPDATE)
  public Result<Country> update(List<Country> countries) {
    return selector.select(CountryService.class).update(countries);
  }
}
