package tests;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import java.util.List;
import org.junit.jupiter.api.Test;

class BasicTests {

  //  @Test
  //  void gradle() {
  //    var result = new Tests().gradle("basic");
  //
  //    var expectedLines =
  //        List.of(
  //            ">> BEGIN >>",
  //            ".* basic.ApplicationTests PASSED",
  //            ">> MORE LINES >>",
  //            ".* BUILD SUCCESSFUL in \\d+s",
  //            ">> END. >>");
  //    assertLinesMatch(expectedLines, result.getOutputLines("out"));
  //  }

  @Test
  void maven() {
    var result = new Tests().maven("basic");

    var expectedLines =
        List.of(
            ">> BEGIN >>",
            "\\Q[INFO] Launching JUnit Platform\\E.*",
            // "\\Q[INFO] Successfully executed 2 test(s) in \\E.+ ms",
            ">> MORE LINES >>",
            "[INFO] BUILD SUCCESS",
            ">> END. >>");
    assertLinesMatch(expectedLines, result.getOutputLines("out"));
  }
}
