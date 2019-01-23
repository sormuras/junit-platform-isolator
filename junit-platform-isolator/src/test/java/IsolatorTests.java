import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.sormuras.junit.platform.isolator.ConfigurationBuilder;
import de.sormuras.junit.platform.isolator.Isolator;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class IsolatorTests {

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

  @Test
  void writeConfigurationLog() throws Exception {
    Path temp = Files.createTempDirectory("isolator-configuration-");

    var isolator = new Isolator(new NoopDriver());
    var configuration =
        configureNoop().setPlatformClassLoader(false).setTargetDirectory(temp.toString()).build();

    assertEquals(0, isolator.evaluate(configuration));

    Path conf = temp.resolve("junit-platform-configuration.txt");
    assertTrue(Files.isReadable(conf));
  }

  private static ConfigurationBuilder configureNoop() {
    return new ConfigurationBuilder()
        .setDryRun(true)
        .setWorkerIsolationRequired(false)
        .setWorkerClassName("NoopWorker");
  }
}
