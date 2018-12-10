package stack;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class MainTests {

  @Test
  void test_should_see_tested_class() throws Exception {
    assertNotNull(getClass().getResourceAsStream("Main.class"));
  }

  @Test
  void test_should_not_see_testengine() throws Exception {
    assertNull(getClass().getResourceAsStream("/org/junit/jupiter/engine/JupiterTestEngine.class"));
  }

  @Test
  void tested_class_should_not_see_test_class() throws Exception {
    new Main().lookupTestClass();
  }

  @Test
  void tested_class_should_not_see_engine_class() throws Exception {
    new Main().lookupEngineClass();
  }
}
