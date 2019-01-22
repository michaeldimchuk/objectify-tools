package io.md.objectify.dao;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Result;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListByIdResult<K, T> implements Result<List<T>> {

  private final Map<K, T> result;

  @Override
  public List<T> now() {
    return Lists.newArrayList(result.values());
  }
}
