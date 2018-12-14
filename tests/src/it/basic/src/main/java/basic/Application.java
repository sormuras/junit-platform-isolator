package basic;

import de.sormuras.junit.platform.isolator.Configuration;

class Application {

  private final Configuration configuration = new Configuration();

  boolean isDryRun() {
    return configuration.basic().isDryRun();
  }
}
