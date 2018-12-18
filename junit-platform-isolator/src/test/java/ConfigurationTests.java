import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import de.sormuras.junit.platform.isolator.Configuration;
import de.sormuras.junit.platform.isolator.ConfigurationBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class ConfigurationTests {

  @TestFactory
  DynamicNode[] checkAllDefaultValues() {
    Configuration configuration = new Configuration();
    Configuration.Basic basic = configuration.basic();
    Configuration.Discovery discovery = configuration.discovery();
    Configuration.Launcher launcher = configuration.launcher();

    return new DynamicNode[] {
      // basic
      dynamicTest("dryRun", () -> assertFalse(basic.isDryRun())),
      dynamicTest("failIfNoTests", () -> assertTrue(basic.isFailIfNoTests())),
      dynamicTest("platformClassLoader", () -> assertTrue(basic.isPlatformClassLoader())),
      dynamicTest("defaultAssertionStatus", () -> assertTrue(basic.isDefaultAssertionStatus())),
      dynamicTest(
          "targetDirectory",
          () -> assertEquals("target/junit-platform", basic.getTargetDirectory())),
      dynamicTest("workerClassName", () -> assertFalse(basic.getWorkerClassName().isEmpty())),
      dynamicTest("workerCoordinates", () -> assertFalse(basic.getWorkerCoordinates().isEmpty())),
      // discovery
      dynamicTest("parameters", () -> assertEmpty(discovery.getParameters().keySet())),
      // discovery - selectors
      dynamicTest("uris", () -> assertEmpty(discovery.getSelectedUris())),
      dynamicTest("files", () -> assertEmpty(discovery.getSelectedFiles())),
      dynamicTest("directories", () -> assertEmpty(discovery.getSelectedDirectories())),
      dynamicTest("packages", () -> assertEmpty(discovery.getSelectedPackages())),
      dynamicTest("classes", () -> assertEmpty(discovery.getSelectedClasses())),
      dynamicTest("methods", () -> assertEmpty(discovery.getSelectedMethods())),
      dynamicTest(
          "classpath/resources", () -> assertEmpty(discovery.getSelectedClasspathResources())),
      dynamicTest("classpath/roots", () -> assertEmpty(discovery.getSelectedClasspathRoots())),
      dynamicTest("modules", () -> assertEmpty(discovery.getSelectedModules())),
      // discovery - filters
      dynamicTest(
          "classNamePatterns",
          () -> assertEquals(1, discovery.getFilterClassNamePatterns().size())),
      dynamicTest("tags", () -> assertEmpty(discovery.getFilterTags())),
      // launcher
      dynamicTest("engine", () -> assertTrue(launcher.isTestEngineAutoRegistration())),
      dynamicTest("listener", () -> assertTrue(launcher.isTestExecutionListenerAutoRegistration()))
    };
  }

  @Test
  void serialization() {
    var configuration =
        new ConfigurationBuilder()
            .discovery()
            .addSelectedUris(Set.of(URI.create("https://junit.org")))
            .addSelectedUris(URI.create("https://junit.org"))
            .addSelectedFiles(Set.of("a"))
            .addSelectedFiles("b")
            .setSelectedDirectories(Set.of("a", "b"))
            .setSelectedPackages(Set.of(""))
            .setSelectedClasspathRoots(Set.of("a/b"))
            .setFilterTags(Set.of("x | !y"))
            .setParameters(Map.of("a", "b"))
            .end()
            .setDryRun(true)
            .build();

    var bytes = configuration.toBytes();
    var second = Configuration.fromBytes(bytes);
    assertEquals(configuration, second);
  }

  private static void assertEmpty(Collection<?> collection) {
    assertTrue(collection.isEmpty());
  }
}
