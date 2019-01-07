package de.sormuras.junit.platform.isolator;

import static java.lang.ModuleLayer.boot;
import static java.lang.ModuleLayer.defineModulesWithOneLoader;

import java.lang.module.ModuleFinder;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
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
      var configuration = boot().configuration().resolve(finder, ModuleFinder.of(), modules);
      var parentLayers = List.of(boot());
      var parentLoader = ClassLoader.getSystemClassLoader(); // TODO ... parent?!
      var controller = defineModulesWithOneLoader(configuration, parentLayers, parentLoader);
      // TODO Read `module-info.test` to configure module layer at runtime
      //   https://github.com/sormuras/junit-platform-isolator/issues/10
      //   controller.addExports();
      //   controller.addOpens();
      //   controller.addReads();
      var name = "de.sormuras.junit.platform.isolator.worker"; // TODO Or one of modules?
      return controller.layer().findLoader(name);
    }

    @Override
    public ClassLoader platformClassLoader() {
      return ClassLoader.getPlatformClassLoader();
    }
  }
}
