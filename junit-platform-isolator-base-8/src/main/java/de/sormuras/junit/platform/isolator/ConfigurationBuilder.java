package de.sormuras.junit.platform.isolator;

import java.util.List;
import java.util.Map;
import java.util.Set;

/** Isolator configuration builder. */
public class ConfigurationBuilder {

  private final Configuration configuration = new Configuration();
  private final DiscoveryBuilder discoveryBuilder = new DiscoveryBuilder();
  private final LauncherBuilder launcherBuilder = new LauncherBuilder();

  /** @return configuration instance. */
  public Configuration build() {
    return configuration;
  }

  /** Enter discovery builder scope. */
  public DiscoveryBuilder discovery() {
    return discoveryBuilder;
  }

  /** Enter launcher builder scope. */
  public LauncherBuilder launcher() {
    return launcherBuilder;
  }

  /** Discover tests only, i.e. don't execute them. */
  public ConfigurationBuilder setDryRun(boolean dryRun) {
    configuration.basic().dryRun = dryRun;
    return this;
  }

  /** Fail and return exit status code 2 if no tests are found. */
  public ConfigurationBuilder setFailIfNoTests(boolean failIfNoTests) {
    configuration.basic().failIfNoTests = failIfNoTests;
    return this;
  }

  /** Use platform or thread context classloader. */
  public ConfigurationBuilder setPlatformClassLoader(boolean platformClassLoader) {
    configuration.basic().platformClassLoader = platformClassLoader;
    return this;
  }

  /** Enable Java language {@code assert} statements. */
  public ConfigurationBuilder setDefaultAssertionStatus(boolean defaultAssertionStatus) {
    configuration.basic().defaultAssertionStatus = defaultAssertionStatus;
    return this;
  }

  /** Maven coordinates of the artifact providing the worker implementation. */
  public ConfigurationBuilder setWorkerCoordinates(String workerCoordinates) {
    configuration.basic().workerCoordinates = workerCoordinates;
    return this;
  }

  /** Maven coordinates of the artifact providing the worker implementation. */
  public ConfigurationBuilder setWorkerClassName(String workerClassName) {
    configuration.basic().workerClassName = workerClassName;
    return this;
  }

  /** Nested Launcher Discovery Request Builder. */
  public class DiscoveryBuilder {

    /** Back to enclosing scope. */
    public ConfigurationBuilder end() {
      return ConfigurationBuilder.this;
    }

    /** Configuration Parameters are text-based key-value pairs. */
    public DiscoveryBuilder setParameters(Map<String, String> parameters) {
      configuration.discovery().parameters = parameters;
      return this;
    }

    /** Select `class-path` roots. */
    public DiscoveryBuilder setSelectedClasspathRoots(Set<String> selectedClasspathRoots) {
      configuration.discovery().selectedClasspathRoots = selectedClasspathRoots;
      return this;
    }

    /** Tags or tag expressions to <b>include</b> only tests whose tags match. */
    public DiscoveryBuilder setFilterTagsIncluded(List<String> filterTagsIncluded) {
      configuration.discovery().filterTagsIncluded = filterTagsIncluded;
      return this;
    }
  }

  /** Nested Launcher Builder. */
  public class LauncherBuilder {

    /** Back to enclosing scope. */
    public ConfigurationBuilder end() {
      return ConfigurationBuilder.this;
    }

    /**
     * Determine if test engines should be discovered at runtime using the {@link
     * java.util.ServiceLoader ServiceLoader} mechanism and automatically registered.
     */
    public LauncherBuilder setTestEngineAutoRegistration(boolean enabled) {
      configuration.launcher().testEngineAutoRegistration = enabled;
      return this;
    }

    /**
     * Determine if test execution listeners should be discovered at runtime using the {@link
     * java.util.ServiceLoader ServiceLoader} mechanism and automatically registered.
     */
    public LauncherBuilder setTestExecutionListenerAutoRegistration(boolean enabled) {
      configuration.launcher().testExecutionListenerAutoRegistration = enabled;
      return this;
    }
  }
}
