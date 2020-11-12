package de.sormuras.junit.platform.isolator;

import static de.sormuras.junit.platform.isolator.GroupArtifact.ISOLATOR;
import static de.sormuras.junit.platform.isolator.GroupArtifact.ISOLATOR_WORKER;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_JUPITER_API;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_JUPITER_ENGINE;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_PLATFORM_COMMONS;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_VINTAGE_ENGINE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/** Maven artifact version defaults. */
public enum Version {
  /** {@code isolator.version} */
  ISOLATOR_VERSION(implementationVersion("1.0.0-M11"), ISOLATOR, ISOLATOR_WORKER),

  /** {@code junit.platform.version} */
  JUNIT_PLATFORM_VERSION("1.3.2", JUNIT_PLATFORM_COMMONS),

  /** {@code junit.jupiter.version} */
  JUNIT_JUPITER_VERSION("5.3.2", JUNIT_JUPITER_API, JUNIT_JUPITER_ENGINE),

  /** {@code junit.vintage.version} */
  JUNIT_VINTAGE_VERSION("5.3.2", JUNIT_VINTAGE_ENGINE);

  /** createArtifactVersionMap */
  public static Map<String, String> buildMap(UnaryOperator<String> versionOperator) {
    Map<String, String> result = new HashMap<>();
    withNextVersion:
    for (Version version : Version.values()) {
      String versionKey = version.getKey();
      for (GroupArtifact groupArtifact : version.getGroupArtifacts()) {
        String artifactVersion = versionOperator.apply(groupArtifact.toString());
        if (artifactVersion != null) {
          result.put(versionKey, artifactVersion);
          continue withNextVersion;
        }
      }
      result.put(versionKey, version.getDefaultVersion());
    }
    return result;
  }

  public static void forEach(Consumer<Version> versionConsumer) {
    Arrays.stream(values()).sorted().forEach(versionConsumer);
  }

  /** Extract implementation version. */
  public static String implementationVersion(String defaultVersion) {
    String version = Version.class.getPackage().getImplementationVersion();
    return version != null ? version : defaultVersion;
  }

  private final String key;
  private final String defaultVersion;
  private final List<GroupArtifact> groupArtifacts;

  Version(String defaultVersion, GroupArtifact... groupArtifacts) {
    this.key = name().toLowerCase().replace('_', '.');
    this.defaultVersion = defaultVersion;
    this.groupArtifacts = Arrays.asList(groupArtifacts);
  }

  public List<GroupArtifact> getGroupArtifacts() {
    return groupArtifacts;
  }

  public String getKey() {
    return key;
  }

  public String getDefaultVersion() {
    return defaultVersion;
  }
}
