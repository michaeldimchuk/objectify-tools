package io.md.code.objectify.dao;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Cache
@Entity
@NoArgsConstructor
@FieldDefaults(makeFinal = false)
public class StringEntity {

  @Id
  private Long id;

  private String value;
}
