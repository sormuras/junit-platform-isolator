/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.sormuras.junit.platform.isolator;

import static java.util.stream.Collectors.joining;

import java.lang.module.FindException;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

public class Modules {

  private final ModuleReference mainModuleReference;
  private final ModuleReference testModuleReference;
  private final TestMode mode;

  public Modules(Path mainPath, Path testPath) {
    this.mainModuleReference = getSingleModuleReferenceOrNull(mainPath);
    this.testModuleReference = getSingleModuleReferenceOrNull(testPath);
    this.mode = TestMode.of(getName(mainModuleReference), getName(testModuleReference));
  }

  public TestMode getMode() {
    return mode;
  }

  public Optional<String> getMainModuleName() {
    return Optional.ofNullable(getName(mainModuleReference));
  }

  public Optional<ModuleReference> getMainModuleReference() {
    return Optional.ofNullable(mainModuleReference);
  }

  public Optional<String> getTestModuleName() {
    return Optional.ofNullable(getName(testModuleReference));
  }

  public Optional<ModuleReference> getTestModuleReference() {
    return Optional.ofNullable(testModuleReference);
  }

  @Override
  public String toString() {
    return String.format(
        "Modules(11) [main=%s, test=%s]", toStringMainModule(), toStringTestModule());
  }

  public String toStringMainModule() {
    return toString(mainModuleReference);
  }

  public String toStringTestModule() {
    return toString(testModuleReference);
  }

  private static String getName(ModuleReference reference) {
    if (reference == null) {
      return null;
    }
    return reference.descriptor().name();
  }

  private static ModuleReference getSingleModuleReferenceOrNull(Path path) {
    Set<ModuleReference> all;
    try {
      all = ModuleFinder.of(path).findAll();
    } catch (FindException e) {
      // e.printStackTrace();
      return null;
    }
    var firstOpt = all.stream().findFirst();
    switch (all.size()) {
      case 0:
        return null;
      case 1:
        return firstOpt.get();
      default:
        throw new IllegalArgumentException(
            "expected exactly one module in "
                + path
                + " but found: "
                + all.stream().map(ModuleReference::toString).collect(joining(", ", "<", ">")));
    }
  }

  private static String toString(ModuleReference reference) {
    if (reference == null) {
      return "<empty>";
    }
    var module = reference.descriptor();
    var builder = new StringBuilder();
    if (module.isOpen()) {
      builder.append("open ");
    }
    builder.append("module ").append(module.name());
    builder.append(" {");
    builder
        .append(" requires=")
        .append(
            module.requires().stream()
                .map(ModuleDescriptor.Requires::name)
                .sorted()
                .collect(joining(", ", "[", "]")));
    builder.append(" packages=").append(module.packages());
    builder.append(" }");
    return builder.toString();
  }
}
