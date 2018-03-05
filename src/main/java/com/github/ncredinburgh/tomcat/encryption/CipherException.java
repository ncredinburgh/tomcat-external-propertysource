package com.github.ncredinburgh.tomcat.encryption;

@SuppressWarnings("serial")
public class CipherException extends RuntimeException {

		public CipherException(Throwable t) {
			super(t);
		}
		
		public CipherException(String message) {
			super(message);
		}
}
