package io.md.code.objectify.dao;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import com.google.auto.service.AutoService;
import com.googlecode.objectify.annotation.Entity;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoService(Processor.class)
public class ObjectifyProcessor extends AbstractProcessor {

  @NonFinal
  Elements elements;

  @NonFinal
  Types types;

  @NonFinal
  Filer filer;

  @Override
  public synchronized void init(ProcessingEnvironment environment) {
    super.init(environment);
    elements = environment.getElementUtils();
    types = environment.getTypeUtils();
    filer = environment.getFiler();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(Entity.class.getCanonicalName());
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment environment) {
    if (annotations.isEmpty()) {
      return false;
    }
    Set<? extends Element> entities = environment.getElementsAnnotatedWith(Entity.class);
    processSafely(toTypeElements(entities));
    return true;
  }

  private void processSafely(List<TypeElement> entities) {
    try {
      ObjectifyDaoGenerator generator = new ObjectifyDaoGenerator(elements, types, filer);
      entities.forEach(generator::generate);
    } catch (Exception e) {
      log.error("Failed to create Objectify Dao implementations", e);
      throw e;
    }
  }

  private List<TypeElement> toTypeElements(Set<? extends Element> elements) {
    return elements.stream()
        .filter(Classes::isClass)
        .filter(TypeElement.class::isInstance)
        .map(TypeElement.class::cast)
        .collect(Collectors.toList());
  }
}
