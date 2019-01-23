package io.md.code.objectify.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SingleResult<T> implements Result<T> {

  private final Result<Key<T>> result;

  private final T value;

  @Override
  public T now() {
    result.now();
    return value;
  }
}
