package de.sormuras.junit.platform.manager;

import static java.lang.System.Logger.Level.DEBUG;
import static java.lang.System.Logger.Level.INFO;
import static java.lang.System.Logger.Level.WARNING;

import java.net.URL;
import java.net.URLClassLoader;

enum OverlaySingleton implements Overlay, Overlay.Log {
  INSTANCE {
    private final System.Logger logger = System.getLogger(Overlay.class.getPackage().getName());

    @Override
    public void debug(String format, Object... args) {
      logger.log(DEBUG, format, args);
    }

    @Override
    public void info(String format, Object... args) {
      logger.log(INFO, format, args);
    }

    @Override
    public void warn(String format, Object... args) {
      logger.log(WARNING, format, args);
    }

    @Override
    public URLClassLoader newClassLoader(String name, ClassLoader parent, URL... urls) {
      return new URLClassLoader(name, urls, parent);
    }
  }
}
