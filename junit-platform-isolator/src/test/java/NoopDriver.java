import de.sormuras.junit.platform.isolator.Driver;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

class NoopDriver implements Driver {
  @Override
  public void debug(String format, Object... args) {}

  @Override
  public void info(String format, Object... args) {}

  @Override
  public void warn(String format, Object... args) {}

  @Override
  public void error(String format, Object... args) {}

  @Override
  public Map<String, Set<Path>> paths() {
    return Collections.emptyMap();
  }

  @Override
  public boolean isIllegalStateIfWorkerIsNotLoadedInIsolation() {
    return false;
  }
}
