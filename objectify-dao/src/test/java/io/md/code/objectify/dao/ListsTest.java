package io.md.code.objectify.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ListsTest {

  @Test
  public void asList_PrimitiveArrayTest() {
    long[] elements = new long[] { 4, 9, 17, 54, 22, 3, 9 };
    List<Long> copiedElements = Lists.asList(elements);
    assertThat(copiedElements.size()).isEqualTo(elements.length);
    for (int index = 0; index < copiedElements.size(); index++) {
      assertThat(copiedElements.get(index)).isEqualTo(elements[index]);
    }
  }

  @Test
  public void asList_VarArgsTest() {
    Long[] elements = new Long[] { 4L, 9L, 17L, 54L, 22L, 3L, 9L };
    List<Long> copiedElements = Lists.asList(elements);
    assertThat(copiedElements).containsExactly(elements);
  }

  @Test
  public void asList_CollectionTest() {
    List<Long> elements = Arrays.asList(4L, 9L, 17L, 54L, 22L, 3L, 9L);
    List<Long> copiedElements = Lists.asList(elements);
    assertThat(copiedElements).containsExactlyElementsOf(elements);
  }
}
