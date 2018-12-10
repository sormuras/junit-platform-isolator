package de.sormuras.junit.platform.manager;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public interface JUnitPlatformDriver {

  /** Log a message formatted in {@link java.text.MessageFormat} style at debug level. */
  void debug(String format, Object... args);

  /** Log a message formatted in {@link java.text.MessageFormat} style at info level. */
  void info(String format, Object... args);

  /** Log a message formatted in {@link java.text.MessageFormat} style at warning level. */
  void warn(String format, Object... args);

  /** Named paths sets. */
  Map<String, Set<Path>> paths();

  /** Determine string for the supplied version constant. */
  String version(Version version);
}
