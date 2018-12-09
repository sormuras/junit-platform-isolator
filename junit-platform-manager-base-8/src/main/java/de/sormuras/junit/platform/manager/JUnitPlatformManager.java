package de.sormuras.junit.platform.manager;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

public class JUnitPlatformManager {

  private static final Log log = OverlaySingleton.INSTANCE;

  private final Isolator isolator;

  public JUnitPlatformManager(Isolator isolator) {
    this.isolator = isolator;
  }

  public int evaluate(Configuration configuration) throws Exception {
    ClassLoader loader = isolator.createClassLoader(configuration.getWorkerCoordinates());
    Thread.currentThread().setContextClassLoader(loader);
    log.debug("Isolator created loader = {0}", loader);

    // Worker
    Class<?> workerClass = Class.forName(configuration.getWorkerClassName(), true, loader);
    ClassLoader workerLoader = workerClass.getClassLoader();
    if (workerLoader != loader) {
      log.warn("{0} was not loaded in isolation: {1}", workerClass, workerLoader);
    }

    Constructor<?> constructor = workerClass.getConstructor(byte[].class);
    byte[] bytes = configuration.toBytes();
    @SuppressWarnings("unchecked")
    Callable<Integer> worker = (Callable<Integer>) constructor.newInstance((Object) bytes);
    log.info("Instantiated {0}", worker);

    try {
      return worker.call();
    } catch (Exception e) {
      log.warn("Calling worker failed: {0}", e.getMessage());
      throw new RuntimeException("Calling worker failed!", e);
    }
  }
}
