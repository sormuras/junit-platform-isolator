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
import java.util.List;

@SuppressWarnings("WeakerAccess")
class Tests {

  private final int expectedExitCode;

  Tests() {
    this.expectedExitCode = 0;
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

    var result = tool.run(configuration.build());

    if (result.getExitCode() != expectedExitCode) {
      System.out.printf(
          "Expected exit code of %d, got: %d", expectedExitCode, result.getExitCode());
      result.getOutputLines("out").forEach(System.out::println);
      result.getOutputLines("err").forEach(System.err::println);
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
              .replaceAll("@project.version@", "1.0.0-SNAPSHOT")
              .replaceAll("@junit.jupiter.version@", "5.3.2");
      Files.writeString(path, target);
    } catch (IOException e) {
      throw new UncheckedIOException("Replacing placeholders failed!", e);
    }
  }
}
