package de.sormuras.junit.platform.isolator;

import java.lang.module.ModuleFinder;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Set;

public enum OverlaySingleton implements Overlay {
  INSTANCE {
    @Override
    public URLClassLoader newClassLoader(String name, ClassLoader parent, URL... urls) {
      return new URLClassLoader(name, urls, parent);
    }

    @Override
    public ClassLoader newModuleLoader(Set<String> modules, ClassLoader parent, Path... entries) {
      var finder = ModuleFinder.of(entries);
      var bootLayer = ModuleLayer.boot();
      var configuration = bootLayer.configuration().resolve(finder, ModuleFinder.of(), modules);
      var parentLoader = ClassLoader.getSystemClassLoader();
      var configuredLayer = bootLayer.defineModulesWithOneLoader(configuration, parentLoader);
      var name = "de.sormuras.junit.platform.isolator.worker"; // TODO Or one of modules?
      return configuredLayer.findLoader(name);
    }

    @Override
    public ClassLoader platformClassLoader() {
      return ClassLoader.getPlatformClassLoader();
    }
  }
}
