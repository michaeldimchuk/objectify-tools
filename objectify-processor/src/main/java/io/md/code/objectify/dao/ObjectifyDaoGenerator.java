package io.md.code.objectify.dao;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;

import com.google.common.collect.ImmutableMap;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ObjectifyDaoGenerator {

  private static final Logger log = LoggerFactory.getLogger(ObjectifyDaoGenerator.class);

  private static final String UNSUPPORTED_PARENT_TYPE = "Google's native key type as a parent is not supported, entity was %s";

  private static final String NOT_PARAMETERIZED_PARENT_TYPE = "Only parameterized types can be parents, found %s in entity %s";

  private static final String INVALID_ARGUMENT_COUNT = "Must have only 1 type argument, found %s on entity %";

  private static final String BAD_PARENT_TYPE = "Objectify only supports Key or Ref type parent fields, but found %s on entity %s";

  private static final String ASYNC_DAO_NAME_TEMPLATE = "%s.Async%sDao";

  private static final String DAO_NAME_TEMPLATE = "%s.%sDao";

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
    SourceFileBuilder builder = getSourceFileBuilder(entity, definition);
    populateSourceFileBuilder(entity, definition, builder);
    writeObjectifyDaos(entity, definition, builder);
  }

  private void populateSourceFileBuilder(TypeElement entity, EntityDefinition definition, SourceFileBuilder builder) {
    getDefaultTemplate().forEach(builder::put);
    definition.toContext().forEach(builder::put);
    addDaoName(builder, definition);
    addParentPackage(entity, builder, definition);
  }

  private Map<String, String> getDefaultTemplate() {
    return ImmutableMap.of(
        Constants.PROCESSOR, ObjectifyProcessor.class.getCanonicalName(),
        Constants.DAO_PACKAGE, Constants.DAO_PACKAGE_NAME
    );
  }

  private void writeObjectifyDaos(TypeElement entity, EntityDefinition definition, SourceFileBuilder builder) {
    DaoConfiguration configuration = DaoConfiguration.of(definition.isStringId(), definition.hasParent());
    String asyncSourceCode = builder.build(configuration.getAsyncTemplate());
    String syncSourceCode = builder.build(configuration.getSyncTemplate());
    String asyncDaoName = getObjectifyDaoName(ASYNC_DAO_NAME_TEMPLATE, entity);
    String syncDaoName = getObjectifyDaoName(DAO_NAME_TEMPLATE, entity);
    writeSourceFile(asyncDaoName, asyncSourceCode);
    writeSourceFile(syncDaoName, syncSourceCode);
  }

  private void writeSourceFile(String className, String sourceCode) {
    try {
      JavaFileObject daoFile = filer.createSourceFile(className);
      try (Writer writer = daoFile.openWriter()) {
        writer.write(sourceCode);
      }
      log.debug("Generated {}", className);
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
    DaoConfiguration configuration = DaoConfiguration.of(definition.isStringId(), definition.hasParent());
    builder.put(Constants.DAO_NAME, configuration.getSyncDao())
        .put(Constants.ASYNC_DAO_NAME, configuration.getAsyncDao());
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

  private EntityDefinition getEntityDefinition(TypeElement entity) {
    EntityDefinition definition = new EntityDefinition();
    definition.setPackageName(Classes.getPackage(entity));
    definition.setSimpleName(entity.getSimpleName().toString());
    return definition;
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
