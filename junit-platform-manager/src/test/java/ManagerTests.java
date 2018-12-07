import de.sormuras.junit.platform.manager.Configuration;
import de.sormuras.junit.platform.manager.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagerTests {
  @Test
  void classLoaderSeparation() {
    Manager manager = new Manager(new Configuration.Default().setDryRun(true));
    Assertions.assertEquals(Integer.valueOf(0), manager.call());
  }
}
