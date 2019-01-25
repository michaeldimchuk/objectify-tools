package io.md.code.objectify.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.ImmutableList;
import com.googlecode.objectify.Key;
import io.md.code.test.Testable;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Test;

public class LongEntityDaoTest extends Testable {

  private static final int ENTITY_COUNT = 3;

  private static final Long[] ID_ARRAY = new Long[] { 1L, 2L, 3L };

  private static final long[] PRIMITIVE_ID_ARRAY = new long[] { 1, 2, 3 };

  private static final List<Long> ID_LIST = ImmutableList.copyOf(ID_ARRAY);

  private static final long ID = 42;

  @Inject
  private LongEntityDao longEntityDao;

  @After
  public void after() {
    deleteAll(LongEntity.class);
  }

  @Test
  public void asyncTest() {
    AsyncLongIdDao<LongEntity> asyncLongEntityDao = longEntityDao.async();
    assertThat(asyncLongEntityDao).isInstanceOf(AsyncLongEntityDao.class);
  }

  @Test
  public void load_SingleIdTest() {
    LongEntity entity = longEntityDao.load(ID);
    assertThat(entity).isNull();

    LongEntity newEntity = randomEntity(ID);
    entity = longEntityDao.load(ID);
    assertThat(entity).isEqualTo(newEntity);
  }

  @Test
  public void load_IdsAsArrayTest() {
    List<LongEntity> entities = longEntityDao.load(ID_ARRAY);
    assertThat(entities).isEmpty();

    List<LongEntity> newEntities = randomEntities();
    entities = longEntityDao.load(ID_ARRAY);
    assertThat(entities).containsExactlyInAnyOrderElementsOf(newEntities);
  }

  @Test
  public void load_IdsAsListTest() {
    List<LongEntity> entities = longEntityDao.load(ID_LIST);
    assertThat(entities).isEmpty();

    List<LongEntity> newEntities = randomEntities();
    entities = longEntityDao.load(ID_LIST);
    assertThat(entities).containsExactlyInAnyOrderElementsOf(newEntities);
  }

  @Test
  public void delete_SingleIdTest() {
    LongEntity newEntity = randomEntity(ID);
    assertThat(longEntityDao.load(ID)).isEqualTo(newEntity);

    longEntityDao.delete(ID);
    assertThat(longEntityDao.load(ID)).isNull();
  }

  @Test
  public void delete_IdsAsArrayTest() {
    List<LongEntity> newEntities = randomEntities();
    assertThat(longEntityDao.load(ID_ARRAY)).containsExactlyInAnyOrderElementsOf(newEntities);

    longEntityDao.delete(PRIMITIVE_ID_ARRAY);
    assertThat(longEntityDao.load(ID_ARRAY)).isEmpty();
  }

  @Test
  public void delete_IdsAsListTest() {
    List<LongEntity> newEntities = randomEntities();
    assertThat(longEntityDao.load(ID_LIST)).containsExactlyInAnyOrderElementsOf(newEntities);

    longEntityDao.delete(ID_LIST);
    assertThat(longEntityDao.load(ID_LIST)).isEmpty();
  }

  @Test
  public void save_SingleEntityTest() {
    LongEntity entity = load(LongEntity.class, ID);
    assertThat(entity).isNull();

    entity = makeRandomEntity(ID);

    LongEntity newEntity = longEntityDao.save(entity);
    assertThat(entity).isEqualTo(newEntity);
    assertThat(entity).isEqualTo(load(LongEntity.class, ID));
  }

  @Test
  public void save_EntityArrayTest() {
    assertThat(load(LongEntity.class, ID_ARRAY)).isEmpty();

    List<LongEntity> entities = longEntityDao.save(
        makeRandomEntity(1),
        makeRandomEntity(2),
        makeRandomEntity(3)
    );
    assertThat(entities).containsExactlyInAnyOrderElementsOf(load(LongEntity.class, ID_ARRAY));
  }

  @Test
  public void save_EntityListTest() {
    assertThat(load(LongEntity.class, ID_ARRAY)).isEmpty();

    List<LongEntity> entities = longEntityDao.save(
        Lists.asList(
            makeRandomEntity(1),
            makeRandomEntity(2),
            makeRandomEntity(3)
        )
    );
    assertThat(entities).containsExactlyInAnyOrderElementsOf(load(LongEntity.class, ID_ARRAY));
  }

  @Test
  public void deleteAllTest() {
    assertThat(load(LongEntity.class, ID_ARRAY)).isEmpty();

    List<LongEntity> entities = randomEntities();
    assertThat(entities).containsExactlyInAnyOrderElementsOf(load(LongEntity.class));

    longEntityDao.deleteAll();
    assertThat(load(LongEntity.class)).isEmpty();
  }

  @Test
  public void getTypeTest() {
    assertThat(longEntityDao.getType()).isEqualTo(LongEntity.class);
  }

  @Test
  public void iteratorTest() {
    assertThat(longEntityDao.iterator().hasNext()).isFalse();

    List<LongEntity> entities = randomEntities();

    Iterator<LongEntity> iterator = longEntityDao.iterator();
    int count = 0;
    while (iterator.hasNext()) {
      assertThat(entities).contains(iterator.next());
      count++;
    }
    assertThat(entities.size()).isEqualTo(count);
  }

  @Test
  public void listTest() {
    assertThat(longEntityDao.list()).isEmpty();

    List<LongEntity> entities = randomEntities();
    assertThat(entities).containsExactlyElementsOf(longEntityDao.list());
  }

  @Test
  public void keysTest() {
    assertThat(longEntityDao.keys()).isEmpty();

    List<Key<LongEntity>> entities = randomEntities().stream()
        .map(Key::create)
        .collect(Collectors.toList());
    assertThat(entities).containsExactlyInAnyOrderElementsOf(longEntityDao.keys());
  }

  /**
   * Verifies that by default the same instance is returned, as is the standard
   * implementation in Objectify.
   */
  @Test
  public void ofyTest() {
    assertThat(longEntityDao.ofy()).isEqualTo(ofy());
  }

  private LongEntity randomEntity(long id) {
    return save(makeRandomEntity(id));
  }

  private LongEntity makeRandomEntity(long id) {
    LongEntity entity = new LongEntity();
    entity.setId(id);
    entity.setValue(RandomStringUtils.random(10));
    return entity;
  }

  private List<LongEntity> randomEntities() {
    List<LongEntity> entities = new ArrayList<>(ENTITY_COUNT);
    for (int x = 1; x < ENTITY_COUNT + 1; x++) {
      entities.add(randomEntity(x));
    }
    return save(entities);
  }
}
