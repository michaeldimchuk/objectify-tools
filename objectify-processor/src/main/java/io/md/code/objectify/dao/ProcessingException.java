package io.md.code.objectify.dao;

class ProcessingException extends RuntimeException {

  ProcessingException(String message) {
    super(message);
  }

  ProcessingException(String message, Object... args) {
    super(String.format(message, args));
  }
}
