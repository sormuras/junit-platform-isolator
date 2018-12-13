import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.sormuras.junit.platform.isolator.Configuration;
import de.sormuras.junit.platform.isolator.Isolator;
import org.junit.jupiter.api.Test;

class IsolatorTests {

  @Test
  void implementationVersion() {
    var version = Isolator.class.getPackage().getImplementationVersion();
    var expected = version != null ? version : "4711";
    assertEquals(expected, Isolator.implementationVersion("4711"));
  }

  @Test
  void noopWorksUsingThreadContextClassLoader() throws ReflectiveOperationException {
    var configuration = new Configuration();
    configuration.basic().dryRun = true;
    configuration.basic().workerClassName = "NoopWorker";
    configuration.basic().platformClassLoader = false;
    var isolator = new Isolator(new NoopDriver());

    assertEquals(0, isolator.evaluate(configuration));
  }

  @Test
  void noopThrowsWithPlatformClassLoader() {
    var configuration = new Configuration();
    configuration.basic().dryRun = true;
    configuration.basic().workerClassName = "NoopWorker";
    configuration.basic().platformClassLoader = true;
    var isolator = new Isolator(new NoopDriver());

    assertThrows(ReflectiveOperationException.class, () -> isolator.evaluate(configuration));
  }
}
