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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

/** Isolator configuration. */
public class Configuration implements Serializable {

  private final Basic basic = new Basic();
  private final Discovery discovery = new Discovery();
  private final Launcher launcher = new Launcher();

  /** Basic isolator and worker configuration. */
  public static class Basic implements Serializable {

    public static final String WORKER_GROUP = "de.sormuras.junit-platform-isolator";
    public static final String WORKER_ARTIFACT = "junit-platform-isolator-worker";
    public static final String WORKER_VERSION = Isolator.implementationVersion("1.0.0-M5");

    boolean dryRun = false;
    boolean failIfNoTests = true;
    boolean platformClassLoader = true;
    boolean defaultAssertionStatus = true;
    String targetDirectory = "target/junit-platform";
    String workerCoordinates = WORKER_GROUP + ':' + WORKER_ARTIFACT + ':' + WORKER_VERSION;
    String workerClassName = "de.sormuras.junit.platform.isolator.worker.Worker";

    public boolean isDryRun() {
      return dryRun;
    }

    public boolean isFailIfNoTests() {
      return failIfNoTests;
    }

    public boolean isPlatformClassLoader() {
      return platformClassLoader;
    }

    public boolean isDefaultAssertionStatus() {
      return defaultAssertionStatus;
    }

    public String getTargetDirectory() {
      return targetDirectory;
    }

    public String getWorkerCoordinates() {
      return workerCoordinates;
    }

    public String getWorkerClassName() {
      return workerClassName;
    }

    private Basic() {}

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Basic)) return false;
      Basic basic = (Basic) o;
      return dryRun == basic.dryRun
          && failIfNoTests == basic.failIfNoTests
          && platformClassLoader == basic.platformClassLoader
          && defaultAssertionStatus == basic.defaultAssertionStatus
          && targetDirectory.equals(basic.targetDirectory)
          && workerCoordinates.equals(basic.workerCoordinates)
          && workerClassName.equals(basic.workerClassName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(
          workerCoordinates,
          workerClassName,
          targetDirectory,
          dryRun,
          failIfNoTests,
          platformClassLoader,
          defaultAssertionStatus);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", Basic.class.getSimpleName() + "[", "]")
          .add("dryRun=" + dryRun)
          .add("failIfNoTests=" + failIfNoTests)
          .add("platformClassLoader=" + platformClassLoader)
          .add("defaultAssertionStatus=" + defaultAssertionStatus)
          .add("targetDirectory='" + targetDirectory + "'")
          .add("workerCoordinates='" + workerCoordinates + "'")
          .add("workerClassName='" + workerClassName + "'")
          .toString();
    }
  }

  /** Launcher discovery request configuration. */
  public static class Discovery implements Serializable {

    Set<String> selectedClasspathRoots = Collections.emptySet();
    Set<String> selectedModules = Collections.emptySet();
    Set<String> selectedPackages = Collections.emptySet();

    List<String> filterTagsIncluded = Collections.emptyList();
    List<String> filterTagsExcluded = Collections.emptyList();

    Map<String, String> parameters = Collections.emptyMap();

    private Discovery() {}

    public Set<String> getSelectedClasspathRoots() {
      return selectedClasspathRoots;
    }

    public Set<String> getSelectedModules() {
      return selectedModules;
    }

    public Set<String> getSelectedPackages() {
      return selectedPackages;
    }

    public List<String> getFilterTagsIncluded() {
      return filterTagsIncluded;
    }

    public List<String> getFilterTagsExcluded() {
      return filterTagsExcluded;
    }

    public Map<String, String> getParameters() {
      return parameters;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Discovery discovery = (Discovery) o;
      return selectedClasspathRoots.equals(discovery.selectedClasspathRoots)
          && selectedModules.equals(discovery.selectedModules)
          && selectedPackages.equals(discovery.selectedPackages)
          && filterTagsIncluded.equals(discovery.filterTagsIncluded)
          && filterTagsExcluded.equals(discovery.filterTagsExcluded)
          && parameters.equals(discovery.parameters);
    }

    @Override
    public int hashCode() {
      return Objects.hash(
          selectedClasspathRoots,
          selectedModules,
          selectedPackages,
          filterTagsIncluded,
          filterTagsExcluded,
          parameters);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", Discovery.class.getSimpleName() + "[", "]")
          .add("selectedClasspathRoots=" + selectedClasspathRoots)
          .add("selectedModules=" + selectedModules)
          .add("selectedPackages=" + selectedPackages)
          .add("filterTagsIncluded=" + filterTagsIncluded)
          .add("filterTagsExcluded=" + filterTagsExcluded)
          .add("parameters=" + parameters)
          .toString();
    }
  }

  /** Launcher factory configuration. */
  public static class Launcher implements Serializable {

    boolean testEngineAutoRegistration = true;
    boolean testExecutionListenerAutoRegistration = true;

    private Launcher() {}

    public boolean isTestEngineAutoRegistration() {
      return testEngineAutoRegistration;
    }

    public boolean isTestExecutionListenerAutoRegistration() {
      return testExecutionListenerAutoRegistration;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Launcher)) return false;
      Launcher launcher = (Launcher) o;
      return testEngineAutoRegistration == launcher.testEngineAutoRegistration
          && testExecutionListenerAutoRegistration
              == launcher.testExecutionListenerAutoRegistration;
    }

    @Override
    public int hashCode() {
      return Objects.hash(testEngineAutoRegistration, testExecutionListenerAutoRegistration);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", Launcher.class.getSimpleName() + "[", "]")
          .add("testEngineAutoRegistration=" + testEngineAutoRegistration)
          .add("testExecutionListenerAutoRegistration=" + testExecutionListenerAutoRegistration)
          .toString();
    }
  }

  /** Basic isolator and worker configuration. */
  public Basic basic() {
    return basic;
  }

  /** Launcher discovery request configuration. */
  public Discovery discovery() {
    return discovery;
  }

  /** Launcher factory configuration. */
  public Launcher launcher() {
    return launcher;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Configuration)) return false;
    Configuration that = (Configuration) o;
    return basic.equals(that.basic)
        && discovery.equals(that.discovery)
        && launcher.equals(that.launcher);
  }

  @Override
  public int hashCode() {
    return Objects.hash(basic, discovery, launcher);
  }

  @Override
  public String toString() {
    return "Configuration{"
        + "basic="
        + basic
        + ", discovery="
        + discovery
        + ", launcher="
        + launcher
        + '}';
  }

  /** Write this configuration into a byte array. */
  public byte[] toBytes() {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(bos)) {
      out.writeObject(this);
      return bos.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException("Writing configuration to byte[] failed", e);
    }
  }

  /** Read configuration from the byte array. */
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
}
