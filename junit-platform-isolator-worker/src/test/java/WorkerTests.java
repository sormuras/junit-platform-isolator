import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.sormuras.junit.platform.isolator.Configuration;
import de.sormuras.junit.platform.isolator.worker.Worker;
import org.junit.jupiter.api.Test;

class WorkerTests {

  private final Configuration configuration = new Configuration();

  @Test
  void defaultWorkerClassNameIsLoadable() {
    assertEquals(Worker.class.getName(), configuration.getWorkerClassName());
  }

  @Test
  void constructors() {
    Worker worker1 = new Worker(configuration, (_1, _2) -> {});
    Worker worker2 = new Worker(configuration.toBytes(), (_1, _2) -> {});

    assertSame(configuration, worker1.getConfiguration());
    assertEquals(configuration, worker2.getConfiguration());
  }
}
