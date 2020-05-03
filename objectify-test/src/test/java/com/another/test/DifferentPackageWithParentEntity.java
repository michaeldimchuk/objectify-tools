package com.another.test;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import io.md.code.objectify.dao.LongEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Cache
@Entity
@NoArgsConstructor
@FieldDefaults(makeFinal = false)
public class DifferentPackageWithParentEntity {

  @Parent
  private Ref<LongEntity> parent;

  @Id
  private Long id;

  private String value;
}
