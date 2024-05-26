package com.hirantha.storage.constants;

public final class ErrorConstants {

  public static final String INVALID_USER_NAME = "User name invalid!";
  public static final String NON_ALPHANUMERIC_USER_NAME = "User name can only have alphanumeric characters!";
  public static final String VISIBILITY_ENUM_INVALID = "Visibility value should be PUBLIC or PRIVATE!";
  public static final String INVALID_FILE_NAME = "File name invalid!";
  public static final String EMPTY_FILE = "File empty or invalid!";
  public static final String FILE_NAME_EXISTS = "File already exists with the provided name.";
  public static final String FILE_CONTENT_EXISTS = "File already exists with similar content.";
  public static final String NO_SUCH_FILE = "File not present!";
  public static final String FILE_NOT_FOUND = "File not found on disk!";

  private ErrorConstants() {
  }
}
