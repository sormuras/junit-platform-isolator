package de.sormuras.junit.platform.isolator;

import static java.lang.ModuleLayer.boot;
import static java.lang.ModuleLayer.defineModulesWithOneLoader;

import java.lang.module.ModuleFinder;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public enum OverlaySingleton implements Overlay {
  INSTANCE {
    @Override
    public URLClassLoader newClassLoader(String name, ClassLoader parent, URL... urls) {
      return new URLClassLoader(name, urls, parent);
    }

    @Override
    public ClassLoader newModuleLoader(
        Driver driver, Configuration configuration, ClassLoader parentLoader) {
      var selectedModules = configuration.discovery().getSelectedModules();
      if (selectedModules.isEmpty()) {
        throw new IllegalStateException("No module selected!");
      }

      // TODO For now, merge all entries into a single layer...
      //      https://github.com/sormuras/junit-platform-isolator/issues/9
      Path[] entries =
          configuration
              .basic()
              .toPaths()
              .values()
              .stream()
              .flatMap(Collection::stream)
              .distinct()
              .toArray(Path[]::new);

      var finder = ModuleFinder.of(entries);
      var moduleConfig = boot().configuration().resolve(finder, ModuleFinder.of(), selectedModules);
      var parentLayers = List.of(boot());
      var controller = defineModulesWithOneLoader(moduleConfig, parentLayers, parentLoader);
      // TODO Read `module-info.test` to configure module layer at runtime
      //   https://github.com/sormuras/junit-platform-isolator/issues/10
      //   controller.addExports();
      //   controller.addOpens();
      //   controller.addReads();
      var name = selectedModules.toArray()[0].toString();
      return controller.layer().findLoader(name);
    }

    @Override
    public ClassLoader platformClassLoader() {
      return ClassLoader.getPlatformClassLoader();
    }
  }
}
