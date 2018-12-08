import de.sormuras.junit.platform.manager.Configuration;
import de.sormuras.junit.platform.manager.library.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagerTests {

  @Test
  void defaultConfiguration() {
    Manager manager = new Manager();
    Assertions.assertFalse(manager.getConfiguration().isDryRun());
  }

  @Test
  void dryRunNeverFails() {
    Configuration configuration = new Configuration.Default().setDryRun(true);
    Manager manager = new Manager(configuration);
    Assertions.assertTrue(configuration.isDryRun());
    Assertions.assertEquals(Integer.valueOf(0), manager.call());
  }
}
