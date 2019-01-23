package io.md.code.objectify.dao;

import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.DeleteIds;
import com.googlecode.objectify.cmd.LoadIds;
import com.googlecode.objectify.cmd.QueryKeys;

public interface ParentDao<P, T> extends TypedDao<T> {

  default List<T> list(P parent) {
    return loadQuery().ancestor(parent).list();
  }

  default List<T> list(Key<P> parent) {
    return loadQuery().ancestor(parent).list();
  }

  default QueryResultIterator<T> iterator(P parent) {
    return loadQuery().ancestor(parent).iterator();
  }

  default QueryResultIterator<T> iterator(Key<P> parent) {
    return loadQuery().ancestor(parent).iterator();
  }

  default QueryKeys<T> keys(P parent) {
    return loadQuery().ancestor(parent).keys();
  }

  default QueryKeys<T> keys(Key<P> parent) {
    return loadQuery().ancestor(parent).keys();
  }

  default LoadIds<T> loadQuery(P parent) {
    return loadQuery().parent(parent);
  }

  default LoadIds<T> loadQuery(Key<P> parent) {
    return loadQuery().parent(parent);
  }

  default DeleteIds deleteQuery(P parent) {
    return deleteQuery().parent(parent);
  }

  default DeleteIds deleteQuery(Key<P> parent) {
    return deleteQuery().parent(parent);
  }
}
