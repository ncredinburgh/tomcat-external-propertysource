package com.github.ncredinburgh.tomcat.command;

import static com.github.ncredinburgh.tomcat.command.FileUtils.println;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.NoSuchElementException;
import java.util.Queue;

import com.github.ncredinburgh.tomcat.Defaults;
import com.github.ncredinburgh.tomcat.encryption.JCAKeyGenerator;

class GenerateKeyCommand implements Command {
	private static final String COMMAND_KEY = "generateKey";
	private static final String COMMAND_USAGE= COMMAND_KEY + " <keyFilename> [<algorithm> <keySize>]";
	
	@Override
	public String getCommandKey() {
		return COMMAND_KEY;
	}

	@Override
	public void doCommand(Options options, Queue<String> arguments) throws UsageException, GeneralSecurityException, IOException {
		try {
			String algorithm = Defaults.DEFAULT_ALGORITHM;
			int keySize = Defaults.DEFAULT_KEY_SIZE;
			String keyFilename = arguments.remove();

			if (!arguments.isEmpty()) {
				algorithm = arguments.remove();
				keySize = Integer.parseInt(arguments.remove());
			}
			
			generateKey(keyFilename, algorithm, keySize);
			println("New key written to file: " + keyFilename, options.isQuiet());
		} catch (NoSuchElementException e) {
			throw new UsageException(e);
		}
		
	}

	@Override
	public String getUsage() {
		return COMMAND_USAGE;
	}

	private static void generateKey(String keyFilename, String algorithm, int keySize) throws GeneralSecurityException, IOException {
		JCAKeyGenerator keyGenerator = new JCAKeyGenerator(algorithm);
		byte[] keyBytes = keyGenerator.generateKey(keySize);
		File keyFile = new File(keyFilename);
		Files.write(keyFile.toPath(), keyBytes);
	}
}
