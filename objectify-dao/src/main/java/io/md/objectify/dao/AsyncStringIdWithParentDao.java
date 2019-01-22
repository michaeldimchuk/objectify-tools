package io.md.objectify.dao;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.DeleteIds;
import com.googlecode.objectify.cmd.LoadIds;
import com.googlecode.objectify.cmd.QueryKeys;

public interface AsyncStringIdWithParentDao<P, T> extends AsyncStringIdDao<T> {

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

  default List<T> list(P parent) {
    return loadQuery().ancestor(parent).list();
  }

  default QueryResultIterator<T> iterator(P parent) {
    return loadQuery().ancestor(parent).iterator();
  }

  default QueryKeys<T> keys(P parent) {
    return loadQuery().ancestor(parent).keys();
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

  default LoadIds<T> loadQuery(P parent) {
    return loadQuery().parent(parent);
  }

  default DeleteIds deleteQuery(P parent) {
    return deleteQuery().parent(parent);
  }
}
