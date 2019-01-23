package io.md.code.objectify.dao;

import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Result;

public interface AsyncStringIdDao<T> extends AsyncTypedDao<T> {

  default Result<T> load(String id) {
    return loadQuery().id(id);
  }

  default Result<List<T>> load(String... ids) {
    Map<String, T> result = loadQuery().ids(ids);
    return new ListByIdResult<>(result);
  }

  default Result<List<T>> load(Iterable<String> ids) {
    Map<String, T> result = loadQuery().ids(ids);
    return new ListByIdResult<>(result);
  }

  default Result<Void> delete(String id) {
    return deleteQuery().id(id);
  }

  default Result<Void> delete(String... ids) {
    return deleteQuery().ids(ids);
  }

  default Result<Void> delete(Iterable<String> ids) {
    return deleteQuery().ids(ids);
  }
}
