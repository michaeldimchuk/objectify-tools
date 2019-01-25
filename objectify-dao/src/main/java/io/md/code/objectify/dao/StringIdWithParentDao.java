package io.md.code.objectify.dao;

import java.util.List;

public interface StringIdWithParentDao<P, T> extends SyncStringIdDao<T>, ParentDao<P, T> {

  AsyncStringIdWithParentDao<P, T> async();

  default T load(P parent, String id) {
    return loadQuery(parent).id(id).now();
  }

  default List<T> load(P parent, String... ids) {
    return Lists.asList(loadQuery(parent).ids(ids).values());
  }

  default List<T> load(P parent, Iterable<String> ids) {
    return Lists.asList(loadQuery(parent).ids(ids).values());
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
}
