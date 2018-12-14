package de.sormuras.junit.platform.isolator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Collection;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;

class ConfigurationTests {

  private static void assertEmpty(Collection<?> collection) {
    assertTrue(collection.isEmpty());
  }

  @TestFactory
  DynamicNode[] checkAllDefaultValues() {
    Configuration configuration = new Configuration();
    Configuration.Basic basic = configuration.basic();
    Configuration.Discovery discovery = configuration.discovery();
    Configuration.Launcher launcher = configuration.launcher();

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
}
