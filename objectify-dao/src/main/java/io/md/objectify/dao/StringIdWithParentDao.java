package io.md.objectify.dao;

import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.collect.Lists;
import com.googlecode.objectify.cmd.DeleteIds;
import com.googlecode.objectify.cmd.LoadIds;
import com.googlecode.objectify.cmd.QueryKeys;

public interface StringIdWithParentDao<P, T> extends StringIdDao<T> {

  default T load(P parent, String id) {
    return loadQuery(parent).id(id).now();
  }

  default List<T> load(P parent, String... ids) {
    return Lists.newArrayList(loadQuery(parent).ids(ids).values());
  }

  default List<T> load(P parent, Iterable<String> ids) {
    return Lists.newArrayList(loadQuery(parent).ids(ids).values());
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

  default void delete(P parent) {
    ofy().delete().keys(keys(parent)).now();
  }

  default void delete(P parent, String id) {
    deleteQuery(parent).id(id).now();
  }

  default void delete(P parent, String... ids) {
    deleteQuery(parent).ids(ids).now();
  }

  default void delete(P parent, Iterable<String> ids) {
    deleteQuery(parent).ids(ids).now();
  }

  default LoadIds<T> loadQuery(P parent) {
    return loadQuery().parent(parent);
  }

  default DeleteIds deleteQuery(P parent) {
    return deleteQuery().parent(parent);
  }
}
