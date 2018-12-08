package de.sormuras.junit.platform.manager;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

class LaunchPad {

  private final Overlay.Log log = OverlaySingleton.INSTANCE;

  private final Launcher launcher;
  private final LauncherDiscoveryRequest request;

  LaunchPad(Launcher launcher, LauncherDiscoveryRequest request) {
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
