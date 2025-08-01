package com.dropchop.recyclone.quarkus.runtime.selectors;

import com.dropchop.recyclone.quarkus.runtime.config.RecycloneBuildConfig;
import com.dropchop.recyclone.quarkus.runtime.utils.ClassNameMatcher;
import com.dropchop.recyclone.base.api.common.RecycloneType;
import com.dropchop.recyclone.base.api.service.RecycloneTypeLiteral;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.dropchop.recyclone.base.api.model.marker.Constants.Implementation.SAME_AS_DEPENDANT;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.com> on 14. 06. 24.
 */
public abstract class ConfigurableSelector<X> implements Selector<X> {

  private final static Logger log = LoggerFactory.getLogger(ConfigurableSelector.class);

  protected final RecycloneBuildConfig config;

  protected static class InjectionConfig {
    protected String targetQualifier;
    protected String fallbackQualifier;
    protected ClassNameMatcher matcher;
    protected List<InjectionConfig> dependencies = new ArrayList<>();
  }

  protected final List<InjectionConfig> dependants = new ArrayList<>();
  protected final String defaultQualifier;
  protected final String fallbackQualifier;

  public ConfigurableSelector(RecycloneBuildConfig config) {
    this.config = config;
    RecycloneBuildConfig.InjectionPointsConfig injectionPointsConfig = config.injectionPointsConfig();
    this.defaultQualifier = injectionPointsConfig.targetQualifier();
    this.fallbackQualifier = injectionPointsConfig.fallbackQualifier();
    for (RecycloneBuildConfig.InjectionPointsConfig.Dependant dependant : injectionPointsConfig.matchDependants()) {
      InjectionConfig dep = new InjectionConfig();
      dep.targetQualifier = dependant.targetQualifier() != null
          ? dependant.targetQualifier() : this.defaultQualifier;
      dep.fallbackQualifier = dependant.fallbackQualifier() != null
          ? dependant.fallbackQualifier() : this.fallbackQualifier;
      dep.matcher = ClassNameMatcher.get(dependant.match());
      dependants.add(dep);
      for (RecycloneBuildConfig.InjectionPointsConfig.Dependant.Dependency dependency : dependant.matchDependencies()) {
        InjectionConfig depDep = new InjectionConfig();
        depDep.targetQualifier = dependency.targetQualifier() != null
            ? dependency.targetQualifier() : dep.targetQualifier;
        depDep.fallbackQualifier = dependency.fallbackQualifier() != null
            ? dependency.fallbackQualifier() : dep.fallbackQualifier;
        depDep.matcher = ClassNameMatcher.get(dependency.match());
        dep.dependencies.add(depDep);
      }
    }
  }

  public abstract Instance<X> getInstances();

  public <Y extends X> Y select(Class<Y> dependencyClass, String targetQualifier, String fallbackQualifier) {
    Instance<X> instances = getInstances();
    String qualifier = targetQualifier;
    Instance<Y> dependency = instances.select(dependencyClass, new RecycloneTypeLiteral(qualifier));
    if (!dependency.isResolvable()) {
      qualifier = fallbackQualifier;
      dependency = instances.select(dependencyClass, new RecycloneTypeLiteral(qualifier));
      if (!dependency.isResolvable()) {
        throw new RuntimeException(
            "Missing dependency class [" + dependencyClass +
                "] implementation for qualifiers [@RecycloneType(" + targetQualifier +
                ")] or [@RecycloneType(" + fallbackQualifier + ")]!"
        );
      }
    }
    Y instance = dependency.get();
    log.info("Selected [{}] for requested [{}] using [{}]", instance.getClass(), dependencyClass, qualifier);
    return instance;
  }

  public <Y extends X> Y select(Class<Y> dependencyClass, String targetQualifier, String fallbackQualifier,
                                String dependantQualifier) {
    String targetQ = targetQualifier;
    if (targetQ.equals(SAME_AS_DEPENDANT) && dependantQualifier != null) {
      targetQ = dependantQualifier;
    }
    String fallbackQ = fallbackQualifier;
    if (fallbackQ.equals(SAME_AS_DEPENDANT) && dependantQualifier != null) {
      fallbackQ = dependantQualifier;
    }
    return select(dependencyClass, targetQ, fallbackQ);
  }

  public <Y extends X> Y select(Class<Y> dependencyClass, Class<?> dependantClass) {
    RecycloneType dependantQualifierAnno = dependantClass.getAnnotation(RecycloneType.class);
    String dependantQualifier = null;
    if (dependantQualifierAnno != null) {
      dependantQualifier = dependantQualifierAnno.value();
    }
    for (InjectionConfig dependant : dependants) {
      if (dependant.matcher.matches(dependantClass.getName())) {
        for (InjectionConfig dependecy : dependant.dependencies) {
          if (dependecy.matcher.matches(dependencyClass.getName())) {
            return select(dependencyClass, dependecy.targetQualifier, dependecy.fallbackQualifier, dependantQualifier);
          }
        }
        return select(dependencyClass, dependant.targetQualifier, dependant.fallbackQualifier, dependantQualifier);
      }
    }
    return select(dependencyClass, this.defaultQualifier, this.fallbackQualifier, dependantQualifier);
  }

  public <Y extends X> Y select(Class<Y> dependencyClass, InjectionPoint injectionPoint) {
    Set<Annotation> annotations = injectionPoint.getQualifiers();
    RecycloneType dependantQualifierAnno = injectionPoint.getBean().getBeanClass().getAnnotation(RecycloneType.class);
    String dependantQualifier = null;
    if (dependantQualifierAnno != null) {
      dependantQualifier = dependantQualifierAnno.value();
    }
    for (Annotation a : annotations) {
      if (a.annotationType().equals(Named.class)) {
        Named named = (Named) a;
        return select(dependencyClass, named.value(), this.fallbackQualifier, dependantQualifier);
      }
    }
    return select(dependencyClass, injectionPoint.getBean().getBeanClass());
  }
}
