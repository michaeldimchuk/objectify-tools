package io.md.code.objectify.dao;

import java.util.List;

import com.google.common.collect.Lists;

public interface LongIdWithParentDao<P, T> extends AsyncLongIdDao<T>, ParentDao<P, T> {

  AsyncLongIdWithParentDao<P, T> async();

  default T load(P parent, long id) {
    return loadQuery(parent).id(id).now();
  }

  default List<T> load(P parent, Long... ids) {
    return Lists.newArrayList(loadQuery(parent).ids(ids).values());
  }

  default List<T> load(P parent, Iterable<Long> ids) {
    return Lists.newArrayList(loadQuery(parent).ids(ids).values());
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
}
