package io.md.objectify.dao;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Cache
@Entity
@NoArgsConstructor
public class StringWithParentEntity {

  @Parent
  private Ref<StringEntity> parent;

  @Id
  private String id;

  private String value;
}
