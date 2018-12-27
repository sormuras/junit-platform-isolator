package de.sormuras.junit.platform.isolator.worker;

import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

import de.sormuras.junit.platform.isolator.Configuration;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import org.junit.platform.reporting.legacy.xml.LegacyXmlReportGeneratingListener;

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
    Launcher launcher = new LauncherCreator().create(configuration.launcher());
    DiscoveryCreator creator = new DiscoveryCreator(configuration.discovery());

    LauncherDiscoveryRequestBuilder requestBuilder = request();
    requestBuilder.selectors(creator.createDiscoverySelectors());
    requestBuilder.filters(creator.createFilters());
    requestBuilder.configurationParameters(configuration.discovery().getParameters());
    LauncherDiscoveryRequest request = requestBuilder.build();

    Configuration.Basic basic = configuration.basic();
    Path targetDirectory = Paths.get(basic.getTargetDirectory());

    if (basic.isDryRun()) {
      return executeDryRun(launcher, request);
    }

    SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();
    launcher.registerTestExecutionListeners(summaryGeneratingListener);

    try {
      Class.forName("org.junit.platform.reporting.legacy.xml.LegacyXmlReportGeneratingListener");
      LegacyXmlReportGeneratingListener legacyXmlReportGeneratingListener =
          new LegacyXmlReportGeneratingListener(
              targetDirectory, new PrintWriter(new LogWriter(this::warn)));
      launcher.registerTestExecutionListeners(legacyXmlReportGeneratingListener);
    } catch (ClassNotFoundException e) {
      warn("LegacyXmlReportGeneratingListener not found - JUnit Platform 1.4+ is required", e);
    }

    debug("Executing launcher...");
    launcher.execute(request);

    TestExecutionSummary summary = summaryGeneratingListener.getSummary();
    List<String> summaryLines = toLines(summary);

    // Write summary to log file...
    Path summaryPath = targetDirectory.resolve("junit-platform-summary.txt");
    if (Files.isDirectory(targetDirectory)) {
      try {
        Files.write(summaryPath, summaryLines);
      } catch (IOException e) {
        warn("Writing summary failed", e.getMessage());
      }
    }

    if (summary.getTestsFoundCount() == 0 && basic.isFailIfNoTests()) {
      warn("No test found: {0}", configuration);
      return 2;
    }

    if (summary.getTotalFailureCount() == 0) {
      long tests = summary.getTestsSucceededCount();
      long duration = summary.getTimeFinished() - summary.getTimeStarted();
      info("Successfully executed {0} test(s) in {1} ms", tests, duration);
      info("Find test run details at " + summaryPath.toUri());
      return 0;
    }

    summaryLines.forEach(this::warn);
    return 1;
  }

  private int executeDryRun(Launcher launcher, LauncherDiscoveryRequest request) {
    info("Dry-run...");
    TestPlan testPlan = launcher.discover(request);

    Configuration.Basic basic = configuration.basic();
    Path targetDirectory = Paths.get(basic.getTargetDirectory());

    if (!testPlan.containsTests()) {
      warn("No test found: {0}", configuration);
      if (basic.isFailIfNoTests()) {
        return 2;
      }
    }
    info("Discovered {0} test(s)", testPlan.countTestIdentifiers(TestIdentifier::isTest));
    Path testPlanPath = targetDirectory.resolve("junit-platform-dry-run-test-plan.txt");
    if (Files.isDirectory(targetDirectory)) {
      List<String> testPlanLines = new ArrayList<>();
      for (TestIdentifier engines : testPlan.getRoots()) {
        testPlan.getDescendants(engines).forEach(id -> testPlanLines.add(id.toString()));
      }
      try {
        Files.write(testPlanPath, testPlanLines);
        info("Find test plan details at " + testPlanPath.toUri());
      } catch (IOException e) {
        warn("Writing test plan failed", e.getMessage());
      }
    }
    return 0;
  }

  public Configuration getConfiguration() {
    return configuration;
  }

  private static List<String> toLines(TestExecutionSummary summary) {
    StringWriter string = new StringWriter();
    PrintWriter writer = new PrintWriter(string);
    summary.printTo(writer);
    summary.printFailuresTo(writer);
    return Arrays.asList(string.toString().split("\\R"));
  }

  private static class LogWriter extends Writer {

    private final BiConsumer<String, Object[]> consumer;
    private final Object[] empty = {};

    private LogWriter(BiConsumer<String, Object[]> consumer) {
      this.consumer = consumer;
    }

    @Override
    public void write(char[] cbuf, int off, int len) {
      consumer.accept(new String(cbuf, off, len), empty);
    }

    @Override
    public void flush() {}

    @Override
    public void close() {}
  }
}
