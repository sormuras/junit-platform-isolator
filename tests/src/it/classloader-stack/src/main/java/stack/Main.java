package stack;

class Main {

  void lookupTestClass() {
    assert getClass().getResourceAsStream("MainTest.class") == null;
  }

  void lookupEngineClass() {
    assert getClass().getResourceAsStream("/org/junit/jupiter/engine/JupiterTestEngine.class")
        == null;
  }
}
