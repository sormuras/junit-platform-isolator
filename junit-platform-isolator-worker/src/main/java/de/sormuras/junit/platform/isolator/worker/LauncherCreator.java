package de.sormuras.junit.platform.isolator.worker;

import de.sormuras.junit.platform.isolator.Configuration;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.core.LauncherFactory;

class LauncherCreator {

  Launcher create(Configuration configuration) {
    return LauncherFactory.create();
  }
}
