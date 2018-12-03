package basic;

import de.sormuras.junit.platform.manager.Configuration;

class Application {

  private final Configuration configuration = new Configuration.Default();

  boolean isDryRun() {
    return configuration.isDryRun();
  }
}
