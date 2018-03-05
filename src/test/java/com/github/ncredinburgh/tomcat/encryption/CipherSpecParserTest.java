package com.github.ncredinburgh.tomcat.encryption;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class CipherSpecParserTest {
	
	@Rule
	public final ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void shouldReturnAlgNameModeAndPadding() throws Exception {
		String cipherSpec = "foo/bar/baz";
		CipherSpecParser testee = new CipherSpecParser(cipherSpec);
		
		Assert.assertEquals("foo", testee.getAlgorithmName());
		Assert.assertEquals("bar", testee.getMode());
		Assert.assertEquals("baz", testee.getPadding());
	}
	
	@Test
	public void shouldThrowExceptionWhenNullCipherSpec() throws Exception {
		thrown.expect(NullPointerException.class);
		
		new CipherSpecParser(null);
	}
	
	@Test
	public void shouldThrowExceptionWhenMalformedCipherSpec() throws Exception {
		thrown.expect(IllegalArgumentException.class);
		
		new CipherSpecParser("foo");
	}
}
