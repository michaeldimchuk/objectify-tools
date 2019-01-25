package io.md.code.test;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.another.test.DifferentPackageEntity;
import com.another.test.DifferentPackageWithParentEntity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.QueryKeys;
import io.md.code.objectify.dao.LongEntity;
import io.md.code.objectify.dao.LongWithParentEntity;
import io.md.code.objectify.dao.StringEntity;
import io.md.code.objectify.dao.StringWithParentEntity;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class Testable extends Loggable {

  private static final LocalServiceTestHelper serviceHelper = new LocalServiceTestHelper(
      new LocalDatastoreServiceTestConfig()
  );

  private static final Injector injector = Guice.createInjector();

  private static final AtomicBoolean initialized = new AtomicBoolean();

  private static Closeable objectifyService;

  @BeforeClass
  public static void setUp() {
    if (!initialized.getAndSet(true)) {
      serviceHelper.setUp();
      registerEntities();
      objectifyService = ObjectifyService.begin();
    }
  }

  @AfterClass
  public static void tearDown() throws IOException {
    if (objectifyService != null) {
      objectifyService.close();
      serviceHelper.tearDown();
    }
  }

  @Before
  public void initialize() {
    injector.injectMembers(this);
  }

  protected <T> List<T> load(Class<T> type) {
    return ofy().load().type(type).list();
  }

  protected <T> T load(Class<T> type, long id) {
    return ofy().load().type(type).id(id).now();
  }

  protected <T> Collection<T> load(Class<T> type, Long... ids) {
    return ofy().load().type(type).ids(ids).values();
  }

  protected <T> T save(T entity) {
    ofy().save().entity(entity).now();
    return entity;
  }

  protected <T> List<T> save(List<T> entities) {
    return Lists.newArrayList(ofy().save().entities(entities).now().values());
  }

  protected <T> void deleteAll(Class<T> type) {
    QueryKeys<T> keys = ofy().load().type(type).keys();
    ofy().delete().keys(keys).now();
  }

  private static void registerEntities() {
    ObjectifyFactory factory = ObjectifyService.factory();
    factory.register(LongEntity.class);
    factory.register(LongWithParentEntity.class);
    factory.register(StringEntity.class);
    factory.register(StringWithParentEntity.class);
    factory.register(DifferentPackageEntity.class);
    factory.register(DifferentPackageWithParentEntity.class);
  }
}
