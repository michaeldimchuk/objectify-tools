package io.md.code.objectify.dao;

import java.util.Map;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

import com.google.common.collect.ImmutableMap;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;

class TypeCache {

  Map<Class<?>, TypeElement> typeElements;

  Map<Class<?>, TypeMirror> typeMirrors;

  TypeCache(Elements elements) {
    typeElements = getKnownTypeElements(elements);
    typeMirrors = getKnownTypeMirrors(elements);
  }

  TypeMirror getTypeMirror(Class<?> type) {
    return typeMirrors.get(type);
  }

  TypeElement getTypeElement(Class<?> type) {
    return typeElements.get(type);
  }

  private Map<Class<?>, TypeMirror> getKnownTypeMirrors(Elements elements) {
    ImmutableMap.Builder<Class<?>, TypeMirror> knownTypes = ImmutableMap.builder();
    addType(knownTypes, String.class, elements);
    addType(knownTypes, Long.class, elements);
    addType(knownTypes, Ref.class, elements);
    addType(knownTypes, Key.class, elements);
    addType(knownTypes, com.google.appengine.api.datastore.Key.class, elements);
    return knownTypes.build();
  }

  private Map<Class<?>, TypeElement> getKnownTypeElements(Elements elements) {
    return ImmutableMap.of(
        Ref.class, elements.getTypeElement(Ref.class.getCanonicalName()),
        Key.class, elements.getTypeElement(Key.class.getCanonicalName())
    );
  }

  private void addType(ImmutableMap.Builder<Class<?>, TypeMirror> knownTypes, Class<?> type, Elements elements) {
    knownTypes.put(type, elements.getTypeElement(type.getCanonicalName()).asType());
  }
}
