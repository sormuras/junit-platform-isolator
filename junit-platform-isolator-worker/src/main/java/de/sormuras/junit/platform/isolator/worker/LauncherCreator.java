package de.sormuras.junit.platform.isolator.worker;

import de.sormuras.junit.platform.isolator.Configuration;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherConfig;
import org.junit.platform.launcher.core.LauncherFactory;

class LauncherCreator {

  Launcher create(Configuration.Launcher configuration) {
    LauncherConfig.Builder builder = LauncherConfig.builder();
    builder.enableTestEngineAutoRegistration(configuration.testEngineAutoRegistration);
    builder.enableTestExecutionListenerAutoRegistration(
        configuration.testExecutionListenerAutoRegistration);
    return LauncherFactory.create(builder.build());
  }
}
