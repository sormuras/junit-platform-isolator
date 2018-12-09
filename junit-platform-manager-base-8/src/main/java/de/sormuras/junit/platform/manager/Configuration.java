package de.sormuras.junit.platform.manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/** Manager configuration. */
public class Configuration implements Serializable {

  private String workerCoordinates =
      "com.github.sormuras.junit-platform-manager"
          + ':'
          + "junit-platform-manager-worker"
          + ':'
          + "master-SNAPSHOT";

  private String workerClassName = "de.sormuras.junit.platform.manager.worker.Worker";

  private boolean dryRun = false;

  private Map<String, String> parameters = Collections.emptyMap();

  private Set<Path> selectedClassPathRoots = Collections.emptySet();

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

  /** Select `class-path` roots. */
  public Set<Path> getSelectedClassPathRoots() {
    return selectedClassPathRoots;
  }

  /** Select `class-path` roots. */
  public Configuration setSelectedClassPathRoots(Set<Path> paths) {
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
      throw new UncheckedIOException("Writing configuration to byte[] failed", e);
    } catch (ClassNotFoundException e) {
      throw new AssertionError("Configuration class not found?!", e);
    }
  }
}
