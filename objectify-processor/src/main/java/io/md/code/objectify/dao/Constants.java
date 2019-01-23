package io.md.code.objectify.dao;

final class Constants {
  static final String ASYNC_ID_WITH_PARENT_DAO_TEMPLATE = "AsyncIdWithParentDaoTemplate.vm";
  static final String ASYNC_ID_DAO_TEMPLATE = "AsyncIdDaoTemplate.vm";
  static final String ID_WITH_PARENT_DAO_TEMPLATE = "IdWithParentDaoTemplate.vm";
  static final String ID_DAO_TEMPLATE = "IdDaoTemplate.vm";

  static final String ASYNC_STRING_ID_WITH_PARENT_DAO = AsyncStringIdWithParentDao.class.getSimpleName();
  static final String ASYNC_LONG_ID_WITH_PARENT_DAO = AsyncLongIdWithParentDao.class.getSimpleName();
  static final String ASYNC_STRING_ID_DAO = AsyncStringIdDao.class.getSimpleName();
  static final String ASYNC_LONG_ID_DAO = AsyncLongIdDao.class.getSimpleName();
  static final String STRING_ID_WITH_PARENT_DAO = StringIdWithParentDao.class.getSimpleName();
  static final String LONG_ID_WITH_PARENT_DAO = LongIdWithParentDao.class.getSimpleName();
  static final String STRING_ID_DAO = StringIdDao.class.getSimpleName();
  static final String LONG_ID_DAO = LongIdDao.class.getSimpleName();

  static final String DAO_PACKAGE_NAME = TypedDao.class.getPackage().getName();

  static final String PACKAGE = "PACKAGE";
  static final String PROCESSOR = "PROCESSOR";
  static final String PARENT_DIFFERENT_PACKAGE = "PARENT_DIFFERENT_PACKAGE";
  static final String PARENT_PACKAGE = "PARENT_PACKAGE";
  static final String PARENT_NAME = "PARENT_NAME";
  static final String DAO_DIFFERENT_PACKAGE = "DAO_DIFFERENT_PACKAGE";
  static final String DAO_PACKAGE = "DAO_PACKAGE";
  static final String DAO_NAME = "DAO_NAME";
  static final String ASYNC_DAO_NAME = "ASYNC_DAO_NAME";
  static final String ENTITY_NAME = "ENTITY_NAME";
  static final String ASYNC_DAO_FIELD = "ASYNC_DAO_FIELD";
}