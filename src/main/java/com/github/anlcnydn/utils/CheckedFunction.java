package com.github.anlcnydn.utils;

// https://stackoverflow.com/a/18198349/
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {
  R apply(T t) throws E;
}
