package io.md.code.objectify.dao;

import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.DeleteType;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.QueryKeys;

public interface TypedDao<T> {

  Class<T> getType();

  default QueryResultIterator<T> iterator() {
    return loadQuery().iterable().iterator();
  }

  default List<T> list() {
    return loadQuery().list();
  }

  default QueryKeys<T> keys() {
    return loadQuery().keys();
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
