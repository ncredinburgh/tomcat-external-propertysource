package com.github.ncredinburgh.tomcat.command;

import javax.xml.bind.DatatypeConverter;

public class IVEncoder {
	
	public static String encodeIV(byte[] iv) {
		return DatatypeConverter.printBase64Binary(iv);
	}

	public static byte[] decodeIV(String iv) {
		if (iv == null) return null;
		return DatatypeConverter.parseBase64Binary(iv);
	}
}
