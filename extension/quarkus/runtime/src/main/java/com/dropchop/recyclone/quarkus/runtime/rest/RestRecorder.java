package com.dropchop.recyclone.quarkus.runtime.rest;

import com.dropchop.recyclone.quarkus.runtime.spi.RecycloneConfig;
import com.dropchop.recyclone.quarkus.runtime.spi.RecycloneApplication;
import com.dropchop.recyclone.quarkus.runtime.spi.RestResourceConfig;
import io.quarkus.arc.Arc;
import io.quarkus.runtime.annotations.Recorder;
import org.jboss.logging.Logger;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nikola Ivačič <nikola.ivacic@dropchop.org> on 14. 03. 24.
 */
@Recorder
public class RestRecorder {

  private static final Logger log = Logger.getLogger(RestRecorder.class);

  public void createRestApplication(Collection<String> restCandidates) {
    RecycloneApplication application = Arc.container().instance(RecycloneApplication.class).get();
    RestResourceConfig restResourceConfig = application.getRestResourceConfig();
    RecycloneConfig appConfig = application.getAppConfig();

    List<Class<?>> classes = new LinkedList<>(restResourceConfig.getResourceClasses());
    for (String candiadate : restCandidates) {
      boolean doExclude = true;

        RecycloneConfig.Rest restConfig = appConfig.rest;
        if (restConfig.includes.isPresent()) {
          for (String include : restConfig.includes.get()) {
            if (include.equals(candiadate)) {
              doExclude = false;
              break;
            }
          }
        }
        if (restConfig.excludes.isPresent()) {
          for (String exclude : restConfig.excludes.get()) {
            if (exclude.equals(candiadate)) {
              doExclude = true;
              break;
            }
          }
        }

      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      Class<?> cls;
      try {
        cls = cl.loadClass(candiadate);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
      if (!doExclude) {
        classes.add(cls);
      }
    }
  }
}
