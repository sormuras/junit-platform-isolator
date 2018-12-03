package basic;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class ApplicationTests {

  @Test
  void checkDryRunFlag() {
    assertFalse(new Application().isDryRun());
  }
}
