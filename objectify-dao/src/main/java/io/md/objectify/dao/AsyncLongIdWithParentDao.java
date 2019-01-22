package io.md.objectify.dao;

import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Result;

public interface AsyncLongIdWithParentDao<P, T> extends AsyncLongIdDao<T>, ParentDao<P, T> {

  default Result<T> load(P parent, long id) {
    return loadQuery(parent).id(id);
  }

  default Result<List<T>> load(P parent, Long... ids) {
    Map<Long, T> result = loadQuery(parent).ids(ids);
    return new ListByIdResult<>(result);
  }

  default Result<List<T>> load(P parent, Iterable<Long> ids) {
    Map<Long, T> result = loadQuery(parent).ids(ids);
    return new ListByIdResult<>(result);
  }

  default Result<Void> delete(P parent) {
    return ofy().delete().keys(keys(parent));
  }

  default Result<Void> delete(P parent, long id) {
    return deleteQuery(parent).id(id);
  }

  default Result<Void> delete(P parent, long... ids) {
    return deleteQuery(parent).ids(ids);
  }

  default Result<Void> delete(P parent, Iterable<Long> ids) {
    return deleteQuery(parent).ids(ids);
  }
}
