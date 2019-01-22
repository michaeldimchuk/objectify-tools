package io.md.objectify.dao;

import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Result;

public interface AsyncStringIdWithParentDao<P, T> extends AsyncStringIdDao<T>, ParentDao<P, T> {

  default Result<T> load(P parent, String id) {
    return loadQuery(parent).id(id);
  }

  default Result<List<T>> load(P parent, String... ids) {
    Map<String, T> result = loadQuery(parent).ids(ids);
    return new ListByIdResult<>(result);
  }

  default Result<List<T>> load(P parent, Iterable<String> ids) {
    Map<String, T> result = loadQuery(parent).ids(ids);
    return new ListByIdResult<>(result);
  }

  default Result<Void> delete(P parent) {
    return ofy().delete().keys(keys(parent));
  }

  default Result<Void> delete(P parent, String id) {
    return deleteQuery(parent).id(id);
  }

  default Result<Void> delete(P parent, String... ids) {
    return deleteQuery(parent).ids(ids);
  }

  default Result<Void> delete(P parent, Iterable<String> ids) {
    return deleteQuery(parent).ids(ids);
  }
}
