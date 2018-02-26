package com.github.ncredinburgh.tomcat.encryption;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A cipher implementation that uses the Java Cryptography Architecture (JCA) to perform the cipher operations.
 * The cipher is specified by a <em>cipherSpec</em> string in the format defined in {@link javax.crypto.Cipher}.
 * The file specified by keyFilename must exist and be readable by the current user.
 */
public class JCACipher implements Cipher {
	
	private final javax.crypto.Cipher cipher;
	private final SecretKeySpec keySpec;
	private IvParameterSpec ivSpec;
	
	public JCACipher(String cipherSpec, byte[] key) {		
		this(cipherSpec, key, null);
	}
	
	public JCACipher(String cipherSpec, byte[] key, byte[] iv) {
		if (cipherSpec == null) throw new IllegalArgumentException("No cipher specified");
		if (key == null) throw new IllegalArgumentException("Missing keyBytes");

		try {
			String algorithmName = new CipherSpecParser(cipherSpec).getAlgorithmName();
			cipher = javax.crypto.Cipher.getInstance(cipherSpec);
			keySpec = new SecretKeySpec(key, algorithmName);
			ivSpec = iv == null ? null : new IvParameterSpec(iv);
		} catch(GeneralSecurityException e) {
			throw new CipherException(e);
		}
	}
	
	public byte[] decrypt(byte[] cipherBytes) throws CipherException {
		if (cipherBytes == null) throw new IllegalArgumentException("Missing cipherBytes");
		
		try {
	        cipher.init(DECRYPT_MODE, keySpec, ivSpec);
	        byte[] clearBytes = cipher.doFinal(cipherBytes);
	        return clearBytes;
		} catch (GeneralSecurityException e) {
			throw new CipherException(e);
		}
	}

	public byte[] encrypt(byte[] clearBytes) throws CipherException {
		if (clearBytes == null) throw new IllegalArgumentException("Missing clearBytes");
		
		try {
			cipher.init(ENCRYPT_MODE, keySpec, ivSpec);
			storeAnyIv(cipher.getParameters());
	        byte[] cipherBytes = cipher.doFinal(clearBytes);
	        return cipherBytes;
		} catch (GeneralSecurityException e) {
			throw new CipherException(e);
		} 
	}
	
	byte[] getIV() {
		return ivSpec == null ? null : ivSpec.getIV();
	}
	
	private void storeAnyIv(AlgorithmParameters parameters) throws GeneralSecurityException {
		if (parameters != null) {
			ivSpec = parameters.getParameterSpec(IvParameterSpec.class);
		}	
	}
}
