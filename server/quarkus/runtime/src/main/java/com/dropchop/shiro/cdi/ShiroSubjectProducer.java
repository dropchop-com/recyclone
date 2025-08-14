package com.dropchop.shiro.cdi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 11. 08. 2025
 */
@RequestScoped
public class ShiroSubjectProducer {

  @Inject
  SecurityManager shiroSecurityManager;

  @Produces
  @RequestScoped
  @SuppressWarnings("UnnecessaryLocalVariable")
  public Subject produceSubject() {
    Subject subject = new Subject.Builder(shiroSecurityManager).buildSubject();
    return subject;
  }
}
