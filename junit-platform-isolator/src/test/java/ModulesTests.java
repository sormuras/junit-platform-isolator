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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.sormuras.junit.platform.isolator.Modules;
import de.sormuras.junit.platform.isolator.TestMode;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

class ModulesTests {

  @Test
  void modeClassic() {
    var main = Paths.get("main");
    var test = Paths.get("test");

    assertEquals(TestMode.CLASSIC, new Modules(main, test).getMode());
  }

  @Test
  void modeModular() {
    var main = path("jars/slf4j-api-1.8.0-beta2.jar");
    var test = path("jars/junit-platform-commons-1.2.0.jar");

    assertEquals(TestMode.MODULAR, new Modules(main, test).getMode());
    assertEquals(TestMode.MODULAR, new Modules(Paths.get("main"), test).getMode());
  }

  @Test
  void modePatchedTestCompile() {
    var main = path("jars/slf4j-api-1.8.0-beta2.jar");
    var test = path("jars/slf4j-api-1.8.0-beta2.jar");

    assertEquals(TestMode.MODULAR_PATCHED_TEST_COMPILE, new Modules(main, test).getMode());
  }

  @Test
  void modePatchedTestRuntime() {
    var main = path("jars/slf4j-api-1.8.0-beta2.jar");
    var test = Paths.get("test");

    assertEquals(TestMode.MODULAR_PATCHED_TEST_RUNTIME, new Modules(main, test).getMode());
  }

  @Test
  void moduleReferences() {
    var main = path("jars/slf4j-api-1.8.0-beta2.jar");
    var test = path("jars/junit-platform-commons-1.2.0.jar");
    var modules = new Modules(main, test);

    assertTrue(modules.getMainModuleReference().isPresent());
    var mainDescriptor = modules.getMainModuleReference().get().descriptor();
    assertEquals("org.slf4j", mainDescriptor.name());
    assertFalse(mainDescriptor.isAutomatic());

    assertTrue(modules.getTestModuleReference().isPresent());
    var testDescriptor = modules.getTestModuleReference().get().descriptor();
    assertEquals("org.junit.platform.commons", testDescriptor.name());
    assertTrue(testDescriptor.isAutomatic());
  }

  private static Path path(String name) {
    try {
      var url = ModulesTests.class.getClassLoader().getResource(name);
      assertNotNull(url, "Resource not found: " + name);
      var path = Paths.get(url.toURI());
      assertTrue(Files.isReadable(path));
      return path;
    } catch (URISyntaxException e) {
      throw new AssertionError("Resource with bad URI?! " + name, e);
    }
  }
}
