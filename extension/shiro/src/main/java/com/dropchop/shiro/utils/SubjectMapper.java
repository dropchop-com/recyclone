package com.dropchop.shiro.utils;

import com.dropchop.recyclone.model.api.base.Dto;
import com.dropchop.recyclone.model.api.base.Entity;
import com.dropchop.recyclone.model.api.base.Model;

import java.util.*;


/**
 * Subject mapper to map Dto and Entity types to String representation in data source.
 * Use case:
 * Usually Entities and Dto types are representing the same data object, so you want to use same subject name
 * to represent this Model types in datasource.
 * for example:
 * User dto and EUser entity represent the same subject in security (ie: "user"). So when searching for subject permissions,
 * one can use either dto or entity type to get correct permission data out of data sources.
 */
abstract public class SubjectMapper {

  /**
   * Model to string mapping
   */
  private final Map<Class<? extends Model>, String> map = new HashMap<>();

  /**
   * String to model mappings for reverse resolving
   */
  private final Map<String, Set<Class<? extends Model>>> valueMap = new HashMap<>();


  /**
   * Takes parameter map and splits them into two maps.
   * @param map
   */
  protected void setMap(Map<Class<? extends Model>, String> map) {
    if (map == null || map.isEmpty()) return;
    map.forEach( (k, v) -> {
      this.map.put(k, v);
      Set<Class<? extends Model>> models = this.valueMap.computeIfAbsent(v, key -> new HashSet<>());
      models.add(k);
    });
  }


  /**
   * Returns subject for Model type if mapped.
   * @param model type.
   * @return string subject representation.
   */
  public String toSubject(Class<? extends Model> model) {
    if (model == null) return null;
    return this.map.get(model);
  }


  /**
   * Returns Entity model type from subject.
   * @param subject representation.
   * @return Entity type.
   */
  public Class<? extends Entity> toEntity(String subject) {
    return (Class<? extends Entity>)this.fromSubject(subject, false);
  }


  /**
   * Returns Dto model type from subject.
   * @param subject representation.
   * @return Dto type.
   */
  public Class<? extends Dto> toDto(String subject) {
    return (Class<? extends Dto>)this.fromSubject(subject, false);
  }


  /**
   * Returns model type from subject if mapped. If asDto is set, Dto type will be returned else Entity type.
   *
   * @param subject representation in mapping.
   * @param asDto boolean to get Dto type instead of Entity type if defined.
   * @return type of Model.
   */
  protected Class<? extends Model> fromSubject(String subject, boolean asDto) {
    if (subject == null || subject.isEmpty()) return null;
    Set<Class<? extends Model>> classes = this.valueMap.get(subject);

    return classes.stream().filter(c -> {
      if (asDto) {
        return Dto.class.isAssignableFrom(c);
      }
      return Entity.class.isAssignableFrom(c);
    }).findFirst().orElse(null);
  }


  /**
   * Implement this method to define mappings.
   *
   * Example:
   *   public void init() {
   *     this.setMap(Map.ofEntries(
   *         Map.entry(User.class, "User"),
   *         Map.entry(EUser.class, "User")
   *     ));
   *   }
   */
  abstract protected void init();
}
