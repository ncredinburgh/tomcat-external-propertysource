package com.github.ncredinburgh.tomcat;

import static java.lang.String.format;

public interface Defaults {

	public static final String DEFAULT_ALGORITHM = "AES";
	public static final String DEFALT_MODE = "ECB";
	public static final String DEFAULT_PADDING = "PKCS5PADDING";
	public static final String DEFAULT_CIPHER_SPEC = format("%s/%s/%s", DEFAULT_ALGORITHM, DEFALT_MODE, DEFAULT_PADDING);
	public static final int DEFAULT_KEY_SIZE = 128;
}
