package com.github.ncredinburgh.tomcat.encryption;

import static java.nio.file.Files.readAllBytes;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileEncryptor {

	private final Cipher cipher;

	public FileEncryptor(Cipher cipher) {
		this.cipher = cipher;
	}

	public InputStream encryptFile(File clearFile) throws CipherException, IOException {
		byte[] cipherBytes = encrypt(clearFile);
		
		return new ByteArrayInputStream(cipherBytes);
	}
	
	public void encryptFile(File clearFile, File cipherFile) throws CipherException, IOException {
		if (cipherFile == null) throw new IllegalArgumentException("Missing cipherFile");
		byte[] cipherBytes = encrypt(clearFile);
		
		Files.write(cipherFile.toPath(),  cipherBytes);
	}

	public InputStream decryptFile(File cipherFile) throws CipherException, IOException {
		byte[] clearBytes = decrypt(cipherFile);
		
		return new ByteArrayInputStream(clearBytes);
	}
	
	public void decryptFile(File cipherFile, File clearFile) throws CipherException, IOException {
		if (clearFile == null) throw new IllegalArgumentException("Missing clearFile");
		byte[] clearBytes = decrypt(cipherFile);
		
		Files.write(clearFile.toPath(),  clearBytes);
	}
	
	private byte[] encrypt(File clearFile) throws IOException {
		if (clearFile == null) throw new IllegalArgumentException("Missing clearFile");
		
		byte[] clearBytes = readAllBytes(clearFile.toPath());
		byte[] cipherBytes = cipher.encrypt(clearBytes);
		return cipherBytes;
	}

	private byte[] decrypt(File cipherFile) throws CipherException, IOException {
		if (cipherFile == null) throw new IllegalArgumentException("Missing cipherFile");
		
		byte[] cipherBytes = readAllBytes(cipherFile.toPath());
		byte[] clearBytes = cipher.decrypt(cipherBytes);
		
		return clearBytes;
	}
}
