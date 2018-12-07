package de.sormuras.junit.platform.manager;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URL;
import java.net.URLClassLoader;
import org.junit.jupiter.api.Test;

class OverlayTests {

  private final Overlay overlay = OverlaySingleton.INSTANCE;

  @Test
  void newClassLoaderWithoutUrl() {
    ClassLoader parent = getClass().getClassLoader();
    URLClassLoader loader = overlay.newClassLoader("-", parent);
    assertArrayEquals(new URL[0], loader.getURLs());
  }

  @Test
  void platformClassLoaderIsNotNull() {
    assertNotNull(overlay.platformClassLoader());
  }

  @Test
  void platformClassLoaderSeesJavaLangObject() {
    assertDoesNotThrow(() -> overlay.platformClassLoader().loadClass("java.lang.Object"));
  }

  @Test
  void platformClassLoaderCantLoadTestAnnotation() {
    Class<? extends Exception> expected = ClassNotFoundException.class;
    ClassLoader loader = overlay.platformClassLoader();

    assertThrows(expected, () -> loader.loadClass(Test.class.getName()));
    assertThrows(expected, () -> Class.forName(Test.class.getName(), true, loader));
  }
}
