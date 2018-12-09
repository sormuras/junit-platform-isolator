package basic;

import de.sormuras.junit.platform.manager.Configuration;

class Application {

  private final Configuration configuration = new Configuration();

  boolean isDryRun() {
    return configuration.isDryRun();
  }
}
