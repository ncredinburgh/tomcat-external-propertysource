package com.github.ncredinburgh.tomcat.encryption;

import static com.github.ncredinburgh.tomcat.encryption.PropertiesUtil.isPropertiesFile;
import static java.lang.String.format;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

import com.github.ncredinburgh.tomcat.Defaults;

public class Main {
	
	public static void main(String[] args) throws Exception {
		Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
		try {
			String command = arguments.remove();
			if (command.equals("listKeyGenerators")) listKeyGenerators(arguments);
			else if (command.equals("listCiphers")) listCiphers(arguments);
			else if (command.equals("generateKey")) generateKey(arguments);
			else if (command.equals("encryptFile")) encryptFile(arguments);
			else if (command.equals("decryptFile")) decryptFile(arguments);
			else printUsage();
		} catch (NoSuchElementException e) {
			printUsage();
		}
	}

	private static void printUsage() {
		System.out.println("Usage: tomcat-external-propertysource <command> <options>");
		System.out.println("Commands:");
		System.out.println("    listKeyGenerators");
		System.out.println("    listCiphers");
		System.out.println("    generateKey <keyFilename> [<algorithm> <keySize>]");
		System.out.println("    encryptFile <inputFilename> <keyFilename> <outputFilename> [<algorithm/mode/padding>] [iv]");
		System.out.println("    decryptFile <inputFilename> <keyFilename> <outputFilename> [<algorithm/mode/padding>] [iv]");
		System.out.println("Defaults:");
		System.out.println("    <algorithm> " + Defaults.DEFAULT_ALGORITHM);
		System.out.println("    <keySize>   " + Defaults.DEFAULT_KEY_SIZE);	
		System.out.println("    <mode>      " + Defaults.DEFALT_MODE);
		System.out.println("    <padding>   " + Defaults.DEFAULT_PADDING);
	}
	
	private static void generateKey(Queue<String> arguments) throws GeneralSecurityException, IOException {
		try {
			String algorithm = Defaults.DEFAULT_ALGORITHM;
			int keySize = Defaults.DEFAULT_KEY_SIZE;
			String keyFilename = arguments.remove();

			if (!arguments.isEmpty()) {
				algorithm = arguments.remove();
				keySize = Integer.parseInt(arguments.remove());
			}
			
			generateKey(keyFilename, algorithm, keySize);
			System.out.println("New key written to file: " + keyFilename);
		} catch (NoSuchElementException e) {
			printUsage();
		}
	}

	
	private static void encryptFile(Queue<String> arguments) throws GeneralSecurityException, IOException {
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
				System.out.println("Warning: The input file does not look like a Java properties file");
			}
			byte[] ivUsed = encryptFile(inputFilename, keyFilename, outputFilename, cipherSpec, decodeIV(iv));
			System.out.println(format("File %s encrypted to file %s", inputFilename, outputFilename));
			if (iv == null && ivUsed != null) {
				System.out.println("To decode use IV: " + encodeIV(ivUsed)); 
			}
			removeInputFileIfRequired(inputFilename);
 		} catch (NoSuchElementException e) {
			printUsage();
		}
	}

	private static void removeInputFileIfRequired(String inputFilename) throws IOException {
		System.out.print(format("Remove input file %s? (Y|n) ", inputFilename));
		Scanner scanner = new Scanner(System.in);
		String response = scanner.nextLine();
		if (isYes(response)) {
			delete(get(inputFilename));
			System.out.println(format("Input file %s removed", inputFilename));
		} else {
			System.out.println(format("Input file %s has not been removed", inputFilename));
		}
		scanner.close();
	}
	
	private static void decryptFile(Queue<String> arguments) throws GeneralSecurityException, IOException {
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
			
			if (isPropertiesFile(new FileReader(inputFilename))) {
				System.out.println("Warning: The input file does not look like an encrypted file");
			}
			decryptFile(inputFilename, keyFilename, outputFilename, cipherSpec, decodeIV(iv));
			System.out.println(format("File %s decrypted to file %s", inputFilename, outputFilename));
		} catch (NoSuchElementException e) {
			printUsage();
		}
	}

	private static void listKeyGenerators(Queue<String> arguments) {
		for (Provider provider : Security.getProviders()) {
			for (Provider.Service service : provider.getServices()) {
				if (service.getType().equals("KeyGenerator")) {
					System.out.println("Provider: " + provider.getName() + "  Algorithm: " + service.getAlgorithm());
				}
			}
		}
	}

	private static void listCiphers(Queue<String> arguments) {
		for (Provider provider : Security.getProviders()) {
			for (Provider.Service service : provider.getServices()) {
				if (service.getType().equals("Cipher")) {
					System.out.println("Provider: " + provider.getName() + "  Algorithm: " + service.getAlgorithm());
				}
			}
		}
	}

	private static void generateKey(String keyFilename, String algorithm, int keySize) throws GeneralSecurityException, IOException {
		JCAKeyGenerator keyGenerator = new JCAKeyGenerator(algorithm);
		byte[] keyBytes = keyGenerator.generateKey(keySize);
		File keyFile = new File(keyFilename);
		Files.write(keyFile.toPath(), keyBytes);
	}

	private static byte[] encryptFile(String inputFilename, String keyFilename, String outputFilename, String cipherSpec, byte[] iv) 
			throws IOException, GeneralSecurityException {
		JCACipher cipher = new JCACipher(cipherSpec, readAllBytes(get(keyFilename)), iv);
		FileEncryptor encryptor = new FileEncryptor(cipher);
		encryptor.encryptFile(new File(inputFilename), new File(outputFilename));
		return cipher.getIV();
	}
	
	private static void decryptFile(String inputFilename, String keyFilename, String outputFilename, String cipherSpec, byte[] iv) 
			throws IOException, GeneralSecurityException {
		JCACipher cipher = new JCACipher(cipherSpec, readAllBytes(get(keyFilename)), iv);
		FileEncryptor decryptor = new FileEncryptor(cipher);
		decryptor.decryptFile(new File(inputFilename), new File(outputFilename));
	}
	
	private static boolean isYes(String response) {
		return response.length() ==0 || response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("YES");
	}
	
	private static String encodeIV(byte[] ivUsed) {
		return DatatypeConverter.printBase64Binary(ivUsed);
	}

	private static byte[] decodeIV(String iv) {
		if (iv == null) return null;
		return DatatypeConverter.parseBase64Binary(iv);
	}
}
