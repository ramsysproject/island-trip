package com.emramirez.islandtrip.validation;

/**
 * This interface is used to validate ${@link T} types
 */
@FunctionalInterface
public interface Validator<T> {

    void validate(T t);
}
