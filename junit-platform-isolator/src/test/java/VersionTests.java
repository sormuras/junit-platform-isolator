import static org.junit.jupiter.api.Assertions.assertEquals;

import de.sormuras.junit.platform.isolator.Version;
import org.junit.jupiter.api.Test;

class VersionTests {

  @Test
  void implementationVersion() {
    var version = Version.class.getPackage().getImplementationVersion();
    var expected = version != null ? version : "4711";
    assertEquals(expected, Version.implementationVersion("4711"));
  }
}
