package de.sormuras.junit.platform.isolator;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

public class Isolator {

  private static final Overlay overlay = OverlaySingleton.INSTANCE;

  private static final String WORKER_LOG_MESSAGE_PREFIX = "[WORKER] ";

  private final Driver driver;

  public Isolator(Driver driver) {
    this.driver = driver;
  }

  public int evaluate(Configuration configuration) throws ReflectiveOperationException {
    Configuration.Basic basic = configuration.basic();
    Thread thread = Thread.currentThread();
    ClassLoader contextClassLoader = thread.getContextClassLoader();

    // Write configuration to log file...
    Path targetDirectory = Paths.get(basic.getTargetDirectory());
    if (Files.isDirectory(targetDirectory)) {
      List<String> lines = Arrays.asList(configuration.toString().split(" "));
      try {
        Files.write(targetDirectory.resolve("junit-platform-configuration.txt"), lines);
      } catch (IOException e) {
        driver.warn("Writing configuration failed", e.getMessage());
      }
    }

    // Create isolated class loaders using driver's paths...
    ClassLoader loader = contextClassLoader;
    if (basic.isPlatformClassLoader()) {
      loader = overlay.platformClassLoader();
    }

    Set<String> selectedModules = configuration.discovery().getSelectedModules();
    if (selectedModules.isEmpty()) {
      driver.debug("Building non-modular classloader stack: {0} layers", driver.paths().size());
      for (Map.Entry<String, Set<Path>> entry : driver.paths().entrySet()) {
        String name = entry.getKey();
        Set<Path> paths = entry.getValue();
        loader = overlay.newClassLoader(name, loader, paths);
        loader.setDefaultAssertionStatus(basic.isDefaultAssertionStatus());
        driver.debug("Created loader named {0} (parent={1}): {2}", name, loader, paths);
      }
    } else {
      driver.debug("Selected test module(s) present: " + selectedModules);
      loader = overlay.newModuleLoader(driver, configuration, loader);
    }

    // Instantiate Worker passing configuration and other arguments...
    Class<?> workerClass = Class.forName(basic.getWorkerClassName(), true, loader);
    ClassLoader workerLoader = workerClass.getClassLoader();
    if (workerLoader != loader) {
      driver.warn("{0} was not loaded in isolation: {1}", workerClass, workerLoader);
      if (driver.isIllegalStateIfWorkerIsNotLoadedInIsolation()) {
        throw new IllegalStateException("Isolating worker failed!");
      }
    }
    Constructor<?> constructor = workerClass.getConstructor(byte[].class, BiConsumer.class);
    byte[] bytes = configuration.toBytes();
    BiConsumer<String, String> log = this::workerLogFunction;
    @SuppressWarnings("unchecked")
    Callable<Integer> worker = (Callable<Integer>) constructor.newInstance(bytes, log);
    driver.debug("Instantiated {0}", worker);

    // Let worker launch the JUnit Platform...
    try {
      thread.setContextClassLoader(loader);
      return worker.call();
    } catch (Exception e) {
      driver.error("Calling worker failed: {0}", e.getMessage());
      throw new RuntimeException("Calling worker failed!", e);
    } finally {
      thread.setContextClassLoader(contextClassLoader);
    }
  }

  private void workerLogFunction(String level, String message) {
    switch (level) {
      case "debug":
        driver.debug(WORKER_LOG_MESSAGE_PREFIX + message);
        return;
      case "info":
        driver.info(WORKER_LOG_MESSAGE_PREFIX + message);
        return;
      case "warn":
        driver.warn(WORKER_LOG_MESSAGE_PREFIX + message);
        return;
      case "error":
        driver.error(WORKER_LOG_MESSAGE_PREFIX + message);
        return;
      default:
        driver.warn(WORKER_LOG_MESSAGE_PREFIX + "[{0}] {1}", level, message);
    }
  }
}
