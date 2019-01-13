package de.sormuras.junit.platform.isolator;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 *
 *
 * <ul>
 *   <li>{@code --add-reads module=target-module(,target-module)*}
 *       <p>Updates module to read the target-module, regardless of the module declaration.
 *       target-module can be all unnamed to read all unnamed modules.
 *   <li>{@code --add-exports module/package=target-module(,target-module)*}
 *       <p>Updates module to export package to target-module, regardless of module declaration. The
 *       target-module can be all unnamed to export to all unnamed modules.
 *   <li>{@code --add-opens module/package=target-module(,target-module)*}
 *       <p>Updates module to open package to target-module, regardless of module declaration.
 * </ul>
 */
class ModuleLayerUpdater implements Consumer<String> {

  private final Driver driver;
  private final ModuleLayer.Controller controller;
  private String operation = null;

  ModuleLayerUpdater(Driver driver, ModuleLayer.Controller controller) {
    this.driver = driver;
    this.controller = controller;
  }

  @Override
  public void accept(String line) {
    // line no. 0, 2, 4,... starts with "--add-[exports|opens|reads]"
    if (operation == null) {
      operation = line;
      driver.debug("Stored `{0}` for next round.", line);
      return;
    }

    // line no. 1, 3, 5,... are arguments "source[/package]=target(,target)*
    var option = operation;
    operation = null;
    driver.debug("Handling `{0}` operator: `{1}`", option, line);

    var split = line.split("=");
    var sourceName = extractModuleName(split[0]);
    var packageName = extractPackageName(split[0]);
    var targetNames = split[1].split(",");
    driver.debug(" o sourceName = `{0}`", sourceName);
    driver.debug(" o packageName = `{0}`", packageName);
    driver.debug(" o targetNames = {0}", Arrays.asList(targetNames));

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

  static String extractModuleName(String moduleNameWithOptionalPackageName) {
    int index = moduleNameWithOptionalPackageName.indexOf("/");
    if (index < 0) {
      return moduleNameWithOptionalPackageName;
    }
    return moduleNameWithOptionalPackageName.substring(0, index);
  }

  static String extractPackageName(String moduleNameWithOptionalPackageName) {
    int index = moduleNameWithOptionalPackageName.indexOf("/");
    if (index < 0) {
      return "";
    }
    return moduleNameWithOptionalPackageName.substring(index + 1);
  }
}
