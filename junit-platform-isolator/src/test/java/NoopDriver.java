import de.sormuras.junit.platform.isolator.Driver;

class NoopDriver implements Driver {
  @Override
  public void debug(String format, Object... args) {}

  @Override
  public void info(String format, Object... args) {}

  @Override
  public void warn(String format, Object... args) {}

  @Override
  public void error(String format, Object... args) {}
}
