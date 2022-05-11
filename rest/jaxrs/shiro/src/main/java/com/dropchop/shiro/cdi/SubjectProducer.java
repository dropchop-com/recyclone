package com.dropchop.shiro.cdi;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 2. 01. 22.
 */
@ApplicationScoped
public class SubjectProducer {

  @Produces
  @RequestScoped
  public Subject subject() {
    return SecurityUtils.getSubject();
  }
}
