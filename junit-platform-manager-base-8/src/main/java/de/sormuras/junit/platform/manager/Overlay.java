package de.sormuras.junit.platform.manager;

import java.net.URL;
import java.net.URLClassLoader;

/** Java version-aware helpers. */
interface Overlay {

  /** Create new URLClassLoader instance. */
  URLClassLoader newClassLoader(String name, ClassLoader parent, URL... urls);

  /**
   * Get the {@link ClassLoader} providing access to just the classes of the runtime Java platform.
   */
  ClassLoader platformClassLoader();

  /** Log-related methods. */
  interface Log {

    /** Log a message formatted in {@link java.text.MessageFormat} style at debug level. */
    void debug(String format, Object... args);

    /** Log a message formatted in {@link java.text.MessageFormat} style at info level. */
    void info(String format, Object... args);

    /** Log a message formatted in {@link java.text.MessageFormat} style at warning level. */
    void warn(String format, Object... args);
  }
}
