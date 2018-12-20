package de.sormuras.junit.platform.isolator.worker;

import de.sormuras.junit.platform.isolator.Configuration;
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
    return LauncherFactory.create(builder.build());
  }
}
