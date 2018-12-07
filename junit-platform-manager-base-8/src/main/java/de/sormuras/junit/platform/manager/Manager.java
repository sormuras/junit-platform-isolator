package de.sormuras.junit.platform.manager;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectDirectory;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.concurrent.Callable;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

public class Manager implements Callable<Integer> {

  private final Overlay.Log log = OverlaySingleton.INSTANCE;

  private final Configuration configuration;
  private final LauncherDiscoveryRequest request;

  public Manager() {
    this(
        new Configuration.Default()
            .setSelectedDirectories(
                Collections.singletonList(Paths.get("target", "test-classes"))));
  }

  public Manager(Configuration configuration) {
    this.configuration = configuration;
    this.request = buildRequest(configuration);
    log.info("Created Manager with configuration: " + configuration);
  }

  private LauncherDiscoveryRequest buildRequest(Configuration configuration) {
    LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
    // selectors
    configuration.getSelectedDirectories().forEach(p -> selectDirectory(p.toFile()));
    // TODO filters
    //   if (!mojo.getTags().isEmpty()) {
    //     builder.filters(TagFilter.includeTags(mojo.getTags()));
    //   }
    // parameters
    builder.configurationParameters(configuration.getParameters());
    return builder.build();
  }

  @Override
  public Integer call() {
    Launcher launcher = createLauncher();
    discover(launcher);
    if (configuration.isDryRun()) {
      log.info("Dry-run.");
      return 0;
    }
    return execute(launcher);
  }

  protected Launcher createLauncher() {
    return LauncherFactory.create();
  }

  protected void discover(Launcher launcher) {
    log.debug("Discovering tests...");

    TestPlan testPlan = launcher.discover(request);
    testPlan.getRoots().forEach(engine -> log.info(" o " + engine.getDisplayName()));
    log.info("Test plan tree: (...)");

    if (!testPlan.containsTests() && !configuration.isDryRun()) {
      String message = "Zero tests discovered!";
      log.warn(message);
      throw new AssertionError(message);
    }
  }

  protected int execute(Launcher launcher) {
    log.debug("Executing tests...");

    SummaryGeneratingListener summary = new SummaryGeneratingListener();
    launcher.registerTestExecutionListeners(summary);

    // TODO https://github.com/junit-team/junit5/issues/1375
    //    String reports = configuration.getReports();
    //    if (!reports.trim().isEmpty()) {
    //      Path path = Paths.get(reports);
    //      if (!path.isAbsolute()) {
    //        path = Paths.get(build.getDirectory()).resolve(path);
    //      }
    //      launcher.register...(new XmlReportsWritingListener(path, log::error));
    //    }

    launcher.execute(request);

    return summary.getSummary().getTotalFailureCount() == 0 ? 0 : 1;
  }
}
