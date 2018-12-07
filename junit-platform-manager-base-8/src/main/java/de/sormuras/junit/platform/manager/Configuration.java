package de.sormuras.junit.platform.manager;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Test run configuration. */
public interface Configuration {

  /** Discover tests only, i.e. don't execute them. */
  boolean isDryRun();

  /** Directory selection. */
  List<Path> getSelectedDirectories();

  /** https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params */
  Map<String, String> getParameters();

  class Default implements Configuration {

    boolean dryRun = false;
    Map<String, String> parameters = Collections.emptyMap();
    List<Path> selectedDirectories = Collections.emptyList();

    @Override
    public boolean isDryRun() {
      return dryRun;
    }

    public Default setDryRun(boolean dryRun) {
      this.dryRun = dryRun;
      return this;
    }

    @Override
    public List<Path> getSelectedDirectories() {
      return selectedDirectories;
    }

    public Default setSelectedDirectories(List<Path> paths) {
      this.selectedDirectories = paths;
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
