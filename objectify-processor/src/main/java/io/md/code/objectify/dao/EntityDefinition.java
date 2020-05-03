package io.md.code.objectify.dao;

import java.util.Map;

import javax.lang.model.type.TypeMirror;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(makeFinal = false)
class EntityDefinition {

  private static final String ASYNC_DAO_FIELD = "async%sDao";

  private String packageName;

  private String simpleName;

  private Boolean stringId;

  private boolean hasParent;

  private TypeMirror parentType;

  boolean isStringId() {
    return stringId;
  }

  boolean hasParent() {
    return hasParent;
  }

  boolean hasId() {
    return stringId != null;
  }

  void setParentType(TypeMirror parentType) {
    this.parentType = parentType;
    hasParent = true;
  }

  Map<String, Object> toContext() {
    return ImmutableMap.of(
        Constants.PACKAGE, packageName,
        Constants.ENTITY_NAME, simpleName,
        Constants.ASYNC_DAO_FIELD, String.format(ASYNC_DAO_FIELD, simpleName),
        Constants.DAO_DIFFERENT_PACKAGE, !Constants.DAO_PACKAGE_NAME.equals(packageName)
    );
  }
}
