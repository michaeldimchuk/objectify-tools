package io.md.objectify.dao;

import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.common.collect.Lists;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.DeleteType;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.QueryKeys;

public interface TypedDao<T> {

  Class<T> getType();

  default T save(T entity) {
    ofy().save().entity(entity).now();
    return entity;
  }

  default List<T> save(T... entities) {
    ofy().save().entities(entities).now();
    return Lists.newArrayList(entities);
  }

  default List<T> save(List<T> entities) {
    ofy().save().entities(entities).now();
    return entities;
  }

  default QueryResultIterator<T> iterator() {
    return loadQuery().iterable().iterator();
  }

  default List<T> list() {
    return loadQuery().list();
  }

  default QueryKeys<T> keys() {
    return loadQuery().keys();
  }

  default void deleteAll() {
    ofy().delete().keys(keys()).now();
  }

  default LoadType<T> loadQuery() {
    return ofy().load().type(getType());
  }

  default DeleteType deleteQuery() {
    return ofy().delete().type(getType());
  }

  default Objectify ofy() {
    return ObjectifyService.ofy();
  }
}
