package de.sormuras.junit.platform.manager;

import java.nio.file.Path;
import java.util.Set;

public interface Resolver {
  /** Resolve single artifact. */
  Path resolve(String coordinates) throws Exception;

  /** Resolve artifact with all transitive dependencies. */
  Set<Path> resolveAll(String coordinates) throws Exception;
}
