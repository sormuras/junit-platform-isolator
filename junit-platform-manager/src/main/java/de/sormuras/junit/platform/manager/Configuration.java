package de.sormuras.junit.platform.manager;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/** Manager configuration. */
public interface Configuration {

  /** Discover tests only, i.e. don't execute them. */
  boolean isDryRun();

  /** Select `class-path` roots. */
  Set<Path> getSelectedClassPathRoots();

  /** https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params */
  Map<String, String> getParameters();

  class Default implements Configuration {

    boolean dryRun = false;

    Map<String, String> parameters = Collections.emptyMap();

    Set<Path> selectedClassPathRoots = Collections.emptySet();

    @Override
    public boolean isDryRun() {
      return dryRun;
    }

    public Default setDryRun(boolean dryRun) {
      this.dryRun = dryRun;
      return this;
    }

    @Override
    public Set<Path> getSelectedClassPathRoots() {
      return selectedClassPathRoots;
    }

    public Default setSelectedClassPathRoots(Set<Path> paths) {
      this.selectedClassPathRoots = paths;
      return this;
    }

    @Override
    public Map<String, String> getParameters() {
      return parameters;
    }

    public Default setParameters(Map<String, String> parameters) {
      this.parameters = parameters;
      return this;
    }
  }
}
