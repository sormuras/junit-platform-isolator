import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.sormuras.junit.platform.manager.Configuration;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class ConfigurationTests {

  @TestFactory
  DynamicTest[] checkAllDefaultValues() {
    Configuration configuration = new Configuration.Default();

    return new DynamicTest[] {
      DynamicTest.dynamicTest("dryRun", () -> assertFalse(configuration.isDryRun()))
    };
  }

  @Test
  void settingDryRun() {
    Configuration configuration = new Configuration.Default().setDryRun(true);
    assertTrue(configuration.isDryRun());
  }
}
