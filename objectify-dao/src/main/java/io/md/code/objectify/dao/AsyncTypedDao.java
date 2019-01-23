package io.md.code.objectify.dao;

import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Result;

public interface AsyncTypedDao<T> extends TypedDao<T> {

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

  default Result<Void> deleteAll() {
    return ofy().delete().keys(keys());
  }
}
