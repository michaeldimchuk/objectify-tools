package io.md.objectify.dao;

import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Result;

public interface AsyncLongIdDao<T> extends AsyncTypedDao<T> {

  default Result<T> load(long id) {
    return loadQuery().id(id);
  }

  default Result<List<T>> load(Long... ids) {
    Map<Long, T> result = loadQuery().ids(ids);
    return new ListByIdResult<>(result);
  }

  default Result<List<T>> load(Iterable<Long> ids) {
    Map<Long, T> result = loadQuery().ids(ids);
    return new ListByIdResult<>(result);
  }

  default Result<Void> delete(long id) {
    return deleteQuery().id(id);
  }

  default Result<Void> delete(long... ids) {
    return deleteQuery().ids(ids);
  }

  default Result<Void> delete(Iterable<Long> ids) {
    return deleteQuery().ids(ids);
  }
}
