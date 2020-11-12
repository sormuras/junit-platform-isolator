package de.sormuras.junit.platform.isolator;

import static java.util.Arrays.asList;
import static java.util.Collections.EMPTY_SET;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
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

  /** Class name of the worker to instantiate. */
  public ConfigurationBuilder setWorkerClassName(String workerClassName) {
    configuration.basic().workerClassName = workerClassName;
    return this;
  }

  /** Worker requires isolation by default -- set to {@code false} to relax. */
  public ConfigurationBuilder setWorkerIsolationRequired(boolean workerIsolationRequired) {
    configuration.basic().workerIsolationRequired = workerIsolationRequired;
    return this;
  }

  /** Set map of paths. */
  public ConfigurationBuilder setPaths(Map<String, Set<String>> paths) {
    configuration.basic().paths = paths;
    return this;
  }

  /** Set main target output directory. */
  public ConfigurationBuilder setTargetMainPath(String targetMainPath) {
    configuration.basic().targetMainPath = targetMainPath;
    return this;
  }

  /** Set test target output directory. */
  public ConfigurationBuilder setTargetTestPath(String targetTestPath) {
    configuration.basic().targetTestPath = targetTestPath;
    return this;
  }

  /** Set path to {@code module-info.test} file. */
  public ConfigurationBuilder setModuleInfoTestPath(String moduleInfoTestPath) {
    configuration.basic().moduleInfoTestPath = moduleInfoTestPath;
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
        return setSelectedUris(new LinkedHashSet<>(uris));
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
        return setSelectedFiles(new LinkedHashSet<>(files));
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

    /** Add directories to the set of selected directories. */
    public DiscoveryBuilder addSelectedDirectories(Collection<String> directories) {
      if (discovery.selectedDirectories == EMPTY_SET) {
        return setSelectedDirectories(new LinkedHashSet<>(directories));
      }
      discovery.selectedDirectories.addAll(directories);
      return this;
    }

    /** Add directories to the set of selected directories. */
    public DiscoveryBuilder addSelectedDirectories(String... directories) {
      return addSelectedDirectories(asList(directories));
    }

    // -- selector: packages --

    /** Select packages via their names. */
    public DiscoveryBuilder setSelectedPackages(Set<String> selectedPackages) {
      discovery.selectedPackages = selectedPackages;
      return this;
    }

    /** Add packages to the set of selected packages. */
    public DiscoveryBuilder addSelectedPackages(Collection<String> packages) {
      if (discovery.selectedPackages == EMPTY_SET) {
        return setSelectedPackages(new LinkedHashSet<>(packages));
      }
      discovery.selectedPackages.addAll(packages);
      return this;
    }

    /** Add packages to the set of selected packages. */
    public DiscoveryBuilder addSelectedPackages(String... packages) {
      return addSelectedPackages(asList(packages));
    }

    // -- selector: classes --

    /** Select classes via their names. */
    public DiscoveryBuilder setSelectedClasses(Set<String> selectedClasses) {
      discovery.selectedClasses = selectedClasses;
      return this;
    }

    /** Add classes to the set of selected classes. */
    public DiscoveryBuilder addSelectedClasses(Collection<String> classes) {
      if (discovery.selectedClasses == EMPTY_SET) {
        return setSelectedClasses(new LinkedHashSet<>(classes));
      }
      discovery.selectedClasses.addAll(classes);
      return this;
    }

    /** Add classes to the set of selected classes. */
    public DiscoveryBuilder addSelectedClasses(String... classes) {
      return addSelectedClasses(asList(classes));
    }

    // -- selector: methods --

    /** Select methods via their names. */
    public DiscoveryBuilder setSelectedMethods(Set<String> selectedMethods) {
      discovery.selectedMethods = selectedMethods;
      return this;
    }

    /** Add methods to the set of selected methods. */
    public DiscoveryBuilder addSelectedMethods(Collection<String> methods) {
      if (discovery.selectedMethods == EMPTY_SET) {
        return setSelectedMethods(new LinkedHashSet<>(methods));
      }
      discovery.selectedMethods.addAll(methods);
      return this;
    }

    /** Add methods to the set of selected methods. */
    public DiscoveryBuilder addSelectedMethods(String... methods) {
      return addSelectedMethods(asList(methods));
    }

    // -- selector: classpath-resources --

    /** Select `class-path` resources. */
    public DiscoveryBuilder setSelectedClasspathResources(Set<String> selectedClasspathResources) {
      discovery.selectedClasspathResources = selectedClasspathResources;
      return this;
    }

    /** Add resources to the set of selected resources. */
    public DiscoveryBuilder addSelectedClasspathResources(Collection<String> resources) {
      if (discovery.selectedClasspathResources == EMPTY_SET) {
        return setSelectedClasspathResources(new LinkedHashSet<>(resources));
      }
      discovery.selectedClasspathResources.addAll(resources);
      return this;
    }

    /** Add resources to the set of selected resources. */
    public DiscoveryBuilder addSelectedClasspathResources(String... resources) {
      return addSelectedClasspathResources(asList(resources));
    }

    // -- selector: classpath-roots --

    /** Select `class-path` roots. */
    public DiscoveryBuilder setSelectedClasspathRoots(Set<String> selectedClasspathRoots) {
      discovery.selectedClasspathRoots = selectedClasspathRoots;
      return this;
    }

    /** Add roots to the set of selected roots. */
    public DiscoveryBuilder addSelectedClasspathRoots(Collection<String> roots) {
      if (discovery.selectedClasspathRoots == EMPTY_SET) {
        return setSelectedClasspathRoots(new LinkedHashSet<>(roots));
      }
      discovery.selectedClasspathRoots.addAll(roots);
      return this;
    }

    /** Add roots to the set of selected roots. */
    public DiscoveryBuilder addSelectedClasspathRoots(String... roots) {
      return addSelectedClasspathRoots(asList(roots));
    }

    // -- selector: modules --

    /** Select modules via their names. */
    public DiscoveryBuilder setSelectedModules(Set<String> selectedModules) {
      discovery.selectedModules = selectedModules;
      return this;
    }

    /** Add modules to the set of selected modules. */
    public DiscoveryBuilder addSelectedModules(Collection<String> modules) {
      if (discovery.selectedModules == EMPTY_SET) {
        return setSelectedModules(new LinkedHashSet<>(modules));
      }
      discovery.selectedModules.addAll(modules);
      return this;
    }

    /** Add modules to the set of selected modules. */
    public DiscoveryBuilder addSelectedModules(String... modules) {
      return addSelectedModules(asList(modules));
    }

    // -- filter: class name patterns --

    /** Class name filters when scanning class-path elements or modules. */
    public DiscoveryBuilder setFilterClassNamePatterns(Set<String> filterClassNamePatterns) {
      discovery.filterClassNamePatterns = filterClassNamePatterns;
      return this;
    }

    /** Add patterns to the set of patterns. */
    public DiscoveryBuilder addFilterClassNamePatterns(Collection<String> patterns) {
      if (discovery.filterClassNamePatterns == EMPTY_SET) {
        return setFilterClassNamePatterns(new LinkedHashSet<>(patterns));
      }
      discovery.filterClassNamePatterns.addAll(patterns);
      return this;
    }

    /** Add patterns to the set of patterns. */
    public DiscoveryBuilder addFilterClassNamePatterns(String... patterns) {
      return addFilterClassNamePatterns(asList(patterns));
    }

    // -- filter: tag or tag expressions --

    /** Tags or tag expressions to <b>include</b> only tests whose tags match. */
    public DiscoveryBuilder setFilterTags(Set<String> filterTags) {
      discovery.filterTags = filterTags;
      return this;
    }

    /** Add tags or tag expressions to the set of tags. */
    public DiscoveryBuilder addFilterTags(Collection<String> tags) {
      if (discovery.filterTags == EMPTY_SET) {
        return setFilterTags(new LinkedHashSet<>(tags));
      }
      discovery.filterTags.addAll(tags);
      return this;
    }

    /** Add tags or tag expressions to the set of tags. */
    public DiscoveryBuilder addFilterTags(String... tags) {
      return addFilterTags(asList(tags));
    }

    // -- configuration parameters --

    /** Set configuration parameters. */
    public DiscoveryBuilder setParameters(Map<String, String> parameters) {
      discovery.parameters = parameters;
      return this;
    }

    /** Put configuration parameter. */
    public DiscoveryBuilder addParameter(String key, String value) {
      if (discovery.parameters == Collections.EMPTY_MAP) {
        discovery.parameters = new LinkedHashMap<>();
      }
      discovery.parameters.put(key, value);
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

    /** Collection of additional test engines. */
    public LauncherBuilder setAdditionalTestEngines(Collection<String> additionalTestEngines) {
      configuration.launcher().additionalTestEngines = additionalTestEngines;
      return this;
    }
  }
}
