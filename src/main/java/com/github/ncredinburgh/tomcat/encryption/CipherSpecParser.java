package com.github.ncredinburgh.tomcat.encryption;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CipherSpecParser {
	private static final Pattern PATTERN = Pattern.compile("^(.*)/(.*)/(.*)$");
	private final Matcher matcher;
	
	public CipherSpecParser(String cipherSpec) {
		if (cipherSpec == null) {
			throw new NullPointerException();
		}
		
		matcher = PATTERN.matcher(cipherSpec);
		
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Malformed cipher spec: " + cipherSpec);
		}
	}

	public String getAlgorithmName() {
		return matcher.group(1);
	}

	public String getMode() {
		return matcher.group(2);
	}
	
	public String getPadding() {
		return matcher.group(3);
	}
}
