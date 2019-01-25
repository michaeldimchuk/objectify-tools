package io.md.code.test;

import java.net.URL;

abstract class Loggable {
  static {
    URL propertyUrl = Loggable.class.getClassLoader().getResource("logging.properties");
    if (propertyUrl != null) {
      System.setProperty("java.util.logging.config.file", propertyUrl.getPath());
    }
  }
}
