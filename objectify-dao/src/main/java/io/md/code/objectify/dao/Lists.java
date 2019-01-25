package io.md.code.objectify.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
class Lists {

  List<Long> asList(long... elements) {
    Long[] copy = new Long[elements.length];
    for (int index = 0; index < elements.length; index++) {
      copy[index] = elements[index];
    }
    return asList(copy);
  }

  @SafeVarargs
  <T> List<T> asList(T... elements) {
    return Arrays.asList(elements);
  }

  <T> List<T> asList(Collection<T> elements) {
    return new ArrayList<>(elements);
  }
}
