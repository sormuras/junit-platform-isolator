import static org.junit.jupiter.api.Assertions.assertEquals;

import de.sormuras.junit.platform.isolator.Configuration;
import de.sormuras.junit.platform.isolator.ConfigurationBuilder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConfigurationTests {

  @Test
  void serialization() {
    var configuration =
        new ConfigurationBuilder()
            .discovery()
            .setSelectedClasspathRoots(Set.of("a/b"))
            .setFilterTagsIncluded(List.of("x | !y"))
            .setParameters(Map.of("a", "b"))
            .end()
            .setDryRun(true)
            .build();

    var bytes = configuration.toBytes();
    var second = Configuration.fromBytes(bytes);
    assertEquals(configuration, second);
  }
}
