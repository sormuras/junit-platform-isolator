package de.sormuras.junit.platform.manager;

/** Test run configuration. */
public interface Configuration {

  /** Discover tests only, i.e. don't execute them. */
  boolean isDryRun();

  class Default implements Configuration {
    boolean dryRun = false;

    @Override
    public boolean isDryRun() {
      return dryRun;
    }

    public Default setDryRun(boolean dryRun) {
      this.dryRun = dryRun;
      return this;
    }
  }
}
