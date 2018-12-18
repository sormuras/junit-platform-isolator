package de.sormuras.junit.platform.isolator.worker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DiscoveryCreatorTests {

  @Test
  void testCheckAsPaths() {
    Optional<Set<Path>> paths = DiscoveryCreator.checkAsPaths(Collections.singleton("123"));
    assertEquals("123", paths.orElseThrow(AssertionError::new).iterator().next().toString());
    assertFalse(DiscoveryCreator.checkAsPaths(null).isPresent());
    assertFalse(DiscoveryCreator.checkAsPaths(Collections.emptySet()).isPresent());
  }

  @Test
  void testCheckStrings() {
    Optional<Set<String>> strings = DiscoveryCreator.checkStrings(Collections.singleton("123"));
    assertEquals("123", strings.orElseThrow(AssertionError::new).iterator().next());
    assertFalse(DiscoveryCreator.checkStrings(null).isPresent());
    assertFalse(DiscoveryCreator.checkStrings(Collections.emptySet()).isPresent());
  }

  @Test
  void testToList() {
    List<String> strings = DiscoveryCreator.toList(Collections.singleton("123"));
    assertEquals("123", strings.get(0));
  }

  @Test
  void testToStringArray() {
    String[] strings = DiscoveryCreator.toStringArray(Collections.singleton("123"));
    assertEquals("123", strings[0]);
  }
}
