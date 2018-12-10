import de.sormuras.junit.platform.isolator.Configuration;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

public class NoopWorker implements Callable<Integer> {

  public NoopWorker(byte[] bytes, BiConsumer<String, String> log) {
    Configuration.fromBytes(bytes);
  }

  @Override
  public Integer call() {
    return 0;
  }
}
