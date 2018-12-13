import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import de.sormuras.junit.platform.isolator.Configuration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class ConfigurationTests {

  private static void assertEmpty(Collection<?> collection) {
    assertTrue(collection.isEmpty());
  }

  @TestFactory
  DynamicNode[] checkAllDefaultValues() {
    var configuration = new Configuration();
    var basic = configuration.basic();
    var discovery = configuration.discovery();
    var launcher = configuration.launcher();

    return new DynamicNode[] {
      // basic
      dynamicTest("dryRun", () -> assertFalse(basic.dryRun)),
      dynamicTest("failIfNoTests", () -> assertTrue(basic.failIfNoTests)),
      dynamicTest("workerClassName", () -> assertFalse(basic.workerClassName.isEmpty())),
      dynamicTest("workerCoordinates", () -> assertFalse(basic.workerCoordinates.isEmpty())),
      dynamicTest("defaultAssertionStatus", () -> assertTrue(basic.defaultAssertionStatus)),
      // discovery
      dynamicTest("parameters", () -> assertEmpty(discovery.parameters.keySet())),
      dynamicTest("selectedClasspathRoots", () -> assertEmpty(discovery.selectedClasspathRoots)),
      dynamicTest("filterTagsIncluded", () -> assertEmpty(discovery.filterTagsIncluded)),
      // launcher
      dynamicTest("engine", () -> assertTrue(launcher.testEngineAutoRegistration)),
      dynamicTest("listener", () -> assertTrue(launcher.testExecutionListenerAutoRegistration))
    };
  }

  @Test
  void serialization() {
    var configuration = new Configuration();
    configuration.discovery().parameters = Map.of("a", "b");
    configuration.discovery().selectedClasspathRoots = Set.of("a/b");
    configuration.discovery().filterTagsIncluded = List.of("x | !y");

    var bytes = configuration.toBytes();
    var second = Configuration.fromBytes(bytes);
    assertEquals(configuration, second);
  }
}
