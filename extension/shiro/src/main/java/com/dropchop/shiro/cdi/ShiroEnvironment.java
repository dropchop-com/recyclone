package com.dropchop.shiro.cdi;

import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.List;

/**
 * Modeled and copied from Shiro Spring.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 6. 01. 22.
 */
@ApplicationScoped
public class ShiroEnvironment extends ShiroAbstractEnvironment {

  @Produces
  @Override
  protected SessionsSecurityManager securityManager(List<Realm> realms) {
    return super.securityManager(realms);
  }

  @Produces
  @Override
  protected SessionManager sessionManager() {
    return super.sessionManager();
  }

  @Produces
  @Override
  protected SubjectDAO subjectDAO() {
    return super.subjectDAO();
  }

  @Produces
  @Override
  protected SessionStorageEvaluator sessionStorageEvaluator() {
    return super.sessionStorageEvaluator();
  }

  @Produces
  @Override
  protected SubjectFactory subjectFactory() {
    return super.subjectFactory();
  }

  @Produces
  @Override
  protected SessionFactory sessionFactory() {
    return super.sessionFactory();
  }

  @Produces
  @Override
  protected SessionDAO sessionDAO() {
    return super.sessionDAO();
  }

  @Produces
  @Override
  protected Authorizer authorizer() {
    return super.authorizer();
  }

  @Produces
  @Override
  protected AuthenticationStrategy authenticationStrategy() {
    return super.authenticationStrategy();
  }

  @Produces
  @Override
  protected Authenticator authenticator() {
    return super.authenticator();
  }

  @Produces
  @Override
  protected RememberMeManager rememberMeManager() {
    return super.rememberMeManager();
  }
}
