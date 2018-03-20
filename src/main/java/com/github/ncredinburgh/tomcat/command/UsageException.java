package com.github.ncredinburgh.tomcat.command;

public class UsageException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public UsageException(Throwable t) {
		super(t);
	}

	public UsageException(String message) {
		super(message);
	}
}
