package com.dropchop.shiro.loaders;

import com.dropchop.recyclone.model.api.base.Model;
import com.dropchop.recyclone.model.dto.base.DtoId;
import com.dropchop.recyclone.model.dto.security.Permission;
import com.dropchop.recyclone.model.dto.security.PermissionInstance;
import com.dropchop.recyclone.model.dto.security.PermissionTemplate;
import com.dropchop.recyclone.model.dto.security.User;
import com.dropchop.shiro.utils.SubjectMapper;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Security loader service load user by login name, token or id.
 * Loads subject permissions or permissions defined for subjects sub subjects
 * example:
 *   User is main subject and has set some permissions.
 *   But user has user accounts (sub subject) so each account can have different permissions set.
 *
 */
@SuppressWarnings("unused")
public interface SecurityLoaderService {

  SubjectMapper getSubjectMapper();

  <O extends DtoId> User<O> loadByLoginName(String loginName);
  <O extends DtoId> User<O> loadByToken(String token);
  <O extends DtoId> User<O> loadById(UUID id);

  /**
   * Will load allowed only permissions for subject or subject of subject (sub subject).
   * Subject represents any model that can have permission attached to it.
   *
   * @param subject - subject
   * @param identifier - subject identifier
   * @param subSubject - sub subject
   * @param subSubjectIdentifier - sub subject identifier
   * @return list of allowed permissions for subject or sub subject.
   * @param <M> subject's model class
   */
  <M extends Model> List<Permission> loadPermissions(Class<M> subject, UUID identifier, Class<M> subSubject, UUID subSubjectIdentifier);

  /**
   * Will load permissions for subject or subject of subject (sub subject).
   * Subject represents any model that can have permission attached to it.
   *
   * @param subject - subject
   * @param identifier - subject identifier
   * @param subSubject - sub subject
   * @param subSubjectIdentifier - sub subject identifier
   * @param onlyAllowed - filter permissions by allowed flag
   * @return list of permissions for subject or sub subject
   * @param <M> subject's model class
   */
  <M extends Model> List<Permission> loadPermissions(Class<M> subject, UUID identifier, Class<M> subSubject, UUID subSubjectIdentifier, boolean onlyAllowed);
  List<Permission> loadPermissionsById(Collection<UUID> ids);

  <M extends Model> List<PermissionTemplate> loadTemplates(Class<M> subject, UUID identifier, Class<M> subSubject, UUID subSubjectIdentifier);
  <M extends Model> List<PermissionInstance> loadInstances(Class<M> subject, UUID identifier);
}
