import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.sormuras.junit.platform.isolator.Configuration;
import de.sormuras.junit.platform.isolator.Isolator;
import org.junit.jupiter.api.Test;

class IsolatorTests {

  @Test
  void noopWorksUsingThreadContextClassLoader() throws ReflectiveOperationException {
    var configuration =
        new Configuration()
            .setDryRun(true)
            .setWorkerClassName("NoopWorker")
            .setPlatformClassLoader(false);
    var isolator = new Isolator(new NoopDriver());

    var result = isolator.evaluate(configuration);
    assertEquals(0, result);
  }

  @Test
  void noopThrowsWithPlatformClassLoader() {
    var configuration =
        new Configuration()
            .setDryRun(true)
            .setWorkerClassName("NoopWorker")
            .setPlatformClassLoader(true);
    var isolator = new Isolator(new NoopDriver());

    assertThrows(ReflectiveOperationException.class, () -> isolator.evaluate(configuration));
  }
}
