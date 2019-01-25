package io.md.code.objectify.dao;

import java.util.List;

public interface SyncLongIdDao<T> extends SyncTypedDao<T> {

  default T load(long id) {
    return loadQuery().id(id).now();
  }

  default List<T> load(Long... ids) {
    return Lists.asList(loadQuery().ids(ids).values());
  }

  default List<T> load(Iterable<Long> ids) {
    return Lists.asList(loadQuery().ids(ids).values());
  }

  default void delete(long id) {
    deleteQuery().id(id).now();
  }

  /**
   * Deletes all entities of type {@link T} with the provided ids.
   * Should not directly pass the ids into Objectify as it has a bug
   * and cannot delete by a primitive array of ids.
   *
   * @param ids the keys of the entities to delete
   */
  default void delete(long... ids) {
    delete(Lists.asList(ids));
  }

  default void delete(Iterable<Long> ids) {
    deleteQuery().ids(ids).now();
  }
}
