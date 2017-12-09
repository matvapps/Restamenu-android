package com.restamenu.util;

/**
 * @author Roodie
 */

public interface Predicate<T> {
    boolean apply(T type);
}