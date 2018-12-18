package de.sormuras.junit.platform.isolator;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_SET;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedHashSet;
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

  /** Base target directory. */
  public ConfigurationBuilder setTargetDirectory(String targetDirectory) {
    configuration.basic().targetDirectory = targetDirectory;
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

    private final Configuration.Discovery discovery = configuration.discovery();

    /** Back to enclosing scope. */
    public ConfigurationBuilder end() {
      return ConfigurationBuilder.this;
    }

    // -- selector: uris --

    /** Set selected uris. */
    public DiscoveryBuilder setSelectedUris(Set<URI> selectedUris) {
      discovery.selectedUris = selectedUris;
      return this;
    }

    /** Add uris to set of selected uris. */
    public DiscoveryBuilder addSelectedUris(Collection<URI> uris) {
      if (discovery.selectedUris == EMPTY_SET) {
        discovery.selectedUris = new LinkedHashSet<>();
      }
      discovery.selectedUris.addAll(uris);
      return this;
    }

    /** Add uris to set of selected uris. */
    public DiscoveryBuilder addSelectedUris(URI... uris) {
      return addSelectedUris(asList(uris));
    }

    // -- selector: files --

    /** Set selected files. */
    public DiscoveryBuilder setSelectedFiles(Set<String> selectedFiles) {
      discovery.selectedFiles = selectedFiles;
      return this;
    }

    /** Add files to the set of selected files. */
    public DiscoveryBuilder addSelectedFiles(Collection<String> files) {
      if (discovery.selectedFiles == EMPTY_SET) {
        discovery.selectedFiles = new LinkedHashSet<>();
      }
      discovery.selectedFiles.addAll(files);
      return this;
    }

    /** Add files to the set of selected files. */
    public DiscoveryBuilder addSelectedFiles(String... files) {
      return addSelectedFiles(asList(files));
    }

    // -- selector: directories --

    /** Select directories. */
    public DiscoveryBuilder setSelectedDirectories(Set<String> selectedDirectories) {
      discovery.selectedDirectories = selectedDirectories;
      return this;
    }

    // -- selector: packages --

    /** Select packages via their names. */
    public DiscoveryBuilder setSelectedPackages(Set<String> selectedPackages) {
      discovery.selectedPackages = selectedPackages;
      return this;
    }

    // -- selector: classes --

    /** Select classes via their names. */
    public DiscoveryBuilder setSelectedClasses(Set<String> selectedClasses) {
      discovery.selectedClasses = selectedClasses;
      return this;
    }

    // -- selector: methods --

    /** Select methods via their names. */
    public DiscoveryBuilder setSelectedMethods(Set<String> selectedMethods) {
      discovery.selectedMethods = selectedMethods;
      return this;
    }

    // -- selector: classpath-resources --

    /** Select `class-path` roots. */
    public DiscoveryBuilder setSelectedClasspathResources(Set<String> selectedClasspathResources) {
      discovery.selectedClasspathResources = selectedClasspathResources;
      return this;
    }

    // -- selector: classpath-roots --

    /** Select `class-path` roots. */
    public DiscoveryBuilder setSelectedClasspathRoots(Set<String> selectedClasspathRoots) {
      discovery.selectedClasspathRoots = selectedClasspathRoots;
      return this;
    }

    // -- selector: modules --

    /** Select modules via their names. */
    public DiscoveryBuilder setSelectedModules(Set<String> selectedModules) {
      discovery.selectedModules = selectedModules;
      return this;
    }

    // -- filter: class name patterns --

    /** Class name filters when scanning class-path elements or modules. */
    public DiscoveryBuilder setFilterClassNamePatterns(Set<String> filterClassNamePatterns) {
      discovery.filterClassNamePatterns = filterClassNamePatterns;
      return this;
    }

    // -- filter: tag or tag expressions --

    /** Tags or tag expressions to <b>include</b> only tests whose tags match. */
    public DiscoveryBuilder setFilterTags(Set<String> filterTags) {
      discovery.filterTags = filterTags;
      return this;
    }

    // -- configuration parameters --

    /** Configuration Parameters are text-based key-value pairs. */
    public DiscoveryBuilder setParameters(Map<String, String> parameters) {
      discovery.parameters = parameters;
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
