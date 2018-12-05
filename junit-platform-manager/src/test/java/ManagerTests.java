import de.sormuras.junit.platform.manager.Manager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

class ManagerTests {
  @Test
  void classLoaderSeparation() {
    Manager manager = new Manager();

    TestPlan plan = manager.discover(LauncherDiscoveryRequestBuilder.request().build());
    Assertions.assertFalse(plan.getRoots().isEmpty());
  }
}
