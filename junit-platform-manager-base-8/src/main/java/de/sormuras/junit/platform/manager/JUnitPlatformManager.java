package de.sormuras.junit.platform.manager;

import static de.sormuras.junit.platform.manager.GroupArtifact.JUNIT_JUPITER_API;
import static de.sormuras.junit.platform.manager.GroupArtifact.JUNIT_JUPITER_ENGINE;
import static de.sormuras.junit.platform.manager.GroupArtifact.JUNIT_PLATFORM_LAUNCHER;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;

public class JUnitPlatformManager {

  private static final Overlay overlay = OverlaySingleton.INSTANCE;

  private final JUnitPlatformDriver driver;

  public JUnitPlatformManager(JUnitPlatformDriver driver) {
    this.driver = driver;
  }

  public int evaluate(Configuration configuration) throws Exception {
    ClassLoader loader = isolate(configuration.getWorkerCoordinates());
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

  private ClassLoader isolate(String workerCoordinates) throws Exception {
    //
    // Main and Test source sets
    //
    Set<Path> mainPaths = driver.paths().get("main");
    Set<Path> testPaths = driver.paths().get("test");

    //
    // JUnit Platform Launcher and all TestEngine implementations
    //
    Set<Path> launcherPaths = new LinkedHashSet<>();
    if (driver.contains(JUNIT_JUPITER_API) && !driver.contains(JUNIT_JUPITER_ENGINE)) {
      launcherPaths.addAll(driver.resolve(JUNIT_JUPITER_ENGINE.toString(driver::version)));
    }
    if (!driver.contains(JUNIT_PLATFORM_LAUNCHER)) {
      launcherPaths.addAll(driver.resolve(JUNIT_PLATFORM_LAUNCHER.toString(driver::version)));
    }

    //
    // Worker + Manager
    //
    Set<Path> managerPaths = driver.resolve(workerCoordinates);

    ClassLoader rootLoader = overlay.platformClassLoader();
    ClassLoader mainLoader = overlay.newClassLoader("main", rootLoader, mainPaths);
    ClassLoader testLoader = overlay.newClassLoader("test", mainLoader, testPaths);
    ClassLoader launcherLoader = overlay.newClassLoader("junit", testLoader, launcherPaths);
    ClassLoader workerLoader = overlay.newClassLoader("worker", launcherLoader, managerPaths);

    testLoader.setDefaultAssertionStatus(true);

    return workerLoader;
  }
}
