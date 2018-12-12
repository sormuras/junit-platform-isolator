package de.sormuras.junit.platform.isolator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Isolator configuration. */
public class Configuration implements Serializable {

  public static final String WORKER_GROUP = "de.sormuras.junit-platform-isolator";
  public static final String WORKER_ARTIFACT = "junit-platform-isolator-worker";
  public static final String WORKER_VERSION = implementationVersion("1.0.0-SNAPSHOT");

  private String workerCoordinates = WORKER_GROUP + ':' + WORKER_ARTIFACT + ':' + WORKER_VERSION;
  private String workerClassName = "de.sormuras.junit.platform.isolator.worker.Worker";
  private boolean dryRun = false;
  private boolean platformClassLoader = true;
  private boolean defaultAssertionStatus = true;
  private Map<String, String> parameters = Collections.emptyMap();
  private Set<String> selectedClassPathRoots = Collections.emptySet();

  public boolean isPlatformClassLoader() {
    return platformClassLoader;
  }

  public Configuration setPlatformClassLoader(boolean platformClassLoader) {
    this.platformClassLoader = platformClassLoader;
    return this;
  }

  public String getWorkerCoordinates() {
    return workerCoordinates;
  }

  public Configuration setWorkerCoordinates(String workerCoordinates) {
    this.workerCoordinates = workerCoordinates;
    return this;
  }

  public String getWorkerClassName() {
    return workerClassName;
  }

  public Configuration setWorkerClassName(String workerClassName) {
    this.workerClassName = workerClassName;
    return this;
  }

  /** Discover tests only, i.e. don't execute them. */
  public boolean isDryRun() {
    return dryRun;
  }

  /** Discover tests only, i.e. don't execute them. */
  public Configuration setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }

  public boolean isDefaultAssertionStatus() {
    return defaultAssertionStatus;
  }

  public Configuration setDefaultAssertionStatus(boolean defaultAssertionStatus) {
    this.defaultAssertionStatus = defaultAssertionStatus;
    return this;
  }

  /** Select `class-path` roots. */
  public Set<String> getSelectedClassPathRoots() {
    return selectedClassPathRoots;
  }

  /** Select `class-path` roots. */
  public Configuration setSelectedClassPathRoots(Set<String> paths) {
    this.selectedClassPathRoots = paths;
    return this;
  }

  /** https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params */
  public Map<String, String> getParameters() {
    return parameters;
  }

  /** https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params */
  public Configuration setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Configuration that = (Configuration) o;
    return dryRun == that.dryRun
        && workerCoordinates.equals(that.workerCoordinates)
        && workerClassName.equals(that.workerClassName)
        && parameters.equals(that.parameters)
        && selectedClassPathRoots.equals(that.selectedClassPathRoots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        workerCoordinates, workerClassName, dryRun, parameters, selectedClassPathRoots);
  }

  @Override
  public String toString() {
    return "Configuration{"
        + "dryRun="
        + dryRun
        + ", parameters="
        + parameters
        + ", selectedClassPathRoots="
        + selectedClassPathRoots
        + '}';
  }

  public byte[] toBytes() {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos)) {
      out.writeObject(this);
      return bos.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException("Writing configuration to byte[] failed", e);
    }
  }

  public static Configuration fromBytes(byte[] bytes) {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = new ObjectInputStream(bis)) {
      return (Configuration) in.readObject();
    } catch (IOException e) {
      throw new UncheckedIOException(
          "Reading configuration from byte[" + bytes.length + "] failed", e);
    } catch (ClassNotFoundException e) {
      throw new AssertionError("Configuration class not found?!", e);
    }
  }

  static String implementationVersion(String defaultVersion) {
    String version = Configuration.class.getPackage().getImplementationVersion();
    return version != null ? version : defaultVersion;
  }
}
