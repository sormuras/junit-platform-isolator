package tests;

import de.sormuras.bartholdy.Bartholdy;
import de.sormuras.bartholdy.Configuration;
import de.sormuras.bartholdy.Result;
import de.sormuras.bartholdy.Tool;
import de.sormuras.bartholdy.tool.Gradle;
import de.sormuras.bartholdy.tool.Maven;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

@SuppressWarnings("WeakerAccess")
class Tests {

  private final int expectedExitCode;

  Tests() {
    this(0);
  }

  Tests(int expectedExitCode) {
    this.expectedExitCode = expectedExitCode;
  }

  Result gradle(String name) {
    var gradle = Gradle.install("5.0", Path.of("cache", "tools"));
    return run(gradle, name, "gradle", "build", "--debug");
  }

  Result maven(String name) {
    return maven(name, "maven", "verify", "--debug");
  }

  Result maven(String name, String variant, String... args) {
    var maven = Maven.install("3.6.0", Path.of("cache", "tools"));
    return run(maven, name, variant, args);
  }

  Result run(Tool tool, String name, String variant, String... args) {
    var template = Path.of("src", "it", name);
    var workspace = Path.of("target", "it", name, variant);
    if (Files.exists(workspace)) {
      Bartholdy.treeDelete(workspace);
    }
    Bartholdy.treeCopy(template, workspace);
    replacePlaceholders(workspace.resolve("build.gradle"));
    replacePlaceholders(workspace.resolve("pom.xml"));

    var configuration = Configuration.builder();
    configuration.setWorkingDirectory(workspace);
    configuration.setArguments(List.of(args));
    configuration.setTimeout(Duration.ofMinutes(1));

    var result = tool.run(configuration.build());

    writeLogs(workspace, result);

    if (result.isTimedOut()) {
      throw new AssertionError("Timed-out: " + configuration.getTimeout());
    }
    if (result.getExitCode() != expectedExitCode) {
      throw new AssertionError(
          "Exit code: " + result.getExitCode() + "(expected=" + expectedExitCode + ")");
    }

    return result;
  }

  void replacePlaceholders(Path path) {
    if (Files.notExists(path)) {
      return;
    }
    try {
      var source = Files.readString(path);
      var target =
          source
              .replaceAll("@project.version@", "1.0.0-M11")
              .replaceAll("@junit.jupiter.version@", "5.3.2");
      Files.writeString(path, target);
    } catch (IOException e) {
      throw new UncheckedIOException("Replacing placeholders failed!", e);
    }
  }

  void writeLogs(Path directory, Result result) {
    try {
      Files.write(directory.resolve("run-err.log"), result.getOutputLines("err"));
      Files.write(directory.resolve("run-out.log"), result.getOutputLines("out"));
    } catch (IOException e) {
      // ignore
    }
  }
}
