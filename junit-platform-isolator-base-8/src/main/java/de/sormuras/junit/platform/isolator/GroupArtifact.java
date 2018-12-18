package de.sormuras.junit.platform.isolator;

import java.util.function.Function;

/** Maven group and artifact coordinates. */
public enum GroupArtifact {
  ISOLATOR("de.sormuras.junit", "junit-platform-isolator", "ISOLATOR"),

  ISOLATOR_WORKER("de.sormuras.junit", "junit-platform-isolator-worker", "ISOLATOR"),

  JUNIT_JUPITER_API("org.junit.jupiter", "junit-jupiter-api", "JUNIT_JUPITER"),

  JUNIT_JUPITER_ENGINE("org.junit.jupiter", "junit-jupiter-engine", "JUNIT_JUPITER"),

  JUNIT_PLATFORM_COMMONS("org.junit.platform", "junit-platform-commons", "JUNIT_PLATFORM"),

  JUNIT_PLATFORM_CONSOLE("org.junit.platform", "junit-platform-console", "JUNIT_PLATFORM"),

  JUNIT_PLATFORM_LAUNCHER("org.junit.platform", "junit-platform-launcher", "JUNIT_PLATFORM"),

  JUNIT_VINTAGE_ENGINE("org.junit.vintage", "junit-vintage-engine", "JUNIT_VINTAGE");

  private final String group;
  private final String artifact;
  private final String string;
  private final String versionName;

  GroupArtifact(String group, String artifact, String versionBaseName) {
    this.group = group;
    this.artifact = artifact;
    this.string = group + ':' + artifact;
    this.versionName = versionBaseName + "_VERSION";
  }

  public String getArtifact() {
    return artifact;
  }

  public String getGroup() {
    return group;
  }

  public Version getVersion() {
    return Version.valueOf(versionName); // lazy
  }

  /**
   * Returns a {@link String} that consists of the group and artifact separated by colon.
   *
   * @return {@code group + ':' + artifact}
   */
  @Override
  public String toString() {
    return string;
  }

  public String toString(Function<Version, String> version) {
    return string + ':' + version.apply(getVersion());
  }

  public String toStringWithDefaultVersion() {
    return string + ':' + getVersion().getDefaultVersion();
  }
}
