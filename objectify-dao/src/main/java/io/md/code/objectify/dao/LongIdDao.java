package io.md.code.objectify.dao;

public interface LongIdDao<T> extends SyncLongIdDao<T> {

  AsyncLongIdDao<T> async();
}
