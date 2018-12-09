import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import de.sormuras.junit.platform.manager.Configuration;
import java.util.Collection;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class ConfigurationTests {

  private static void assertEmpty(Collection<?> collection) {
    assertTrue(collection.isEmpty());
  }

  @TestFactory
  DynamicTest[] checkAllDefaultValues() {
    Configuration config = new Configuration();

    return new DynamicTest[] {
      dynamicTest("dryRun", () -> assertFalse(config.isDryRun())),
      dynamicTest("parameters", () -> assertEmpty(config.getParameters().keySet())),
      dynamicTest("selectedClassPathRoots", () -> assertEmpty(config.getSelectedClassPathRoots()))
    };
  }

  @Test
  void settingDryRun() {
    Configuration configuration = new Configuration().setDryRun(true);
    assertTrue(configuration.isDryRun());
  }
}
