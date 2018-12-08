package de.sormuras.junit.platform.manager.library;

import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

enum OverlaySingleton implements Overlay, Overlay.Log {
  INSTANCE {
    private final Logger logger = Logger.getLogger(Overlay.class.getPackage().getName());

    @Override
    public void debug(String format, Object... args) {
      logger.log(Level.FINE, () -> MessageFormat.format(format, args));
    }

    @Override
    public void info(String format, Object... args) {
      logger.log(Level.INFO, () -> MessageFormat.format(format, args));
    }

    @Override
    public void warn(String format, Object... args) {
      logger.log(Level.WARNING, () -> MessageFormat.format(format, args));
    }

    @Override
    public URLClassLoader newClassLoader(String __, ClassLoader parent, URL... urls) {
      return new URLClassLoader(urls, parent);
    }

    @Override
    public ClassLoader platformClassLoader() {
      URLClassLoader platformClassLoader = new URLClassLoader(new URL[0], null);
      try {
        platformClassLoader.close();
      } catch (Exception e) {
        warn("Closing an empty URLClassLoader failed?!", e);
      }
      return platformClassLoader;
    }
  }
}
