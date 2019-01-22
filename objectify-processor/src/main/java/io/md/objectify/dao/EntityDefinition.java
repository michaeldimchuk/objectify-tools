package io.md.objectify.dao;

import javax.lang.model.type.TypeMirror;

class EntityDefinition {

  private String packageName;

  private Boolean stringId;

  private boolean hasParent;

  private TypeMirror parentType;

  String getPackageName() {
    return packageName;
  }

  void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  boolean isStringId() {
    return stringId;
  }

  void setStringId(boolean stringId) {
    this.stringId = stringId;
  }

  boolean hasParent() {
    return hasParent;
  }

  boolean hasId() {
    return stringId != null;
  }

  TypeMirror getParentType() {
    return parentType;
  }

  void setParentType(TypeMirror parentType) {
    this.parentType = parentType;
    hasParent = true;
  }
}
