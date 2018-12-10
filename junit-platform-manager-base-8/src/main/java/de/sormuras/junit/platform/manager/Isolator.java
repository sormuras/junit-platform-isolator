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

package de.sormuras.junit.platform.manager;

import static de.sormuras.junit.platform.manager.GroupArtifact.JUNIT_JUPITER_ENGINE;
import static de.sormuras.junit.platform.manager.GroupArtifact.JUNIT_PLATFORM_LAUNCHER;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Isolator {

  private static final Overlay overlay = OverlaySingleton.INSTANCE;

  private final Map<String, Set<Path>> paths;
  private final Resolver resolver;
  private final Function<Version, String> version;

  public Isolator(
      Resolver resolver, Map<String, Set<Path>> paths, Function<Version, String> version) {
    this.paths = paths;
    this.resolver = resolver;
    this.version = version;
  }

  ClassLoader createClassLoader(String workerCoordinates) throws Exception {
    //
    // Main and Test source sets
    //
    Set<Path> mainPaths = paths.get("main");
    Set<Path> testPaths = paths.get("test");

    testPaths.removeAll(mainPaths);
    // TODO removeAllTestEngines(testPaths);

    //
    // JUnit Platform Launcher + Engine and all TestEngine implementations
    //
    Set<Path> platformPaths = new LinkedHashSet<>();
    platformPaths.addAll(resolver.resolveAll(JUNIT_PLATFORM_LAUNCHER.toString(version)));
    platformPaths.addAll(resolver.resolveAll(JUNIT_JUPITER_ENGINE.toString(version)));

    platformPaths.removeAll(mainPaths);
    platformPaths.removeAll(testPaths);

    //
    // Worker + Manager
    //
    Set<Path> managerPaths = resolver.resolveAll(workerCoordinates);

    ClassLoader rootLoader = overlay.platformClassLoader();
    ClassLoader mainLoader = new URLClassLoader(urls(mainPaths), rootLoader);
    ClassLoader testLoader = new URLClassLoader(urls(testPaths), mainLoader);
    ClassLoader platformLoader = new URLClassLoader(urls(platformPaths), testLoader);
    ClassLoader managerLoader = new URLClassLoader(urls(managerPaths), platformLoader);

    testLoader.setDefaultAssertionStatus(true);

    return managerLoader;
  }

  private static URL[] urls(Collection<Path> paths) {
    return paths.stream().map(Path::toUri).map(Isolator::toUrl).toArray(URL[]::new);
  }

  private static URL toUrl(URI uri) {
    try {
      return uri.toURL();
    } catch (MalformedURLException e) {
      throw rethrow(e);
    }
  }

  private static UndeclaredThrowableException rethrow(Throwable cause) {
    if (cause instanceof RuntimeException) {
      throw (RuntimeException) cause;
    }
    if (cause instanceof Error) {
      throw (Error) cause;
    }
    return new UndeclaredThrowableException(cause);
  }
}
