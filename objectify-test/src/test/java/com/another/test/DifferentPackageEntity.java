package com.another.test;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Cache
@Entity
@NoArgsConstructor
public class DifferentPackageEntity {

  @Id
  private Long id;

  private String value;
}
