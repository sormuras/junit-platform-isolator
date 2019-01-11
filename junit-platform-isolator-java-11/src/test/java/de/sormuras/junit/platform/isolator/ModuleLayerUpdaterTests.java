package de.sormuras.junit.platform.isolator;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ModuleLayerUpdaterTests {

  @Test
  void moduleName() {
    assertEquals("m", ModuleLayerUpdater.extractModuleName("m"));
    assertEquals("m", ModuleLayerUpdater.extractModuleName("m/p"));
    assertEquals("mod", ModuleLayerUpdater.extractModuleName("mod/pac"));
  }

  @Test
  void packageName() {
    assertEquals("", ModuleLayerUpdater.extractPackageName("m"));
    assertEquals("p", ModuleLayerUpdater.extractPackageName("m/p"));
    assertEquals("pac", ModuleLayerUpdater.extractPackageName("mod/pac"));
  }
}
