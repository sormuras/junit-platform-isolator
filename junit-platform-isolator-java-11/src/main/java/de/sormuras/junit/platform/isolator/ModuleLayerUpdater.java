package de.sormuras.junit.platform.isolator;

import java.util.function.Consumer;

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

  private final ModuleLayer.Controller controller;
  private String operation = null;

  ModuleLayerUpdater(ModuleLayer.Controller controller) {
    this.controller = controller;
  }

  @Override
  public void accept(String line) {
    // line no. 0, 2, 4,... starts with "--add-[exports|opens|reads]"
    if (operation == null) {
      operation = line;
      return;
    }

    // line no. 1, 3, 5,... are arguments "source[/package]=target(,target)*
    var option = operation;
    operation = null;
    var split = line.split("=");
    var sourceName = extractModuleName(split[0]);
    var packageName = extractPackageName(split[0]);
    var source = controller.layer().findModule(sourceName).orElseThrow();
    var targetNames = split[1].split(",");
    for (var targetName : targetNames) {
      var target = controller.layer().findModule(targetName).orElseThrow();
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
