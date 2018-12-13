package de.sormuras.junit.platform.isolator.worker;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;
import static org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder.request;

import de.sormuras.junit.platform.isolator.Configuration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;

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
