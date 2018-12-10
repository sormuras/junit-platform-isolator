package de.sormuras.junit.platform.manager;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collection;

/** Java version-aware helpers. */
public interface Overlay {

  /** Create new URLClassLoader instance. */
  default URLClassLoader newClassLoader(String name, ClassLoader parent, Collection<Path> paths) {
    URL[] urls = paths.stream().map(Path::toUri).map(Overlay::url).toArray(URL[]::new);
    return newClassLoader(name, parent, urls);
  }

  /** Create new URLClassLoader instance. */
  URLClassLoader newClassLoader(String name, ClassLoader parent, URL... urls);

  /**
   * Get the {@link ClassLoader} providing access to just the classes of the runtime Java platform.
   */
  ClassLoader platformClassLoader();

  static URL url(URI uri) {
    try {
      return uri.toURL();
    } catch (MalformedURLException e) {
      throw new UncheckedIOException("URI inconvertible to URL: " + uri, e);
    }
  }
}
