package io.md.objectify.dao;

import java.util.List;

import com.google.common.collect.Lists;

public interface StringIdDao<T> extends SyncTypedDao<T> {

  default T load(String id) {
    return loadQuery().id(id).now();
  }

  default List<T> load(String... ids) {
    return Lists.newArrayList(loadQuery().ids(ids).values());
  }

  default List<T> load(Iterable<String> ids) {
    return Lists.newArrayList(loadQuery().ids(ids).values());
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
