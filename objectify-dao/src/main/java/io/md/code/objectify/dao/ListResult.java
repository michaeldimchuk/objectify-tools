package io.md.code.objectify.dao;

import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListResult<T> implements Result<List<T>> {

  Result<Map<Key<T>, T>> result;

  @Override
  public List<T> now() {
    return Lists.asList(result.now().values());
  }
}
