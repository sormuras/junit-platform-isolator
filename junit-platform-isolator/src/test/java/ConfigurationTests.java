import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import de.sormuras.junit.platform.isolator.Configuration;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class ConfigurationTests {

  private static void assertEmpty(Collection<?> collection) {
    assertTrue(collection.isEmpty());
  }

  @TestFactory
  DynamicTest[] checkAllDefaultValues() {
    var config = new Configuration();

    return new DynamicTest[] {
      dynamicTest("dryRun", () -> assertFalse(config.isDryRun())),
      dynamicTest("parameters", () -> assertEmpty(config.getParameters().keySet())),
      dynamicTest("selectedClassPathRoots", () -> assertEmpty(config.getSelectedClassPathRoots()))
    };
  }

  @Test
  void settingDryRun() {
    var configuration = new Configuration().setDryRun(true);
    assertTrue(configuration.isDryRun());
  }

  @Test
  void serialization() {
    var configuration = new Configuration();
    configuration.setDryRun(true);
    configuration.setParameters(Map.of("a", "b"));
    configuration.setSelectedClassPathRoots(Set.of("a/b"));

    var bytes = configuration.toBytes();
    var second = Configuration.fromBytes(bytes);
    assertEquals(configuration, second);
  }
}
