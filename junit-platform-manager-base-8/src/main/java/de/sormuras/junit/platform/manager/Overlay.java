package de.sormuras.junit.platform.manager;

import java.net.URL;
import java.net.URLClassLoader;

/** Java version-aware helpers. */
public interface Overlay {

  /** Create new URLClassLoader instance. */
  URLClassLoader newClassLoader(String name, ClassLoader parent, URL... urls);

  /**
   * Get the {@link ClassLoader} providing access to just the classes of the runtime Java platform.
   */
  ClassLoader platformClassLoader();
}
