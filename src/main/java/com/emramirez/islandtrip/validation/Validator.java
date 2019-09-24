package com.emramirez.islandtrip.validation;

import com.emramirez.islandtrip.exception.ValidationException;

/**
 * This interface is used to validate ${@link T} types
 */
@FunctionalInterface
public interface Validator<T> {

    void validate(T t) throws ValidationException;
}
