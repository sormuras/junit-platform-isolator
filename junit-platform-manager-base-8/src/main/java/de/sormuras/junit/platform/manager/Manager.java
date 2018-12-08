package de.sormuras.junit.platform.manager;

import static org.junit.platform.engine.discovery.DiscoverySelectors.*;

import java.lang.reflect.UndeclaredThrowableException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

public class Manager implements BiConsumer<String, Object>, Callable<Integer> {

  private final Overlay.Log log = OverlaySingleton.INSTANCE;

  private boolean dryRun = false;
  private final List<DiscoverySelector> selectors = new ArrayList<>();
  private final Map<String, String> parameters = new HashMap<>();

  public Manager() {
    log.info("Manager created (loader={0})", getClass().getClassLoader());
  }

  @Override
  public void accept(String name, Object value) {
    switch (name) {
      case "dry-run(boolean)":
        dryRun = (boolean) value;
        return;
      case "class-path-root(Path)":
        Set<Path> root = Collections.singleton((Path) value);
        selectors.addAll(selectClasspathRoots(root));
        return;
      case "class-path-roots(Set<Path>)":
        @SuppressWarnings("unchecked")
        Set<Path> roots = (Set<Path>) value;
        selectors.addAll(selectClasspathRoots(roots));
        return;
      case "configuration-parameters":
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) value;
        parameters.putAll(map);
        return;
    }
    if (name.startsWith("configuration-parameter-")) {
      String key = name.substring("configuration-parameter-".length());
      parameters.put(key, String.valueOf(value));
      return;
    }
    if (value instanceof DiscoverySelector) {
      selectors.add((DiscoverySelector) value);
      return;
    }
    // direct field access?
    try {
      getClass().getField(name).set(this, value);
    } catch (ReflectiveOperationException e) {
      log.warn("Setting field {0} to {1} failed!", name, value);
      throw rethrow(e);
    }
  }

  @Override
  public Integer call() {
    Launcher launcher = LauncherFactory.create();
    LauncherDiscoveryRequest request = createRequest();
    LaunchPad launchPad = new LaunchPad(launcher, request);
    TestPlan testPlan = launchPad.discover();
    if (isDryRun()) {
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
    builder.selectors(selectors);
    // configuration.getSelectedDirectories().forEach(p -> selectClasspathRoots(p.toFile()));
    // TODO filters
    //   if (!mojo.getTags().isEmpty()) {
    //     builder.filters(TagFilter.includeTags(mojo.getTags()));
    //   }
    // parameters
    builder.configurationParameters(parameters);
    return builder.build();
  }

  public boolean isDryRun() {
    return dryRun;
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
