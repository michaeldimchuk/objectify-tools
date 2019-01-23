package io.md.code.objectify.dao;

public interface StringIdDao<T> extends SyncStringIdDao<T> {

  AsyncStringIdDao<T> async();
}
