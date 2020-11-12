package de.sormuras.junit.platform.isolator.worker;

import de.sormuras.junit.platform.isolator.Configuration;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherFactory;

class LauncherCreator {

  Launcher create(Configuration.Launcher configuration) {
    // TODO Guard against JUnit Platform version < 1.3 ... or drop support for 1.0, 1.1 and 1.2?
    //      https://github.com/sormuras/junit-platform-isolator/issues/8
    try {
      Class.forName("org.junit.platform.launcher.core.LauncherConfig");
    } catch (ClassNotFoundException e) {
      return LauncherFactory.create();
    }
    LauncherConfig.Builder builder = LauncherConfig.builder();
    builder.enableTestEngineAutoRegistration(configuration.isTestEngineAutoRegistration());
    builder.enableTestExecutionListenerAutoRegistration(
        configuration.isTestExecutionListenerAutoRegistration());
    if (configuration.getAdditionalTestEngines() != null) {
      builder.addTestEngines(toArray(instantiateClasses(configuration.getAdditionalTestEngines())));
    }
    return LauncherFactory.create(builder.build());
  }

  private TestEngine[] toArray(Collection<TestEngine> collection) {
    return collection.toArray(new TestEngine[collection.size()]);
  }

  private Collection<TestEngine> instantiateClasses(final Collection<String> classes) {
    return classes.stream()
        .map(this::classToInstance)
        .collect(Collectors.toCollection(ArrayList::new));
  }

  private TestEngine classToInstance(final String clazz) {
    try {
      Class<?> aClass = Class.forName(clazz);
      Constructor<?> constructor = aClass.getConstructor();
      return (TestEngine) constructor.newInstance();
    } catch (ClassNotFoundException e) {
      throw new IllegalArgumentException(e);
    } catch (InvocationTargetException
        | NoSuchMethodException
        | IllegalAccessException
        | InstantiationException e) {
      throw new IllegalStateException(e);
    }
  }
}
