package tests;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import java.util.List;
import org.junit.jupiter.api.Test;

class ClassLoaderStackTests {

  @Test
  void maven() {
    var result = new Tests(1).maven("classloader-stack");

    // var expectedLines = List.of(">> BEGIN >>", "[INFO] BUILD SUCCESS", ">> END. >>");
    // assertLinesMatch(expectedLines, result.getOutputLines("out"));

    var expectedLines =
        List.of(
            ">> BEGIN >>",
            "\\Q[ERROR]   MainTests.test_should_not_see_testengine\\E.*",
            "\\Q[ERROR]   MainTests.tested_class_should_not_see_engine_class\\E.*",
            "[INFO] ",
            "[ERROR] Tests run: 4, Failures: 2, Errors: 0, Skipped: 0",
            ">> MORE LINES >>",
            "[INFO] BUILD FAILURE",
            ">> END. >>");
    assertLinesMatch(expectedLines, result.getOutputLines("out"));
  }
}
