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

import java.nio.file.Path;
import java.util.Optional;

public class Modules {

  private final TestMode mode;
  private final Path mainPath;
  private final Path testPath;

  public Modules(Path mainPath, Path testPath) {
    this.mainPath = mainPath;
    this.testPath = testPath;
    this.mode = TestMode.CLASSIC;
  }

  public TestMode getMode() {
    return mode;
  }

  public Optional<String> getMainModuleName() {
    return Optional.empty();
  }

  public Optional<Object> getMainModuleReference() {
    return Optional.empty();
  }

  public Optional<String> getTestModuleName() {
    return Optional.empty();
  }

  public Optional<Object> getTestModuleReference() {
    return Optional.empty();
  }

  @Override
  public String toString() {
    return String.format("Modules [main=%s, test=%s]", toStringMainModule(), toStringTestModule());
  }

  public String toStringMainModule() {
    return mainPath.toString();
  }

  public String toStringTestModule() {
    return testPath.toString();
  }
}
