import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.sormuras.junit.platform.manager.Configuration;
import de.sormuras.junit.platform.manager.worker.Worker;
import org.junit.jupiter.api.Test;

class WorkerTests {

  @Test
  void constructors() {
    Configuration configuration = new Configuration();

    Worker worker1 = new Worker(configuration);
    Worker worker2 = new Worker(configuration.toBytes());

    assertSame(configuration, worker1.getConfiguration());
    assertEquals(configuration, worker2.getConfiguration());
  }
}
