package de.sormuras.junit.platform.isolator;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Stream;

public interface Driver {

  /** Log a message formatted in {@link java.text.MessageFormat} style at debug level. */
  void debug(String format, Object... args);

  /** Log a message formatted in {@link java.text.MessageFormat} style at info level. */
  void info(String format, Object... args);

  /** Log a message formatted in {@link java.text.MessageFormat} style at warning level. */
  void warn(String format, Object... args);

  /** Log a message formatted in {@link java.text.MessageFormat} style at error level. */
  void error(String format, Object... args);

  /** Iterate all relevant lines and let the passed consumer handle each. */
  default void parseModuleInfoTestLines(Path moduleInfoTestPath, Consumer<String> consumeLine) {
    try (Stream<String> lines = Files.lines(moduleInfoTestPath)) {
      lines
          .map(String::trim)
          .filter(line -> !line.isEmpty())
          .filter(line -> !line.startsWith("//"))
          .forEach(consumeLine);
    } catch (IOException e) {
      throw new UncheckedIOException("Reading " + moduleInfoTestPath + " failed", e);
    }
  }
}
