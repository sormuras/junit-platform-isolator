package de.sormuras.junit.platform.isolator;

import static java.util.Collections.addAll;

import java.util.ArrayList;
import java.util.List;

class TargetModuleCollector extends AbstractModuleInfoTestConsumer {

  private final List<String> targets;

  TargetModuleCollector(Driver driver) {
    super(driver);
    this.targets = new ArrayList<>();
  }

  @Override
  void accept(String option, String sourceName, String packageName, String[] targetNames) {
    addAll(targets, targetNames);
  }

  public List<String> getTargets() {
    return targets;
  }
}
