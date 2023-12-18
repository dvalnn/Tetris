package com.apontadores.totris.ui;

/**
 * Interface for Button actions
 * 
 * @param <T> action type of args
 * @param <V> action return type
 */
public interface ButtonAction<T, V> {
  /**
   * @param args type T variable
   * @return type V value
   */
  public V exec(T args);
}
