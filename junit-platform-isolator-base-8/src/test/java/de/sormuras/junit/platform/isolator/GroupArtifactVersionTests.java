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

import static de.sormuras.junit.platform.isolator.GroupArtifact.ISOLATOR;
import static de.sormuras.junit.platform.isolator.GroupArtifact.ISOLATOR_WORKER;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_JUPITER;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_JUPITER_API;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_JUPITER_ENGINE;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_PLATFORM_COMMONS;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_PLATFORM_CONSOLE;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_PLATFORM_LAUNCHER;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_PLATFORM_REPORTING;
import static de.sormuras.junit.platform.isolator.GroupArtifact.JUNIT_VINTAGE_ENGINE;
import static de.sormuras.junit.platform.isolator.Version.ISOLATOR_VERSION;
import static de.sormuras.junit.platform.isolator.Version.JUNIT_JUPITER_VERSION;
import static de.sormuras.junit.platform.isolator.Version.JUNIT_PLATFORM_VERSION;
import static de.sormuras.junit.platform.isolator.Version.JUNIT_VINTAGE_VERSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class GroupArtifactVersionTests {

  /** Expected size of the {@link Version} enum. */
  private static final int SIZE = 4;

  @Test
  void forEachVersionCallsThePassedVisitorEveryTimes() {
    List<Version> versions = new ArrayList<>();
    Version.forEach(versions::add);
    assertEquals(SIZE, versions.size(), "versions=" + versions);
  }

  @Test
  void passingNullAsVersionToCreateArtifactVersionMap() {
    Map<String, String> map = Version.buildMap(__ -> null);
    assertEquals(SIZE, map.size(), "map=" + map);
    assertEquals("1.0.0-M11", map.get("isolator.version"));
    assertEquals("1.3.2", map.get("junit.platform.version"));
    assertEquals("5.3.2", map.get("junit.jupiter.version"));
    assertEquals("5.3.2", map.get("junit.vintage.version"));
  }

  @Test
  void passingDashAsVersionToCreateArtifactVersionMap() {
    Map<String, String> map = Version.buildMap(__ -> "-");
    assertEquals(SIZE, map.size(), "map=" + map);
    assertEquals("-", map.get("isolator.version"));
    assertEquals("-", map.get("junit.platform.version"));
    assertEquals("-", map.get("junit.jupiter.version"));
    assertEquals("-", map.get("junit.vintage.version"));
  }

  @Test
  void passingPresetVersionMapToCreateArtifactVersionMap() {
    Map<String, String> preset = new HashMap<>();
    preset.put("de.sormuras.junit:junit-platform-isolator", "o");
    preset.put("org.junit.platform:junit-platform-commons", "a");
    preset.put("org.junit.jupiter:junit-jupiter-api", "b");
    preset.put("org.junit.vintage:junit-vintage-engine", "c");
    Map<String, String> map = Version.buildMap(preset::get);
    assertEquals(SIZE, map.size(), "map=" + map);
    assertEquals("o", map.get("isolator.version"));
    assertEquals("a", map.get("junit.platform.version"));
    assertEquals("b", map.get("junit.jupiter.version"));
    assertEquals("c", map.get("junit.vintage.version"));
  }

  @Test
  void versionOfGroupArtifactsDoMatch() {
    assertSame(ISOLATOR_VERSION, ISOLATOR.getVersion());
    assertSame(ISOLATOR_VERSION, ISOLATOR_WORKER.getVersion());
    assertSame(JUNIT_PLATFORM_VERSION, JUNIT_PLATFORM_COMMONS.getVersion());
    assertSame(JUNIT_PLATFORM_VERSION, JUNIT_PLATFORM_CONSOLE.getVersion());
    assertSame(JUNIT_PLATFORM_VERSION, JUNIT_PLATFORM_LAUNCHER.getVersion());
    assertSame(JUNIT_PLATFORM_VERSION, JUNIT_PLATFORM_REPORTING.getVersion());
    assertSame(JUNIT_JUPITER_VERSION, JUNIT_JUPITER.getVersion());
    assertSame(JUNIT_JUPITER_VERSION, JUNIT_JUPITER_API.getVersion());
    assertSame(JUNIT_JUPITER_VERSION, JUNIT_JUPITER_ENGINE.getVersion());
    assertSame(JUNIT_VINTAGE_VERSION, JUNIT_VINTAGE_ENGINE.getVersion());
  }
}
