package de.sormuras.junit.platform.isolator;

import java.util.Arrays;
import java.util.function.Supplier;

class ModuleLayerUpdater extends AbstractModuleInfoTestConsumer {

  private final ModuleLayer.Controller controller;

  ModuleLayerUpdater(Driver driver, ModuleLayer.Controller controller) {
    super(driver);
    this.controller = controller;
  }

  @Override
  public void accept(String option, String sourceName, String packageName, String[] targetNames) {
    driver.debug(
        "Updating module layer: {0} `{1}[/{2}]`=`{3}`",
        sourceName, packageName, Arrays.asList(targetNames));

    var layer = controller.layer();
    var source = layer.findModule(sourceName).orElseThrow(moduleNotFound(sourceName));
    for (var targetName : targetNames) {
      var target = layer.findModule(targetName).orElseThrow(moduleNotFound(targetName));
      switch (option) {
        case "--add-exports":
          controller.addExports(source, packageName, target);
          break;
        case "--add-opens":
          controller.addOpens(source, packageName, target);
          break;
        case "--add-reads":
          controller.addReads(source, target);
          break;
        default:
          throw new UnsupportedOperationException("Unknown option: " + option);
      }
    }
  }

  private static Supplier<AssertionError> moduleNotFound(String name) {
    return () -> new AssertionError("Module with name `" + name + "` not found!");
  }
}
