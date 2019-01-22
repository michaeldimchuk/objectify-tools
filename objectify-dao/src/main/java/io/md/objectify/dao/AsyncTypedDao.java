package io.md.objectify.dao;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Result;
import com.googlecode.objectify.cmd.DeleteType;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.QueryKeys;

public interface AsyncTypedDao<T> {

  Class<T> getType();

  default Result<T> save(T entity) {
    Result<Key<T>> result = ofy().save().entity(entity);
    return new SingleResult<>(result, entity);
  }

  default Result<List<T>> save(T... entities) {
    Result<Map<Key<T>, T>> result = ofy().save().entities(entities);
    return new ListResult<>(result);
  }

  default Result<List<T>> save(List<T> entities) {
    Result<Map<Key<T>, T>> result = ofy().save().entities(entities);
    return new ListResult<>(result);
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
    ofy().delete().keys(keys());
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
