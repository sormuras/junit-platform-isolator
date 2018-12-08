package basic;

import de.sormuras.junit.platform.manager.Manager;

class Application {

  private final Manager manager = new Manager();

  boolean isDryRun() {
    return manager.isDryRun();
  }
}
