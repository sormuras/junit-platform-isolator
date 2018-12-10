package de.sormuras.junit.platform.manager.worker;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;

import de.sormuras.junit.platform.manager.Configuration;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

public class Worker implements Callable<Integer> {

  private final Configuration configuration;
  private final BiConsumer<String, String> log;

  public Worker(byte[] configuration, BiConsumer<String, String> log) {
    this(Configuration.fromBytes(configuration), log);
  }

  public Worker(Configuration configuration, BiConsumer<String, String> log) {
    this.configuration = configuration;
    this.log = log;
  }

  private LauncherDiscoveryRequest buildRequest() {
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

  @Override
  public Integer call() {
    Launcher launcher = LauncherFactory.create();
    LauncherDiscoveryRequest request = buildRequest();
    TestPlan testPlan = launcher.discover(request);
    if (configuration.isDryRun()) {
      log.accept("info", "Dry-run.");
      return 0;
    }
    if (!testPlan.containsTests()) {
      log.accept("warn", "No test found: " + testPlan.getRoots());
      return -1;
    }
    SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();
    launcher.registerTestExecutionListeners(summaryGeneratingListener);

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

    TestExecutionSummary summary = summaryGeneratingListener.getSummary();
    long failures = summary.getTotalFailureCount();
    boolean ok = failures == 0;

    if (ok) {
      long duration = summary.getTimeStarted() - summary.getTimeFinished();
      log.accept(
          "info",
          "Successfully executed "
              + summary.getTestsSucceededCount()
              + " test(s) in "
              + duration
              + " ms");
      return 0;
    }

    StringWriter string = new StringWriter();
    PrintWriter writer = new PrintWriter(string);
    summary.printTo(writer);
    summary.printFailuresTo(writer);
    for (String line : string.toString().split("\\R")) {
      log.accept("warn", line);
    }
    return 1;
  }

  public Configuration getConfiguration() {
    return configuration;
  }
}
