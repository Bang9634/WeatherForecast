package com.bang9634.service;

/**
 * ServiceKeyValidator 인터페이스는 서비스 키의 유효성을 검사하는 메서드를 정의한다.
 * <p>
 * 이 인터페이스를 구현하는 클래스는 서비스 키가 유효한지 여부를 판단하는 로직을 제공해야 한다.
 * 
 * @author bangdeokjae
 */
@FunctionalInterface
public interface ServiceKeyValidator {
    boolean validate(String key);
}