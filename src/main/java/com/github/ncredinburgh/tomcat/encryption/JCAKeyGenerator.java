package com.github.ncredinburgh.tomcat.encryption;

import java.security.GeneralSecurityException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class JCAKeyGenerator {
	
	private final String algorithmName;

	public JCAKeyGenerator(String algorithmName) {
		this.algorithmName = algorithmName;
	}
	
	public byte[] generateKey(int keySize) { 
		try {
			KeyGenerator generator = KeyGenerator.getInstance(algorithmName);
			generator.init(keySize);
			SecretKey key = generator.generateKey();
			byte[] keyBytes = key.getEncoded();
			
			return keyBytes;
		} catch (GeneralSecurityException e) {
			throw new CipherException(e);
		}
	}
}

	