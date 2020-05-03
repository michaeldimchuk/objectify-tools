package io.md.code.objectify.dao;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Cache
@Entity
@NoArgsConstructor
@FieldDefaults(makeFinal = false)
public class StringWithParentEntity {

  @Parent
  private Ref<StringEntity> parent;

  @Id
  private String id;

  private String value;
}
