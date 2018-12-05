package de.sormuras.junit.platform.manager;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherFactory;

public class Manager implements Launcher {

  private final Overlay.Log log = OverlaySingleton.INSTANCE;

  private final Launcher launcher;

  public Manager() {
    this(LauncherFactory.create());
  }

  public Manager(Launcher launcher) {
    this.launcher = launcher;
    log.info("Created Manager with launcher: " + launcher);
  }

  @Override
  public void registerTestExecutionListeners(TestExecutionListener... listeners) {
    log.info("registerTestExecutionListeners({0})", (Object) listeners);
    launcher.registerTestExecutionListeners(listeners);
  }

  @Override
  public TestPlan discover(LauncherDiscoveryRequest request) {
    log.info("discover({0})", launcher);
    return launcher.discover(request);
  }

  @Override
  public void execute(LauncherDiscoveryRequest request, TestExecutionListener... listeners) {
    log.info("execute({0}, {1})" + request, (Object) listeners);
    launcher.execute(request, listeners);
  }
}
