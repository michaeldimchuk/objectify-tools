package io.md.code.objectify.dao;

import java.util.List;

public interface SyncTypedDao<T> extends TypedDao<T> {

  default T save(T entity) {
    ofy().save().entity(entity).now();
    return entity;
  }

  default List<T> save(T... entities) {
    ofy().save().entities(entities).now();
    return Lists.asList(entities);
  }

  default List<T> save(List<T> entities) {
    ofy().save().entities(entities).now();
    return entities;
  }

  default void deleteAll() {
    ofy().delete().keys(keys()).now();
  }
}
