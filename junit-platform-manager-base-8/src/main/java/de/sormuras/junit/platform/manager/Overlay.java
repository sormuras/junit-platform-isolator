package de.sormuras.junit.platform.manager;

import java.net.URL;
import java.net.URLClassLoader;

interface Overlay {

  /** Create new URLClassLoader instance. */
  URLClassLoader newClassLoader(String name, ClassLoader parent, URL... urls);
}
