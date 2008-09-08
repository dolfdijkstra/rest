package com.fatwire.cs.rest;

public interface HttpCommand<T> {

	T execute() throws CommandException;
	
}
