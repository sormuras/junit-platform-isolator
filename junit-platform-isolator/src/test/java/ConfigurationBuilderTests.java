import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import de.sormuras.junit.platform.isolator.ConfigurationBuilder;
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
        .setSelectedClasspathRoots(Set.of("target/test-classes"))
        .setSelectedModules(Set.of("java.base"))
        .setSelectedPackages(Set.of("java.lang"))
        .setFilterTagsIncluded(List.of("fast"))
        .setFilterTagsExcluded(List.of("slow"))
        .setParameters(Map.of("smoke", "test"))
        .end()
        .setWorkerCoordinates("local:worker:version")
        .setWorkerClassName("NoopWorker")
        .launcher()
        .setTestEngineAutoRegistration(false)
        .setTestExecutionListenerAutoRegistration(false)
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
            + "workerClassName='NoopWorker'"
            + "], "
            + "discovery=Discovery["
            + "selectedClasspathRoots=[target/test-classes], "
            + "selectedModules=[java.base], "
            + "selectedPackages=[java.lang], "
            + "filterTagsIncluded=[fast], "
            + "filterTagsExcluded=[slow], "
            + "parameters={smoke=test}"
            + "], "
            + "launcher=Launcher["
            + "testEngineAutoRegistration=false, "
            + "testExecutionListenerAutoRegistration=false"
            + "]"
            + "}";
    assertLinesMatch(lines(expected), lines(configuration.toString()));
  }

  private static List<String> lines(String string) {
    return List.of(string.split(" "));
  }
}
