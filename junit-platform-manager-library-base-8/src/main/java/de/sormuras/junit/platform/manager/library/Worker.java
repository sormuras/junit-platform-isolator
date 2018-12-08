package de.sormuras.junit.platform.manager.library;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

class Worker {

  private final Overlay.Log log = OverlaySingleton.INSTANCE;

  private final Launcher launcher;
  private final LauncherDiscoveryRequest request;

  Worker(Launcher launcher, LauncherDiscoveryRequest request) {
    this.launcher = launcher;
    this.request = request;
  }

  TestPlan discover() {
    log.debug("Discovering tests...");

    TestPlan testPlan = launcher.discover(request);
    testPlan.getRoots().forEach(engine -> log.info(" o " + engine.getDisplayName()));
    log.info("Test plan tree: (...)");

    return testPlan;
  }

  int execute() {
    log.debug("Executing tests...");

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
      log.info(
          "Successfully executed %d test(s) in %d ms", summary.getTestsSucceededCount(), duration);
      return 0;
    }

    StringWriter string = new StringWriter();
    PrintWriter writer = new PrintWriter(string);
    summary.printTo(writer);
    summary.printFailuresTo(writer);
    for (String line : string.toString().split("\\R")) {
      log.warn(line);
    }
    return 1;
  }
}
