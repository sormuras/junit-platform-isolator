package de.sormuras.junit.platform.isolator;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

public class Isolator {

  private static final Overlay overlay = OverlaySingleton.INSTANCE;

  private final Driver driver;

  public Isolator(Driver driver) {
    this.driver = driver;
  }

  public int evaluate(Configuration configuration) throws ReflectiveOperationException {
    Thread thread = Thread.currentThread();
    ClassLoader contextClassLoader = thread.getContextClassLoader();

    // Create isolated class loaders using driver's paths...
    ClassLoader loader = contextClassLoader;
    if (configuration.isPlatformClassLoader()) {
      loader = overlay.platformClassLoader();
    }
    for (Map.Entry<String, Set<Path>> entry : driver.paths().entrySet()) {
      String name = entry.getKey();
      Set<Path> paths = entry.getValue();
      driver.debug("Creating loader named {0} (parent={1}): ", name, loader, paths);
      loader = overlay.newClassLoader(name, loader, paths);
      loader.setDefaultAssertionStatus(configuration.isDefaultAssertionStatus());
    }

    // Instantiate Worker passing configuration and other arguments...
    Class<?> workerClass = Class.forName(configuration.getWorkerClassName(), true, loader);
    ClassLoader workerLoader = workerClass.getClassLoader();
    if (workerLoader != loader) {
      driver.warn("{0} was not loaded in isolation: {1}", workerClass, workerLoader);
      throw new IllegalStateException("Isolating worker failed!");
    }
    Constructor<?> constructor = workerClass.getConstructor(byte[].class, BiConsumer.class);
    byte[] bytes = configuration.toBytes();
    BiConsumer<String, String> log = this::log;
    @SuppressWarnings("unchecked")
    Callable<Integer> worker = (Callable<Integer>) constructor.newInstance(bytes, log);
    driver.debug("Instantiated {0}", worker);

    // Let worker launch the JUnit Platform...
    try {
      thread.setContextClassLoader(loader);
      return worker.call();
    } catch (Exception e) {
      driver.warn("Calling worker failed: {0}", e.getMessage());
      throw new RuntimeException("Calling worker failed!", e);
    } finally {
      thread.setContextClassLoader(contextClassLoader);
    }
  }

  void log(String level, String message) {
    switch (level) {
      case "debug":
        driver.debug(message);
        return;
      case "info":
        driver.info(message);
        return;
      case "warn":
        driver.warn(message);
        return;
      default:
        driver.warn("[{0}] {1}", level, message);
    }
  }
}
