package de.sormuras.junit.platform.manager;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import org.junit.jupiter.api.Test;

class OverlayTests {
  @Test
  void newClassLoaderWithoutUrl() {
    var parent = getClass().getClassLoader();
    var loader = OverlaySingleton.INSTANCE.newClassLoader("name", parent);
    assertEquals("name", loader.getName());
    assertArrayEquals(new URL[0], loader.getURLs());
  }
}
