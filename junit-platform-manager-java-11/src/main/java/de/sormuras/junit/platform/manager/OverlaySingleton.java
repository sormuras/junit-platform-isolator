package de.sormuras.junit.platform.manager;

import java.net.URL;
import java.net.URLClassLoader;

public enum OverlaySingleton implements Overlay {
  INSTANCE {
    @Override
    public URLClassLoader newClassLoader(String name, ClassLoader parent, URL... urls) {
      return new URLClassLoader(name, urls, parent);
    }

    @Override
    public ClassLoader platformClassLoader() {
      return ClassLoader.getPlatformClassLoader();
    }
  }
}
