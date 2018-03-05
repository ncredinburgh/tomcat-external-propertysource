package com.github.ncredinburgh.tomcat.encryption;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class JCACipherTest {

	private static final String clearText = "Sup3rS3cr3tP455w0rd";	
		
	@Parameterized.Parameters(name = "cipherSpec={0}")
	public static Iterable<Object[]> data() {
	    return Arrays.asList(new Object[][] {
	    	
	    	// Algorithm: DES with 56bit key
	    	// Cipher Spec               Key                                 IV                          Cipher Text
	    	//--------------------------------------------------------------------------------------------------------------------------------
	        { "DES/ECB/PKCS5PADDING",    "XQhkqA5P6fQ=",                     null,                       "/o8fTcv6GX7/PXpSH0/nd1nweZ+KxRJ0" },
	        { "DES/CBC/PKCS5PADDING",    "XQhkqA5P6fQ=",                     "j9HUMAR7C0Q=",             "3nUtLFtS4O99J+xsBl/NdLvGJviomFjH" },
	        { "DES/CFB/PKCS5PADDING",    "XQhkqA5P6fQ=",                     "Yrs0U7wC8u0=",             "ju4W10APZ3IgTv4QUczIkggT8MwzKg7a" },
	        { "DES/OFB/PKCS5PADDING",    "XQhkqA5P6fQ=",                     "sLMCz2tZzIo=",             "yGBxO7VBj8QZUmoVrOSYGvxqJPJC9BcW" },
	      
	        // Algorithm: 3DES (DESede) with 168bit key  
	        // Cipher Spec               Key                                 IV                          Cipher Text
	    	//--------------------------------------------------------------------------------------------------------------------------------
	        { "DESede/ECB/PKCS5PADDING", "a24aEz1StoBtdpv4pEYINOa1p/hFApGr", null,                       "kqo2vmhIubOeTHvB/2HrHO1sLv3Mtmiu" },
	        { "DESede/CBC/PKCS5PADDING", "a24aEz1StoBtdpv4pEYINOa1p/hFApGr", "/SkXL8bHma8=",             "ekWKRTjPYVVmrnCyowPbyHQ1HczTqSho" },
	        { "DESede/CFB/PKCS5PADDING", "a24aEz1StoBtdpv4pEYINOa1p/hFApGr", "sospt8ojpkE=",             "yF9Ua2D8/AxepaPGTg4YllOYSW/+ZSNm" },
	        { "DESede/OFB/PKCS5PADDING", "a24aEz1StoBtdpv4pEYINOa1p/hFApGr", "dBd0u3P1me8=",             "lGcGB0Vz2fEqCeseOEv0FGDS6E7rGjIP" },
	        
	    	// Algorithm: AES with 128bit key
	    	// Cipher Spec               Key                                 IV                          Cipher Text
	    	//--------------------------------------------------------------------------------------------------------------------------------
	        { "AES/ECB/PKCS5PADDING",    "9ZF5DqJA9O8F6sWSJuEehQ==",         null,                       "C0iZc6o+6xqr0NggmuTo9gRtfowg0kyM8fqNQEJwAZE=" },
	        { "AES/CBC/PKCS5PADDING",    "9ZF5DqJA9O8F6sWSJuEehQ==",         "J+tTz0vngLRvr061dPMEgA==", "vm3WvOXgmCY8nC9FjckOrbeM/C+ct7kC7XH3cmhL2f0=" },
	        { "AES/CFB/PKCS5PADDING",    "9ZF5DqJA9O8F6sWSJuEehQ==",         "xgr4IhKh6FKD9C8rtpMwmw==", "RRmAes8HG7KwyLtu+OzSne2PuYbo4U4/lek1V0BEmxs=" },
	        { "AES/OFB/PKCS5PADDING",    "9ZF5DqJA9O8F6sWSJuEehQ==",         "uKyDcdNnrCdAQdTSI1kUFQ==", "W6qXO0TOX8kEY/wTTC+rgqxvTDWsmB3jqoLL8fQzcsM=" }
	    });
	}
	
	private final String cipherSpec;
	private final String key;
	private final String iv;
	private final String cipherText;
	
	public JCACipherTest(String cipherSpec, String key, String iv, String cipherText) {
		this.cipherSpec = cipherSpec;
		this.key = key;
		this.iv = iv;
		this.cipherText = cipherText;
	}
	
	@Test
	public void shouldEncryptValueUsing() throws Exception {
		JCACipher testee = new JCACipher(cipherSpec, parseBase64Binary(key), iv == null ? null : parseBase64Binary(iv));
		
		byte[] actualCipherBytes = testee.encrypt(clearText.getBytes());
		
		assertThat(printBase64Binary(actualCipherBytes), is(equalTo(cipherText)));
	}	
	
	@Test
	public void shouldDecryptValueUsing() throws Exception {
		JCACipher testee = new JCACipher(cipherSpec, parseBase64Binary(key), iv == null ? null : parseBase64Binary(iv));
		
		byte[] actualClearBytes = testee.decrypt(parseBase64Binary(cipherText));
		
		assertThat(new String(actualClearBytes), is(equalTo(clearText)));
	}
	
	@Test
	@Ignore
	public void generateKey() throws Exception {
		JCAKeyGenerator generator = new JCAKeyGenerator("DESede");
		byte[] key = generator.generateKey(168);
		System.out.println(printBase64Binary(key));
	}
}
