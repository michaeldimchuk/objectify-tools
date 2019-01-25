package io.md.code.objectify.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SingleResult<T> implements Result<T> {

  Result<Key<T>> result;

  T value;

  @Override
  public T now() {
    result.now();
    return value;
  }
}
