import static org.junit.jupiter.api.Assertions.assertEquals;

import de.sormuras.junit.platform.isolator.ConfigurationBuilder;
import java.util.Map;
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
        .discovery()
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
            + "workerCoordinates='local:worker:version', "
            + "workerClassName='NoopWorker'"
            + "], "
            + "discovery=Discovery["
            + "selectedClasspathRoots=[], "
            + "filterTagsIncluded=[], "
            + "parameters={smoke=test}"
            + "], "
            + "launcher=Launcher["
            + "testEngineAutoRegistration=false, "
            + "testExecutionListenerAutoRegistration=false"
            + "]"
            + "}";
    assertEquals(expected, configuration.toString());
  }
}
