package com.psw.tetris.ui;

/**
 * Interface for Button actions
 * 
 * @param <T> action type of args
 * @param <V> action return type
 * @return
 */
public interface ButtonAction<T, V> {
  public V exec(T args);
}
