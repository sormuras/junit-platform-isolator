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

  private final Basic basic = new Basic();
  private final Discovery discovery = new Discovery();
  private final Launcher launcher = new Launcher();

  /** Basic isolator and worker configuration. */
  public class Basic implements Serializable {

    final String WORKER_GROUP = "de.sormuras.junit-platform-isolator";
    final String WORKER_ARTIFACT = "junit-platform-isolator-worker";
    final String WORKER_VERSION = Isolator.implementationVersion("1.0.0-SNAPSHOT");

    /** Maven coordinates of the artifact providing the worker implementation. */
    public String workerCoordinates = WORKER_GROUP + ':' + WORKER_ARTIFACT + ':' + WORKER_VERSION;

    /** Class name of the worker to instantiate. */
    public String workerClassName = "de.sormuras.junit.platform.isolator.worker.Worker";

    /** Discover tests only, i.e. don't execute them. */
    public boolean dryRun = false;

    /** Fail and return exit status code 2 if no tests are found. */
    public boolean failIfNoTests = true;

    /** Use platform or thread context classloader. */
    public boolean platformClassLoader = true;

    /** Enable Java language {@code assert} statements. */
    public boolean defaultAssertionStatus = true;

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
          && workerCoordinates.equals(basic.workerCoordinates)
          && workerClassName.equals(basic.workerClassName);
    }

    @Override
    public int hashCode() {
      return Objects.hash(
          workerCoordinates,
          workerClassName,
          dryRun,
          failIfNoTests,
          platformClassLoader,
          defaultAssertionStatus);
    }

    @Override
    public String toString() {
      return "Basic{"
          + "workerCoordinates='"
          + workerCoordinates
          + '\''
          + ", workerClassName='"
          + workerClassName
          + '\''
          + ", dryRun="
          + dryRun
          + ", failIfNoTests="
          + failIfNoTests
          + ", platformClassLoader="
          + platformClassLoader
          + ", defaultAssertionStatus="
          + defaultAssertionStatus
          + '}';
    }
  }

  /** Launcher discovery request configuration. */
  public class Discovery implements Serializable {

    /** https://junit.org/junit5/docs/current/user-guide/#running-tests-config-params */
    public Map<String, String> parameters = Collections.emptyMap();

    /** Select `class-path` roots. */
    public Set<String> selectedClassPathRoots = Collections.emptySet();

    private Discovery() {}

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Discovery)) return false;
      Discovery discovery = (Discovery) o;
      return parameters.equals(discovery.parameters)
          && selectedClassPathRoots.equals(discovery.selectedClassPathRoots);
    }

    @Override
    public int hashCode() {
      return Objects.hash(parameters, selectedClassPathRoots);
    }

    @Override
    public String toString() {
      return "Discovery{"
          + "parameters="
          + parameters
          + ", selectedClassPathRoots="
          + selectedClassPathRoots
          + '}';
    }
  }

  /** Launcher factory configuration. */
  public class Launcher implements Serializable {

    /**
     * Determine if test engines should be discovered at runtime using the {@link
     * java.util.ServiceLoader ServiceLoader} mechanism and automatically registered.
     */
    public boolean testEngineAutoRegistration = true;

    /**
     * Determine if test execution listeners should be discovered at runtime using the {@link
     * java.util.ServiceLoader ServiceLoader} mechanism and automatically registered.
     */
    public boolean testExecutionListenerAutoRegistration = true;

    private Launcher() {}

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
      return "Launcher{"
          + "testEngineAutoRegistration="
          + testEngineAutoRegistration
          + ", testExecutionListenerAutoRegistration="
          + testExecutionListenerAutoRegistration
          + '}';
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
