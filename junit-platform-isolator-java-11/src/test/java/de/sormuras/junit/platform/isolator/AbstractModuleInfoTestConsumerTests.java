package de.sormuras.junit.platform.isolator;

import static de.sormuras.junit.platform.isolator.AbstractModuleInfoTestConsumer.extractModuleName;
import static de.sormuras.junit.platform.isolator.AbstractModuleInfoTestConsumer.extractPackageName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AbstractModuleInfoTestConsumerTests {

  @Test
  void moduleName() {
    assertEquals("m", extractModuleName("m"));
    assertEquals("m", extractModuleName("m/p"));
    assertEquals("mod", extractModuleName("mod/pac"));
  }

  @Test
  void packageName() {
    assertEquals("", extractPackageName("m"));
    assertEquals("p", extractPackageName("m/p"));
    assertEquals("pac", extractPackageName("mod/pac"));
  }
}
