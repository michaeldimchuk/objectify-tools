package io.md.code.objectify.dao;

import java.util.List;
import java.util.stream.Collectors;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

final class Classes {

  private static final String NOT_PACKAGE = "Couldn't get package, element %s is not a root type";

  static boolean samePackage(Element first, Element second) {
    return getPackage(first).equals(getPackage(second));
  }

  static <T extends Element> String getPackage(T element) {
    Element entityPackage = element.getEnclosingElement();
    if (entityPackage.getKind() != ElementKind.PACKAGE) {
      throw new ProcessingException(NOT_PACKAGE, element);
    }
    PackageElement packageElement = (PackageElement) entityPackage;
    return packageElement.getQualifiedName().toString();
  }

  static List<VariableElement> getFields(TypeElement element) {
    return element.getEnclosedElements().stream()
        .filter(Classes::isField)
        .filter(VariableElement.class::isInstance)
        .map(VariableElement.class::cast)
        .collect(Collectors.toList());
  }

  static boolean isParameterizedType(TypeMirror type) {
    return type.getKind() == TypeKind.DECLARED
        && type instanceof DeclaredType;
  }

  static <T extends Element> boolean isField(T element) {
    return element.getKind() == ElementKind.FIELD;
  }

  static <T extends Element> boolean isClass(T element) {
    return element.getKind() == ElementKind.CLASS;
  }
}
