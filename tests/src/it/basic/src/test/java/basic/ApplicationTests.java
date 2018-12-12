package basic;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.sormuras.junit.platform.isolator.Configuration;
import org.junit.jupiter.api.Test;

class ApplicationTests {

  @Test
  void checkDryRunFlag() {
    assertFalse(new Application().isDryRun());
  }

  @Test
  void checkImplementationVersionIsPresent() {
    String version = Configuration.class.getPackage().getImplementationVersion();
    assertNotNull(version);
  }
}
