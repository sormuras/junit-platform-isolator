import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.sormuras.junit.platform.isolator.ConfigurationBuilder;
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
    var isolator = new Isolator(new NoopDriver());
    var configuration = configureNoop().setPlatformClassLoader(false).build();

    assertEquals(0, isolator.evaluate(configuration));
  }

  @Test
  void noopThrowsWithPlatformClassLoader() {
    var isolator = new Isolator(new NoopDriver());
    var configuration = configureNoop().setPlatformClassLoader(true).build();

    assertThrows(ReflectiveOperationException.class, () -> isolator.evaluate(configuration));
  }

  private static ConfigurationBuilder configureNoop() {
    return new ConfigurationBuilder().setDryRun(true).setWorkerClassName("NoopWorker");
  }
}
