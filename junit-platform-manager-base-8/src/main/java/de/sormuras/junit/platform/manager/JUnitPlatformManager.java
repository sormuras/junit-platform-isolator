package de.sormuras.junit.platform.manager;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

public class JUnitPlatformManager {

  private static final Overlay overlay = OverlaySingleton.INSTANCE;

  private final JUnitPlatformDriver driver;

  public JUnitPlatformManager(JUnitPlatformDriver driver) {
    this.driver = driver;
  }

  private ClassLoader createClassLoader() {
    ClassLoader loader = overlay.platformClassLoader();
    for (Map.Entry<String, Set<Path>> entry : driver.paths().entrySet()) {
      String name = entry.getKey();
      Set<Path> paths = entry.getValue();
      driver.info("entry = {0}", entry);
      loader = overlay.newClassLoader(name, loader, paths);
      loader.setDefaultAssertionStatus(true);
    }
    return loader;
  }

  public int evaluate(Configuration configuration) throws ReflectiveOperationException {
    ClassLoader loader = createClassLoader();
    Thread.currentThread().setContextClassLoader(loader);
    driver.debug("Isolator created loader = {0}", loader);

    // Worker
    Class<?> workerClass = Class.forName(configuration.getWorkerClassName(), true, loader);
    ClassLoader workerLoader = workerClass.getClassLoader();
    if (workerLoader != loader) {
      driver.warn("{0} was not loaded in isolation: {1}", workerClass, workerLoader);
    }

    Constructor<?> constructor = workerClass.getConstructor(byte[].class, BiConsumer.class);
    byte[] bytes = configuration.toBytes();
    BiConsumer<String, String> log = this::log;
    @SuppressWarnings("unchecked")
    Callable<Integer> worker = (Callable<Integer>) constructor.newInstance(bytes, log);
    driver.debug("Instantiated {0}", worker);

    try {
      return worker.call();
    } catch (Exception e) {
      driver.warn("Calling worker failed: {0}", e.getMessage());
      throw new RuntimeException("Calling worker failed!", e);
    }
  }

  private void log(String level, String message) {
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
