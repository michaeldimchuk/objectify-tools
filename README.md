This repository is a collection of various tools used to reduce code duplication and templating done by developers.

# objectify-dao

This project contains pre-defined Objectify DAO definitions with the logic already implemented.
It can be used as a standalone artifact, or with the objectify-processor project, as described below.

The classes of note in this project are:

* LongIdDao / AsyncLongIdDao
* StringIdDao / AsyncStringIdDao
* LongIdWithParentDao / AsyncLongIdWithParentDao
* StringIdWithParentDao / AsyncStringIdWithParentDao

Other classes are meant only to provide background implementations and shouldn't be implemented directly.

Sample use case:

```
@Entity
public class Car {

  @Id
  private Long id;

  private String name;

  private String model;
}

public class AsyncCarDao implements AsyncLongIdDao<Car> {

  @Override
  public Class<Car> getType() {
    return Car.class;
  }
}

public class CarDao implements LongIdDao<Car> {

  private final AsyncLongIdDao<Car> asyncCarDao;

  CarDao(AsyncCarDao asyncCarDao) {
    this.asyncCarDao = asyncCarDao;
  }

  @Override
  public Class<Car> getType() {
    return Car.class;
  }

  @Override
  public AsyncLongIdDao<Car> async() {
    return asyncCarDao;
  }
}

```
# objectify-processor

An annotation processor used to automatically create Objectify DAO implementations based on the interfaces above.
The processor will pick up all classes annotated with @Entity and create all the necessary logic for them.

To use it, simply add the following dependencies to any project with @Entity annotated classes:

```
<dependency>
  <groupId>io.md.code</groupId>
  <artifactId>objectify-dao</artifactId>
</dependency>
<dependency>
  <groupId>io.md.code</groupId>
  <artifactId>objectify-processor</artifactId>
  <scope>provided</scope>
</dependency>
```

Note that this implementation is intended to be extremely type specific, so only entities who's parent types are
explicitly defined will be supported. While `Key<?>` would be a valid parent from the perspective of Objectify,
this implementation chooses not to support this. Only parents like `Key<Car>` or `Ref<Car>` will be supported.