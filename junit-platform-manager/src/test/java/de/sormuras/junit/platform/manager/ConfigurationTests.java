package de.sormuras.junit.platform.manager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConfigurationTests {
  @Test
  void accessPackagePrivateProperty() {
    Configuration.Default configuration = new Configuration.Default();
    configuration.dryRun = false;
    assertFalse(configuration.isDryRun());
    configuration.dryRun = true;
    assertTrue(configuration.isDryRun());
  }
}
