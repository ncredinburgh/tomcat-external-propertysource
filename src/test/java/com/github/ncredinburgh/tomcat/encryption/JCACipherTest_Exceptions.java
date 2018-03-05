package com.github.ncredinburgh.tomcat.encryption;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class JCACipherTest_Exceptions {
	
	private final byte[] goodKey = parseBase64Binary("9ZF5DqJA9O8F6sWSJuEehQ==");
	private final String goodCipherSpec = "AES/ECB/PKCS5PADDING";
	private JCACipher testee;
	
	@Rule
	public final ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldThrowExceptionWhenMissingCipherSpec() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("No cipher specified");

		testee = new JCACipher(null, goodKey);
	}
	
	@Test
	public void shouldThrowExceptionWhenMissingKey() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Missing keyBytes");
		
		testee = new JCACipher(goodCipherSpec, null);
	}

	@Test
	public void shouldThrowExceptionWhenMalformedCipherSpec() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Malformed cipher spec");
		
		testee = new JCACipher("foo", goodKey);
	}

	
	@Test
	public void shouldThrowExceptionWhenNoSuchAlgorithm() throws Exception {
		thrown.expect(CipherException.class);
		thrown.expectMessage("NoSuchAlgorithmException");
		
		testee = new JCACipher("foo/bar/baz", goodKey);
	}	

	@Test
	public void shouldThrowExceptionWhenDecryptWithoutCipherBytes() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Missing cipherBytes");

		testee = new JCACipher(goodCipherSpec, goodKey);
		testee.decrypt(null);
	}

	@Test
	public void shouldThrowExceptionWhenEncryptWithoutClearBytes() throws Exception {	
		thrown.expect(IllegalArgumentException.class);
		thrown.expectMessage("Missing clearBytes");

		testee = new JCACipher(goodCipherSpec, goodKey);
		testee.encrypt(null);
	}
}
