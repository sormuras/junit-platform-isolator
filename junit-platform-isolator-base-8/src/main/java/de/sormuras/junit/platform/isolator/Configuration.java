package de.sormuras.junit.platform.isolator;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.net.URI;
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

    boolean dryRun = false;
    boolean failIfNoTests = true;
    boolean platformClassLoader = true;
    boolean defaultAssertionStatus = true;
    String targetDirectory = "target/junit-platform";
    String workerCoordinates = GroupArtifact.ISOLATOR_WORKER.toStringWithDefaultVersion();
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

    Set<URI> selectedUris = emptySet();
    Set<String> selectedFiles = emptySet();
    Set<String> selectedDirectories = emptySet();
    Set<String> selectedPackages = emptySet();
    Set<String> selectedClasses = emptySet();
    Set<String> selectedMethods = emptySet();
    Set<String> selectedClasspathResources = emptySet();
    Set<String> selectedClasspathRoots = emptySet();
    Set<String> selectedModules = emptySet();

    Set<String> filterClassNamePatterns = singleton("^(Test.*|.+[.$]Test.*|.*Tests?)$");
    Set<String> filterTags = emptySet();

    Map<String, String> parameters = emptyMap();

    private Discovery() {}

    public Set<URI> getSelectedUris() {
      return selectedUris;
    }

    public Set<String> getSelectedFiles() {
      return selectedFiles;
    }

    public Set<String> getSelectedDirectories() {
      return selectedDirectories;
    }

    public Set<String> getSelectedPackages() {
      return selectedPackages;
    }

    public Set<String> getSelectedClasses() {
      return selectedClasses;
    }

    public Set<String> getSelectedMethods() {
      return selectedMethods;
    }

    public Set<String> getSelectedClasspathResources() {
      return selectedClasspathResources;
    }

    public Set<String> getSelectedClasspathRoots() {
      return selectedClasspathRoots;
    }

    public Set<String> getSelectedModules() {
      return selectedModules;
    }

    public Set<String> getFilterClassNamePatterns() {
      return filterClassNamePatterns;
    }

    public Set<String> getFilterTags() {
      return filterTags;
    }

    public Map<String, String> getParameters() {
      return parameters;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Discovery discovery = (Discovery) o;
      return selectedUris.equals(discovery.selectedUris)
          && selectedFiles.equals(discovery.selectedFiles)
          && selectedDirectories.equals(discovery.selectedDirectories)
          && selectedPackages.equals(discovery.selectedPackages)
          && selectedClasses.equals(discovery.selectedClasses)
          && selectedMethods.equals(discovery.selectedMethods)
          && selectedClasspathResources.equals(discovery.selectedClasspathResources)
          && selectedClasspathRoots.equals(discovery.selectedClasspathRoots)
          && selectedModules.equals(discovery.selectedModules)
          && filterClassNamePatterns.equals(discovery.filterClassNamePatterns)
          && filterTags.equals(discovery.filterTags)
          && parameters.equals(discovery.parameters);
    }

    @Override
    public int hashCode() {
      return Objects.hash(
          selectedUris,
          selectedFiles,
          selectedDirectories,
          selectedPackages,
          selectedClasses,
          selectedMethods,
          selectedClasspathResources,
          selectedClasspathRoots,
          selectedModules,
          filterClassNamePatterns,
          filterTags,
          parameters);
    }

    @Override
    public String toString() {
      return new StringJoiner(", ", Discovery.class.getSimpleName() + "[", "]")
          .add("selectedUris=" + selectedUris)
          .add("selectedFiles=" + selectedFiles)
          .add("selectedDirectories=" + selectedDirectories)
          .add("selectedPackages=" + selectedPackages)
          .add("selectedClasses=" + selectedClasses)
          .add("selectedMethods=" + selectedMethods)
          .add("selectedClasspathResources=" + selectedClasspathResources)
          .add("selectedClasspathRoots=" + selectedClasspathRoots)
          .add("selectedModules=" + selectedModules)
          .add("filterClassNamePatterns=" + filterClassNamePatterns)
          .add("filterTags=" + filterTags)
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
