package com.dropchop.shiro.utils;

import com.dropchop.recyclone.model.dto.security.Permission;

import java.util.List;


/**
 * Resolve permissions for subject or subjects of subject, thus allowing permission hierarchy (parent - child)
 */
public interface Resolver {

  List<Permission> resolve(String subject, String subjectIdentifier, String subSubject, String subSubjectIdentifier);
}
