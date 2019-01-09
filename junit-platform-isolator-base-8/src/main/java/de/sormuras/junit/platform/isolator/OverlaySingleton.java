package de.sormuras.junit.platform.isolator;

import java.net.URL;
import java.net.URLClassLoader;

public enum OverlaySingleton implements Overlay {
  INSTANCE {
    @Override
    public URLClassLoader newClassLoader(String __, ClassLoader parent, URL... urls) {
      return new URLClassLoader(urls, parent);
    }

    @Override
    public ClassLoader newModuleLoader(Driver _d, Configuration _c, ClassLoader _p) {
      throw new UnsupportedOperationException("Module system is not available!");
    }

    @Override
    public ClassLoader platformClassLoader() {
      URLClassLoader platformClassLoader = new URLClassLoader(new URL[0], null);
      try {
        platformClassLoader.close();
      } catch (Exception e) {
        throw new AssertionError("Closing an empty URLClassLoader failed?!", e);
      }
      return platformClassLoader;
    }
  }
}
