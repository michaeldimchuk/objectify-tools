package io.md.code.objectify.dao;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

class SourceFileBuilder {

  private final VelocityContext context = new VelocityContext();

  SourceFileBuilder put(String key, Object value) {
    context.put(key, value);
    return this;
  }

  String build(String templateName) {
    Template template = VelocityEngineHolder.INSTANCE.getTemplate(templateName);
    StringWriter stringWriter = new StringWriter();
    template.merge(context, stringWriter);
    return stringWriter.toString();
  }

  private static final class VelocityEngineHolder {

    private static final VelocityEngine INSTANCE = getVelocityEngine();

    private static VelocityEngine getVelocityEngine() {
      VelocityEngine velocityEngine = new VelocityEngine();
      Properties velocityProperties = new Properties();
      velocityProperties.put("resource.loader", "class");
      velocityProperties.put("class.resource.loader.description", "Velocity Classpath Resource Loader");
      velocityProperties.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
      velocityProperties.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogChute");
      velocityEngine.init(velocityProperties);
      return velocityEngine;
    }
  }
}
