package io.md.code.objectify.dao;

import java.util.List;

import com.google.common.collect.Lists;

public interface SyncLongIdDao<T> extends SyncTypedDao<T> {

  default T load(long id) {
    return loadQuery().id(id).now();
  }

  default List<T> load(Long... ids) {
    return Lists.newArrayList(loadQuery().ids(ids).values());
  }

  default List<T> load(Iterable<Long> ids) {
    return Lists.newArrayList(loadQuery().ids(ids).values());
  }

  default void delete(long id) {
    deleteQuery().id(id).now();
  }

  default void delete(long... ids) {
    deleteQuery().ids(ids).now();
  }

  default void delete(Iterable<Long> ids) {
    deleteQuery().ids(ids).now();
  }
}
