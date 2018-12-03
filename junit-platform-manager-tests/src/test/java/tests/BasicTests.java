package tests;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import java.util.List;
import org.junit.jupiter.api.Test;

class BasicTests {

  @Test
  void gradle() {
    var result = new Tests().gradle("basic");

    var expectedLines = List.of(">> BEGIN >>", ".* BUILD SUCCESSFUL in \\d+s", ">> END. >>");
    assertLinesMatch(expectedLines, result.getOutputLines("out"));
  }

  @Test
  void maven() {
    var result = new Tests().maven("basic");

    var expectedLines = List.of(">> BEGIN >>", "[INFO] BUILD SUCCESS", ">> END. >>");
    assertLinesMatch(expectedLines, result.getOutputLines("out"));
  }
}
