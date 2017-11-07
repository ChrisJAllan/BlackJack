/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chrisallan.util;

import java.util.function.Consumer;

/**
 * Creates a runnable with no argument from a consumer with one argument and an
 * argument.
 * 
 * Partially replaces C++ bind(). I miss C++.
 * 
 * @author chris
 * @param <T> Argument type
 */
public class Binding<T> implements Runnable
{
	private final Consumer<T> function;
	private final T value;
	
	public Binding(Consumer<T> c, T v)
	{
		this.function = c;
		this.value = v;
	}
	
	@Override
	public void run()
	{
		function.accept(value);
	}
}

