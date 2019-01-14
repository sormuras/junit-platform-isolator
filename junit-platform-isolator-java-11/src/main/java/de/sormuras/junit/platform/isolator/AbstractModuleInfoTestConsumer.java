package de.sormuras.junit.platform.isolator;

import java.util.function.Consumer;

/**
 * Line parser.
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
abstract class AbstractModuleInfoTestConsumer implements Consumer<String> {

  final Driver driver;
  private String operation = null;

  AbstractModuleInfoTestConsumer(Driver driver) {
    this.driver = driver;
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

    accept(option, sourceName, packageName, targetNames);
  }

  abstract void accept(String option, String sourceName, String packageName, String[] targetNames);

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
