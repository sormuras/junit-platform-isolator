import static org.junit.jupiter.api.Assertions.assertTrue;

import de.sormuras.junit.platform.manager.JUnitPlatformManager;
import org.junit.jupiter.api.Test;

class JUnitPlatformManagerTests {

  @Test
  void settingDryRun() {
    var manager = new JUnitPlatformManager(new NoopDriver());

    assertTrue(manager.getClass().getSimpleName().contains("Manager"));
  }
}
