package com.dropchop.shiro.cdi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 08. 2025
 */
@RequestScoped
public class ShiroSubjectProducer {

  @Inject
  SecurityManager shiroSecurityManager;

  private final AtomicReference<PrincipalCollection> atomicReference = new AtomicReference<>();

  public void setAuthenticatedPrincipals(PrincipalCollection principals) {
    this.atomicReference.set(principals); // forcibly sets the Subject
  }

  @Produces
  @RequestScoped
  public Subject produceSubject() {
    PrincipalCollection principals = atomicReference.get();
    if (principals != null) {
      return new Subject.Builder(shiroSecurityManager).authenticated(true).principals(principals).buildSubject();
    }
    return new Subject.Builder(shiroSecurityManager).buildSubject();
  }
}
