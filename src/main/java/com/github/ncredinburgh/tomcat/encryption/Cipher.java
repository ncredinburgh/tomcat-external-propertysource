package com.github.ncredinburgh.tomcat.encryption;

public interface Cipher {

	public byte[] encrypt(byte[] clearBytes) throws CipherException;

	public byte[] decrypt(byte[] cipherBytes) throws CipherException;

}
