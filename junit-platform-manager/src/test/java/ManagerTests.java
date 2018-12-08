import de.sormuras.junit.platform.manager.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagerTests {
  @Test
  void classLoaderSeparation() {
    Manager manager = new Manager();
    manager.accept("dry-run(boolean)", true);
    Assertions.assertTrue(manager.isDryRun());
    Assertions.assertEquals(Integer.valueOf(0), manager.call());
  }
}
