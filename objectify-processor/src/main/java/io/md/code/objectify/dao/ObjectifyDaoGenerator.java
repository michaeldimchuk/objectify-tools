package io.md.code.objectify.dao;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ObjectifyDaoGenerator {

  private static final String UNSUPPORTED_PARENT_TYPE = "Google's native key type as a parent is not supported, entity was %s";

  private static final String NOT_PARAMETERIZED_PARENT_TYPE = "Only parameterized types can be parents, found %s in entity %s";

  private static final String INVALID_ARGUMENT_COUNT = "Must have only 1 type argument, found %s on entity %";

  private static final String BAD_PARENT_TYPE = "Objectify only supports Key or Ref type parent fields, but found %s on entity %s";

  private static final String ASYNC_DAO_NAME_TEMPLATE = "%s.Async%sDao";

  private static final String ASYNC_DAO_FIELD_TEMPLATE = "async%sDao";

  private static final String DAO_NAME_TEMPLATE = "%s.%sDao";

  private static final Logger log = LoggerFactory.getLogger(ObjectifyProcessor.class);

  private final TypeCache typeCache;

  private final Types types;

  private final Filer filer;

  ObjectifyDaoGenerator(Elements elements, Types types, Filer filer) {
    typeCache = new TypeCache(elements);
    this.types = types;
    this.filer = filer;
  }

  void generate(TypeElement entity) {
    EntityDefinition definition = getEntityDefinition(entity);
    String fieldName = String.format(ASYNC_DAO_FIELD_TEMPLATE, entity.getSimpleName());
    SourceFileBuilder builder = getSourceFileBuilder(entity, definition)
        .put(Constants.PACKAGE, definition.getPackageName())
        .put(Constants.PROCESSOR, ObjectifyProcessor.class.getCanonicalName())
        .put(Constants.ENTITY_NAME, entity.getSimpleName())
        .put(Constants.DAO_PACKAGE, Constants.DAO_PACKAGE_NAME)
        .put(Constants.ASYNC_DAO_FIELD, fieldName)
        .put(Constants.DAO_DIFFERENT_PACKAGE, !Classes.getPackage(entity).equals(Constants.DAO_PACKAGE_NAME));
    addDaoName(builder, definition);
    addParentPackage(entity, builder, definition);
    writeObjectifyDaos(entity, definition, builder);
  }

  private void writeObjectifyDaos(TypeElement entity, EntityDefinition definition, SourceFileBuilder builder) {
    String asyncSourceCode = builder.build(getAsyncTemplate(definition));
    String syncSourceCode = builder.build(getTemplate(definition));
    String asyncDaoName = getObjectifyDaoName(ASYNC_DAO_NAME_TEMPLATE, entity);
    String syncDaoName = getObjectifyDaoName(DAO_NAME_TEMPLATE, entity);
    writeSourceFile(asyncDaoName, asyncSourceCode);
    writeSourceFile(syncDaoName, syncSourceCode);
  }

  private String getAsyncTemplate(EntityDefinition definition) {
    if (definition.hasParent()) {
      return Constants.ASYNC_ID_WITH_PARENT_DAO_TEMPLATE;
    }
    return Constants.ASYNC_ID_DAO_TEMPLATE;
  }

  private String getTemplate(EntityDefinition definition) {
    if (definition.hasParent()) {
      return Constants.ID_WITH_PARENT_DAO_TEMPLATE;
    }
    return Constants.ID_DAO_TEMPLATE;
  }

  private void writeSourceFile(String className, String sourceCode) {
    try {
      JavaFileObject daoFile = filer.createSourceFile(className);
      try (Writer writer = daoFile.openWriter()) {
        writer.write(sourceCode);
      }
      log.info("Generated {}", className);
    } catch (IOException e) {
      throw new ProcessingException("Unable to create a source file", e);
    }
  }

  private String getObjectifyDaoName(String template, TypeElement entity) {
    return String.format(
        template,
        Classes.getPackage(entity),
        entity.getSimpleName()
    );
  }

  private void addParentPackage(TypeElement entity, SourceFileBuilder builder, EntityDefinition definition) {
    if (!definition.hasParent()) {
      return;
    }
    Element parent = types.asElement(definition.getParentType());
    builder.put(Constants.PARENT_DIFFERENT_PACKAGE, !Classes.samePackage(entity, parent))
        .put(Constants.PARENT_PACKAGE, Classes.getPackage(parent))
        .put(Constants.PARENT_NAME, parent.getSimpleName());
  }

  private void addDaoName(SourceFileBuilder builder, EntityDefinition definition) {
    if (definition.isStringId() && definition.hasParent()) {
      builder.put(Constants.DAO_NAME, Constants.STRING_ID_WITH_PARENT_DAO)
          .put(Constants.ASYNC_DAO_NAME, Constants.ASYNC_STRING_ID_WITH_PARENT_DAO);
    } else if (!definition.isStringId() && definition.hasParent()) {
      builder.put(Constants.DAO_NAME, Constants.LONG_ID_WITH_PARENT_DAO)
          .put(Constants.ASYNC_DAO_NAME, Constants.ASYNC_LONG_ID_WITH_PARENT_DAO);
    } else if (!definition.isStringId()) {
      builder.put(Constants.DAO_NAME, Constants.LONG_ID_DAO)
          .put(Constants.ASYNC_DAO_NAME, Constants.ASYNC_LONG_ID_DAO);
    } else {
      builder.put(Constants.DAO_NAME, Constants.STRING_ID_DAO)
          .put(Constants.ASYNC_DAO_NAME, Constants.ASYNC_STRING_ID_DAO);
    }
  }

  private EntityDefinition getEntityDefinition(TypeElement entity) {
    EntityDefinition definition = new EntityDefinition();
    definition.setPackageName(Classes.getPackage(entity));
    return definition;
  }

  private SourceFileBuilder getSourceFileBuilder(TypeElement entity, EntityDefinition definition) {
    for (VariableElement field : Classes.getFields(entity)) {
      updateDefinition(entity, field, definition);
      if (definition.hasParent() && definition.hasId()) {
        break;
      }
    }
    return new SourceFileBuilder();
  }

  private void updateDefinition(TypeElement entity, VariableElement field, EntityDefinition definition) {
    updateForParent(entity, field, definition);
    updateForId(field, definition);
  }

  private void updateForId(VariableElement field, EntityDefinition definition) {
    if (definition.hasId() || field.getAnnotation(Id.class) == null) {
      return;
    }
    TypeMirror stringType = typeCache.getTypeMirror(String.class);
    definition.setStringId(types.isSameType(field.asType(), stringType));
  }

  private void updateForParent(TypeElement entity, VariableElement field, EntityDefinition definition) {
    if (definition.hasParent() || field.getAnnotation(Parent.class) == null) {
      return;
    }
    definition.setParentType(getParentType(entity, field.asType()));
  }

  private TypeMirror getParentType(TypeElement entity, TypeMirror parentType) {
    validateParentType(entity, parentType);
    TypeMirror argument = getParameterizedArgument(entity, parentType);
    validateParameterizedParent(entity, parentType, argument);
    return argument;
  }

  private void validateParentType(TypeElement entity, TypeMirror parentType) {
    if (types.isSameType(parentType, typeCache.getTypeMirror(com.google.appengine.api.datastore.Key.class))) {
      throw new ProcessingException(UNSUPPORTED_PARENT_TYPE, entity);
    }

    if (!Classes.isParameterizedType(parentType)) {
      throw new ProcessingException(NOT_PARAMETERIZED_PARENT_TYPE, parentType, entity);
    }
  }

  private void validateParameterizedParent(TypeElement entity, TypeMirror parentType, TypeMirror argument) {
    DeclaredType expectedType = types.getDeclaredType(typeCache.getTypeElement(Ref.class), argument);
    if (types.isSameType(expectedType, parentType)) {
      return;
    }
    expectedType = types.getDeclaredType(typeCache.getTypeElement(Key.class), argument);
    if (types.isSameType(expectedType, parentType)) {
      return;
    }
    throw new ProcessingException(BAD_PARENT_TYPE, parentType, entity);
  }

  private TypeMirror getParameterizedArgument(TypeElement entity, TypeMirror parentType) {
    List<? extends TypeMirror> arguments = ((DeclaredType) parentType).getTypeArguments();
    if (arguments.size() != 1) {
      throw new ProcessingException(INVALID_ARGUMENT_COUNT, arguments, entity);
    }
    return arguments.get(0);
  }
}
