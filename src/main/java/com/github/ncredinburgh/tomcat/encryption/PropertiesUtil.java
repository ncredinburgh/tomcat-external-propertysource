package com.github.ncredinburgh.tomcat.encryption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

public class PropertiesUtil {

	private static final Pattern format1 = Pattern.compile("^\\S* ?= ?.*$");
	private static final Pattern format2 = Pattern.compile("^\\S* ?: ?.*$");
	private static final Pattern format3 = Pattern.compile("^\\S* .*$");
	
	public static boolean isPropertiesFile(Reader reader) throws IOException {
		
		try (BufferedReader buffedReader = new BufferedReader(reader)) {
			String line;
			while ((line = buffedReader.readLine()) != null ) {
				line = line.trim();
				if (isBlankOrComment(line))
					continue;
				if (!isValidPropertyLine(line)) {
					return false;
				}
			}
			return true;
		}
	}

	private static boolean isValidPropertyLine(String line) {
		return format1.matcher(line).matches() ||
				format2.matcher(line).matches() ||
				format3.matcher(line).matches();
	}

	private static boolean isBlankOrComment(String line) {
		return line.length() == 0 || 
				line.startsWith("#") || 
				line.startsWith("!") ;
	}
}
