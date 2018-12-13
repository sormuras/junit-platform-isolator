package de.sormuras.junit.platform.isolator.worker;

import de.sormuras.junit.platform.isolator.Configuration;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
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

  /** Log a message formatted in {@link java.text.MessageFormat} style at debug level. */
  private void debug(String format, Object... args) {
    log.accept("debug", MessageFormat.format(format, args));
  }

  /** Log a message formatted in {@link java.text.MessageFormat} style at info level. */
  private void info(String format, Object... args) {
    log.accept("info", MessageFormat.format(format, args));
  }

  /** Log a message formatted in {@link java.text.MessageFormat} style at warning level. */
  private void warn(String format, Object... args) {
    log.accept("warn", MessageFormat.format(format, args));
  }

  @Override
  public Integer call() {
    debug("Creating launcher and discovery request...");
    Launcher launcher = new LauncherCreator().create(configuration);
    LauncherDiscoveryRequest request = new LauncherDiscoveryRequestCreator().create(configuration);

    if (configuration.isDryRun()) {
      debug("Discover-only in dry-run mode...");
      TestPlan testPlan = launcher.discover(request);
      if (!testPlan.containsTests()) {
        warn("No test found: {0}", configuration);
        if (configuration.isFailIfNoTests()) {
          return 2;
        }
      }
      info("Dry-run");
      info("Discovered {0} test(s)", testPlan.countTestIdentifiers(TestIdentifier::isTest));
      return 0;
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

    debug("Executing launcher...");
    launcher.execute(request);

    TestExecutionSummary summary = summaryGeneratingListener.getSummary();

    if (summary.getTestsFoundCount() == 0 && configuration.isFailIfNoTests()) {
      warn("No test found: {0}", configuration);
      return 2;
    }

    if (summary.getTotalFailureCount() == 0) {
      long tests = summary.getTestsSucceededCount();
      long duration = summary.getTimeFinished() - summary.getTimeStarted();
      info("Successfully executed {0} test(s) in {1} ms", tests, duration);
      return 0;
    }

    warn("{0} failure(s) found", summary.getTotalFailureCount());
    StringWriter string = new StringWriter();
    PrintWriter writer = new PrintWriter(string);
    summary.printTo(writer);
    summary.printFailuresTo(writer);
    for (String line : string.toString().split("\\R")) {
      warn(line);
    }
    return 1;
  }

  public Configuration getConfiguration() {
    return configuration;
  }
}
