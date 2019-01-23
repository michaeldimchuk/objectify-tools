package io.md.code.objectify.dao;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;

import io.md.code.test.Testable;
import org.junit.Test;

public class LongEntityDaoTest extends Testable {

  @Inject
  private LongEntityDao longEntityDao;

  @Test
  public void getTypeTest() {
    assertThat(longEntityDao.getType()).isEqualTo(LongEntity.class);
  }

  @Test
  public void asyncTest() {
    AsyncLongIdDao<LongEntity> asyncLongEntityDao = longEntityDao.async();
    assertThat(asyncLongEntityDao).isInstanceOf(AsyncLongEntityDao.class);
  }
}
