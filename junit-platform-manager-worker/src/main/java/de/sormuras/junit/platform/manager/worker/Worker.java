package de.sormuras.junit.platform.manager.worker;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;

import de.sormuras.junit.platform.manager.Configuration;
import de.sormuras.junit.platform.manager.Log;
import de.sormuras.junit.platform.manager.OverlaySingleton;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

public class Worker implements Callable<Integer> {

  private final Log log = OverlaySingleton.INSTANCE;

  private final Configuration configuration;

  public Worker(byte[] configuration) {
    this(Configuration.fromBytes(configuration));
  }

  public Worker(Configuration configuration) {
    this.configuration = configuration;
    log.info("Manager created (loader={0})", getClass().getClassLoader());
  }

  @Override
  public Integer call() {
    Launcher launcher = LauncherFactory.create();
    LauncherDiscoveryRequest request = createRequest();
    LaunchPad launchPad = new LaunchPad(launcher, request);
    TestPlan testPlan = launchPad.discover();
    if (configuration.isDryRun()) {
      log.info("Dry-run.");
      return 0;
    }
    if (!testPlan.containsTests()) {
      log.warn("No test found.", testPlan.getRoots());
      return -1;
    }
    return launchPad.execute();
  }

  private LauncherDiscoveryRequest createRequest() {
    LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
    // selectors
    Set<Path> classPathRoots = new LinkedHashSet<>();
    for (String root : configuration.getSelectedClassPathRoots()) {
      classPathRoots.add(Paths.get(root));
    }
    builder.selectors(selectClasspathRoots(classPathRoots));
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
}
