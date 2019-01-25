package io.md.code.objectify.dao;

import java.util.List;

public interface SyncStringIdDao<T> extends SyncTypedDao<T> {

  default T load(String id) {
    return loadQuery().id(id).now();
  }

  default List<T> load(String... ids) {
    return Lists.asList(loadQuery().ids(ids).values());
  }

  default List<T> load(Iterable<String> ids) {
    return Lists.asList(loadQuery().ids(ids).values());
  }

  default void delete(String id) {
    deleteQuery().id(id).now();
  }

  default void delete(String... ids) {
    deleteQuery().ids(ids).now();
  }

  default void delete(Iterable<String> ids) {
    deleteQuery().ids(ids).now();
  }
}
