package com.github.ncredinburgh.tomcat.command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import com.github.ncredinburgh.tomcat.Defaults;

public class Main {
	
	private static final Command[] COMMANDS = new Command[] {
			new ListKeyGeneratorsCommand(),
			new ListCiphersCommand(),
			new GenerateKeyCommand(),
			new EncryptFileCommand(),
			new DecryptFileCommand()
	};
	
	public static void main(String[] args) throws Exception {
		try {
			Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
			String commandKey = arguments.remove();
			Command command = getCommand(commandKey);
			command.doCommand(arguments);
		} catch (NoSuchElementException | UsageException e) {
			printUsage();
		}
	}

	private static void printUsage() {
		System.out.println("Usage: tomcat-external-propertysource <command> <options>");
		System.out.println("Commands:");
		
		for (Command command : COMMANDS) {
			System.out.println("    " + command.getUsage());
		}

		System.out.println("Defaults:");
		System.out.println("    <algorithm> " + Defaults.DEFAULT_ALGORITHM);
		System.out.println("    <keySize>   " + Defaults.DEFAULT_KEY_SIZE);	
		System.out.println("    <mode>      " + Defaults.DEFALT_MODE);
		System.out.println("    <padding>   " + Defaults.DEFAULT_PADDING);
	}

	private static Command getCommand(String commandKey) throws UsageException {
		for (Command command : COMMANDS) {
			if (command.getCommandKey().equals(commandKey)) return command;
		}
		throw new UsageException("Unrecognised command: " + commandKey);
	}
}
