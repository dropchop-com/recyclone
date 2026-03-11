package com.dropchop.shiro.cdi;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.AuthenticationStrategy;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;

import java.util.List;

/**
 * Modeled and copied from Shiro Spring.
 *
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 6. 01. 22.
 */
@DefaultBean
@ApplicationScoped
public class ShiroEnvironment extends ShiroAbstractEnvironment {

  @Produces
  @ApplicationScoped
  @SuppressWarnings("CdiInjectionPointsInspection")
  protected SessionsSecurityManager securityManager(List<Realm> realms) {
    return super.securityManager(realms);
  }

  @Override
  protected SessionManager sessionManager() {
    return super.sessionManager();
  }

  @Override
  protected SubjectDAO subjectDAO() {
    return super.subjectDAO();
  }

  @Override
  protected SessionStorageEvaluator sessionStorageEvaluator() {
    return super.sessionStorageEvaluator();
  }

  @Override
  protected SubjectFactory subjectFactory() {
    return super.subjectFactory();
  }

  @Override
  protected SessionFactory sessionFactory() {
    return super.sessionFactory();
  }

  @Override
  protected SessionDAO sessionDAO() {
    return super.sessionDAO();
  }

  @Override
  protected Authorizer authorizer() {
    return super.authorizer();
  }

  @Override
  protected AuthenticationStrategy authenticationStrategy() {
    return super.authenticationStrategy();
  }

  @Override
  protected Authenticator authenticator() {
    return super.authenticator();
  }

  @Override
  protected RememberMeManager rememberMeManager() {
    return super.rememberMeManager();
  }
}
