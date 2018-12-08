package de.sormuras.junit.platform.manager.library;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;

import de.sormuras.junit.platform.manager.Configuration;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.Callable;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

public class Manager implements Callable<Integer> {

  private final Overlay.Log log = OverlaySingleton.INSTANCE;

  private final Configuration configuration;

  public Manager() {
    this(new Configuration.Default());
  }

  public Manager(Configuration configuration) {
    this.configuration = configuration;
    log.info("Manager created (loader={0})", getClass().getClassLoader());
  }

  @Override
  public Integer call() {
    Launcher launcher = LauncherFactory.create();
    LauncherDiscoveryRequest request = createRequest();
    Worker runner = new Worker(launcher, request);
    TestPlan testPlan = runner.discover();
    if (configuration.isDryRun()) {
      log.info("Dry-run.");
      return 0;
    }
    if (!testPlan.containsTests()) {
      log.warn("No test found.", testPlan.getRoots());
      return -1;
    }
    return runner.execute();
  }

  private LauncherDiscoveryRequest createRequest() {
    LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
    // selectors
    builder.selectors(selectClasspathRoots(configuration.getSelectedClassPathRoots()));
    // TODO filters
    //   if (!mojo.getTags().isEmpty()) {
    //     builder.filters(TagFilter.includeTags(mojo.getTags()));
    //   }
    // parameters
    builder.configurationParameters(configuration.getParameters());
    return builder.build();
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  private static UndeclaredThrowableException rethrow(Throwable cause) {
    if (cause instanceof RuntimeException) {
      throw (RuntimeException) cause;
    }
    if (cause instanceof Error) {
      throw (Error) cause;
    }
    return new UndeclaredThrowableException(cause);
  }
}
