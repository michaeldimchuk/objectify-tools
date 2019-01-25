package io.md.code.objectify.dao;

enum DaoConfiguration {
  LONG_ID(
      AsyncLongIdDao.class.getSimpleName(),
      LongIdDao.class.getSimpleName(),
      Constants.ASYNC_ID_DAO_TEMPLATE,
      Constants.ID_DAO_TEMPLATE,
      false,
      false
  ),
  STRING_ID(
      AsyncStringIdDao.class.getSimpleName(),
      StringIdDao.class.getSimpleName(),
      Constants.ASYNC_ID_DAO_TEMPLATE,
      Constants.ID_DAO_TEMPLATE,
      true,
      false
  ),
  LONG_ID_WITH_PARENT(
      AsyncLongIdWithParentDao.class.getSimpleName(),
      LongIdWithParentDao.class.getSimpleName(),
      Constants.ASYNC_ID_WITH_PARENT_DAO_TEMPLATE,
      Constants.ID_WITH_PARENT_DAO_TEMPLATE,
      false,
      true
  ),
  STRING_ID_WITH_PARENT(
      AsyncStringIdWithParentDao.class.getSimpleName(),
      StringIdWithParentDao.class.getSimpleName(),
      Constants.ASYNC_ID_WITH_PARENT_DAO_TEMPLATE,
      Constants.ID_WITH_PARENT_DAO_TEMPLATE,
      true,
      true
  );

  private final String asyncDao;

  private final String syncDao;

  private final String asyncTemplate;

  private final String syncTemplate;

  private final boolean hasStringId;

  private final boolean hasParent;

  DaoConfiguration(String asyncDao,
                   String syncDao,
                   String asyncTemplate,
                   String syncTemplate,
                   boolean hasStringId,
                   boolean hasParent) {
    this.asyncDao = asyncDao;
    this.syncDao = syncDao;
    this.asyncTemplate = asyncTemplate;
    this.syncTemplate = syncTemplate;
    this.hasStringId = hasStringId;
    this.hasParent = hasParent;
  }

  String getAsyncDao() {
    return asyncDao;
  }

  String getSyncDao() {
    return syncDao;
  }

  String getAsyncTemplate() {
    return asyncTemplate;
  }

  String getSyncTemplate() {
    return syncTemplate;
  }

  static DaoConfiguration of(boolean hasStringId, boolean hasParent) {
    for (DaoConfiguration daoName : values()) {
      if (daoName.hasStringId == hasStringId && daoName.hasParent == hasParent) {
        return daoName;
      }
    }
    throw new ProcessingException("This should never happen, no matching Dao configuration found");
  }
}
