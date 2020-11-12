import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import de.sormuras.junit.platform.isolator.ConfigurationBuilder;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConfigurationBuilderTests {

  @Test
  void touchAllSetters() {
    var builder = new ConfigurationBuilder();

    builder
        .setDryRun(true)
        .setDefaultAssertionStatus(false)
        .setFailIfNoTests(false)
        .setPlatformClassLoader(false)
        .setTargetDirectory("bin/junit")
        .discovery()
        .setSelectedUris(Set.of(URI.create("https://junit.org")))
        .setSelectedFiles(Set.of("file"))
        .setSelectedDirectories(Set.of("directory"))
        .setSelectedPackages(Set.of("java.lang"))
        .setSelectedClasses(Set.of("java.lang.Object"))
        .setSelectedMethods(Set.of("java.lang.Object#toString()"))
        .setSelectedClasspathResources(Set.of("META-INF/MANIFEST.MF"))
        .setSelectedClasspathRoots(Set.of("target/test-classes"))
        .setSelectedModules(Set.of("java.base"))
        .setFilterClassNamePatterns(Set.of(".*"))
        .setFilterTags(Set.of("fast"))
        .setParameters(Map.of("smoke", "test"))
        .end()
        .setWorkerCoordinates("local:worker:version")
        .setWorkerClassName("NoopWorker")
        .setWorkerIsolationRequired(false)
        .setPaths(Map.of("test", Set.of("abc")))
        .setTargetMainPath("main")
        .setTargetTestPath("test")
        .setModuleInfoTestPath("info.test")
        .launcher()
        .setTestEngineAutoRegistration(false)
        .setTestExecutionListenerAutoRegistration(false)
        .setAdditionalTestEngines(
            Collections.singletonList("de.sormuras.junit.platform.CustomTestEngine"))
        .end()
        .setDryRun(false);

    var configuration = builder.build();

    var expected =
        "Configuration{"
            + "basic=Basic["
            + "dryRun=false, "
            + "failIfNoTests=false, "
            + "platformClassLoader=false, "
            + "defaultAssertionStatus=false, "
            + "targetDirectory='bin/junit', "
            + "workerCoordinates='local:worker:version', "
            + "workerClassName='NoopWorker', "
            + "workerIsolationRequired=false, "
            + "paths={test=[abc]}, "
            + "targetMainPath='main', "
            + "targetTestPath='test', "
            + "moduleInfoTestPath='info.test'"
            + "], "
            + "discovery=Discovery["
            + "selectedUris=[https://junit.org], "
            + "selectedFiles=[file], "
            + "selectedDirectories=[directory], "
            + "selectedPackages=[java.lang], "
            + "selectedClasses=[java.lang.Object], "
            + "selectedMethods=[java.lang.Object#toString()], "
            + "selectedClasspathResources=[META-INF/MANIFEST.MF], "
            + "selectedClasspathRoots=[target/test-classes], "
            + "selectedModules=[java.base], "
            + "filterClassNamePatterns=[.*], "
            + "filterTags=[fast], "
            + "parameters={smoke=test}"
            + "], "
            + "launcher=Launcher["
            + "testEngineAutoRegistration=false, "
            + "testExecutionListenerAutoRegistration=false, "
            + "additionalTestEngines=[de.sormuras.junit.platform.CustomTestEngine]"
            + "]"
            + "}";
    assertLinesMatch(lines(expected), lines(configuration.toString()));
  }

  private static List<String> lines(String string) {
    return List.of(string.split(" "));
  }
}
