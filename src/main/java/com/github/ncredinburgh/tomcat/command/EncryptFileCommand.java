package com.github.ncredinburgh.tomcat.command;

import static com.github.ncredinburgh.tomcat.command.FileUtils.println;
import static com.github.ncredinburgh.tomcat.command.FileUtils.removeInputFileIfRequired;
import static com.github.ncredinburgh.tomcat.command.IVEncoder.decodeIV;
import static com.github.ncredinburgh.tomcat.command.IVEncoder.encodeIV;
import static com.github.ncredinburgh.tomcat.encryption.PropertiesUtil.isPropertiesFile;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.NoSuchElementException;
import java.util.Queue;

import com.github.ncredinburgh.tomcat.Defaults;
import com.github.ncredinburgh.tomcat.encryption.Cipher;
import com.github.ncredinburgh.tomcat.encryption.FileEncryptor;
import com.github.ncredinburgh.tomcat.encryption.JCACipher;

class EncryptFileCommand implements Command {
	private static final String COMMAND_KEY = "encryptFile";
	private static final String COMMAND_USAGE= COMMAND_KEY + " <inputFilename> <keyFilename> <outputFilename> [<algorithm/mode/padding>] [iv]";
	
	@Override
	public String getCommandKey() {
		return COMMAND_KEY;
	}

	@Override
	public void doCommand(Options options, Queue<String> arguments) throws UsageException, IOException, GeneralSecurityException {
		try {
			String cipherSpec = Defaults.DEFAULT_CIPHER_SPEC;
			String iv = null;
			
			String inputFilename = arguments.remove();
			String keyFilename = arguments.remove();
			String outputFilename = arguments.remove();
	
			if (!arguments.isEmpty()) {
				cipherSpec = arguments.remove();
			}
			if (!arguments.isEmpty()) {
				iv = arguments.remove();
			}
			
			if (!isPropertiesFile(new FileReader(inputFilename))) {
				println("Warning: The input file does not look like a Java properties file", options.isQuiet());
			}
			byte[] ivUsed = encryptFile(inputFilename, keyFilename, outputFilename, cipherSpec, decodeIV(iv));
			println(format("File %s encrypted to file %s", inputFilename, outputFilename), options.isQuiet());
			if (iv == null && ivUsed != null) {
				println("To decode use IV: " + encodeIV(ivUsed), options.isQuiet());
			}
			removeInputFileIfRequired(options, inputFilename);
 		} catch (NoSuchElementException e) {
			throw new UsageException(e);
		}
	}

	@Override
	public String getUsage() {
		return COMMAND_USAGE;
	}
	
	private static byte[] encryptFile(String inputFilename, String keyFilename, String outputFilename, String cipherSpec, byte[] iv) 
			throws IOException, GeneralSecurityException {
		Cipher cipher = new JCACipher(cipherSpec, readAllBytes(get(keyFilename)), iv);
		FileEncryptor encryptor = new FileEncryptor(cipher);
		encryptor.encryptFile(new File(inputFilename), new File(outputFilename));
		return cipher.getIV();
	}
}
