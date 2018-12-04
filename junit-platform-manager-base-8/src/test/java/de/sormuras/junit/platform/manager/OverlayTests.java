package de.sormuras.junit.platform.manager;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.net.URL;
import java.net.URLClassLoader;
import org.junit.jupiter.api.Test;

class OverlayTests {
  @Test
  void newClassLoaderWithoutUrl() {
    ClassLoader parent = getClass().getClassLoader();
    URLClassLoader loader = OverlaySingleton.INSTANCE.newClassLoader("-", parent);
    assertArrayEquals(new URL[0], loader.getURLs());
  }
}
