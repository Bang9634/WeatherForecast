package com.bang9634.service;

@FunctionalInterface
public interface ServiceKeyValidator {
    boolean validate(String key);
}