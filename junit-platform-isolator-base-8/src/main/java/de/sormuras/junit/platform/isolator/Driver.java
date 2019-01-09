package de.sormuras.junit.platform.isolator;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public interface Driver {

  /** Log a message formatted in {@link java.text.MessageFormat} style at debug level. */
  void debug(String format, Object... args);

  /** Log a message formatted in {@link java.text.MessageFormat} style at info level. */
  void info(String format, Object... args);

  /** Log a message formatted in {@link java.text.MessageFormat} style at warning level. */
  void warn(String format, Object... args);

  /** Log a message formatted in {@link java.text.MessageFormat} style at error level. */
  void error(String format, Object... args);

  /** Named paths sets. */
  Map<String, Set<Path>> paths();

  /** Modules helper. */
  Modules modules();

  /** If worker class could not be loaded in isolation: fail. */
  default boolean isIllegalStateIfWorkerIsNotLoadedInIsolation() {
    return true;
  }
}
