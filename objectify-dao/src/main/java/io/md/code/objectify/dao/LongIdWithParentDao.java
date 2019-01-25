package io.md.code.objectify.dao;

import java.util.List;

public interface LongIdWithParentDao<P, T> extends AsyncLongIdDao<T>, ParentDao<P, T> {

  AsyncLongIdWithParentDao<P, T> async();

  default T load(P parent, long id) {
    return loadQuery(parent).id(id).now();
  }

  default List<T> load(P parent, Long... ids) {
    return Lists.asList(loadQuery(parent).ids(ids).values());
  }

  default List<T> load(P parent, Iterable<Long> ids) {
    return Lists.asList(loadQuery(parent).ids(ids).values());
  }

  default void delete(P parent) {
    ofy().delete().keys(keys(parent)).now();
  }

  default void delete(P parent, long id) {
    deleteQuery(parent).id(id).now();
  }

  default void delete(P parent, long... ids) {
    delete(parent, Lists.asList(ids));
  }

  default void delete(P parent, Iterable<Long> ids) {
    deleteQuery(parent).ids(ids).now();
  }
}
