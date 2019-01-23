package io.md.code.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;

public abstract class Testable {

  private static final Injector injector = Guice.createInjector();

  @Before
  public void initialize() {
    injector.injectMembers(this);
  }
}
