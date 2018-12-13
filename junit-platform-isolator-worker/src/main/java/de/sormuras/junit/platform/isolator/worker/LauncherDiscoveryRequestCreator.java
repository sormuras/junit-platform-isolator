package de.sormuras.junit.platform.isolator.worker;

import de.sormuras.junit.platform.isolator.Configuration;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

class LauncherDiscoveryRequestCreator {

  LauncherDiscoveryRequest create(Configuration configuration) {
    LauncherDiscoveryRequestBuilder requestBuilder = request();
    // selectors
    Set<Path> classPathRoots = new LinkedHashSet<>();
    for (String root : configuration.getSelectedClassPathRoots()) {
      classPathRoots.add(Paths.get(root));
    }
    requestBuilder.selectors(selectClasspathRoots(classPathRoots));
    // TODO filters
    //   if (!mojo.getTags().isEmpty()) {
    //     builder.filters(TagFilter.includeTags(mojo.getTags()));
    //   }
    // parameters
    requestBuilder.configurationParameters(configuration.getParameters());
    return requestBuilder.build();
  }

}
