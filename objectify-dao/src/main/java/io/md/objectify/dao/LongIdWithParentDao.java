package io.md.objectify.dao;

import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.collect.Lists;
import com.googlecode.objectify.cmd.DeleteIds;
import com.googlecode.objectify.cmd.LoadIds;
import com.googlecode.objectify.cmd.QueryKeys;

public interface LongIdWithParentDao<P, T> extends LongIdDao<T> {

  default T load(P parent, long id) {
    return loadQuery(parent).id(id).now();
  }

  default List<T> load(P parent, Long... ids) {
    return Lists.newArrayList(loadQuery(parent).ids(ids).values());
  }

  default List<T> load(P parent, Iterable<Long> ids) {
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

  default void delete(P parent, long id) {
    deleteQuery(parent).id(id).now();
  }

  default void delete(P parent, long... ids) {
    deleteQuery(parent).ids(ids).now();
  }

  default void delete(P parent, Iterable<Long> ids) {
    deleteQuery(parent).ids(ids).now();
  }

  default LoadIds<T> loadQuery(P parent) {
    return loadQuery().parent(parent);
  }

  default DeleteIds deleteQuery(P parent) {
    return deleteQuery().parent(parent);
  }
}
