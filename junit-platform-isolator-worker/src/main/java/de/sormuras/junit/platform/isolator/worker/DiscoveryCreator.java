package de.sormuras.junit.platform.isolator.worker;

import static java.util.stream.Collectors.toSet;
import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathResource;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClasspathRoots;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectDirectory;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectFile;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectModules;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;
import static org.junit.platform.launcher.TagFilter.includeTags;

import de.sormuras.junit.platform.isolator.Configuration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.Filter;

class DiscoveryCreator {

  private final Configuration.Discovery discovery;

  DiscoveryCreator(Configuration.Discovery discovery) {
    this.discovery = discovery;
  }

  List<? extends DiscoverySelector> createDiscoverySelectors() {
    List<DiscoverySelector> selectors = new ArrayList<>();

    checkStrings(discovery.getSelectedFiles())
        .ifPresent(names -> names.forEach(path -> selectors.add(selectFile(path))));

    checkStrings(discovery.getSelectedDirectories())
        .ifPresent(names -> names.forEach(path -> selectors.add(selectDirectory(path))));

    checkStrings(discovery.getSelectedPackages())
        .ifPresent(names -> names.forEach(name -> selectors.add(selectPackage(name))));

    checkStrings(discovery.getSelectedClasses())
        .ifPresent(names -> names.forEach(name -> selectors.add(selectClass(name))));

    checkStrings(discovery.getSelectedMethods())
        .ifPresent(names -> names.forEach(name -> selectors.add(selectMethod(name))));

    checkStrings(discovery.getSelectedClasspathResources())
        .ifPresent(names -> names.forEach(name -> selectors.add(selectClasspathResource(name))));

    checkAsPaths(discovery.getSelectedClasspathRoots())
        .ifPresent(paths -> selectors.addAll(selectClasspathRoots(paths)));

    checkStrings(discovery.getSelectedModules())
        .ifPresent(names -> selectors.addAll(selectModules(names)));

    return selectors;
  }

  Filter[] createFilters() {
    List<Filter> filters = new ArrayList<>();

    checkStrings(discovery.getFilterClassNamePatterns())
        .ifPresent(patterns -> filters.add(includeClassNamePatterns(toStringArray(patterns))));

    checkStrings(discovery.getFilterTags())
        .ifPresent(tags -> filters.add(includeTags(toList(tags))));

    return filters.toArray(new Filter[0]);
  }

  static Optional<Set<Path>> checkAsPaths(Set<String> strings) {
    if (strings == null || strings.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(strings.stream().map(Paths::get).collect(toSet()));
  }

  static <T extends Collection<String>> Optional<T> checkStrings(T strings) {
    if (strings == null || strings.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(strings);
  }

  static <T> List<T> toList(Collection<T> collection) {
    return new ArrayList<>(collection);
  }

  static String[] toStringArray(Collection<String> collection) {
    return collection.toArray(new String[0]);
  }
}
