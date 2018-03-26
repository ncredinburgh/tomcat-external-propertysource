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
			Options options = processOptions(arguments);
			command.doCommand(options, arguments);
		} catch (NoSuchElementException | UsageException e) {
			printUsage();
		}
	}

	private static Options processOptions(Queue<String> arguments) {
		Options options = new Options();

		while (!arguments.isEmpty() && arguments.peek().startsWith(Options.OPT)) {
			String option = arguments.remove();
			options.put(option, null);
		}
		
		return options;
	}

	private static void printUsage() {
		System.out.println("Usage: tomcat-external-propertysource <command> [<options>] <arguments>");
		System.out.println("Commands:");
		
		for (Command command : COMMANDS) {
			System.out.println("    " + command.getUsage());
		}

		System.out.println("Defaults:");
		System.out.println("    <algorithm> " + Defaults.DEFAULT_ALGORITHM);
		System.out.println("    <keySize>   " + Defaults.DEFAULT_KEY_SIZE);	
		System.out.println("    <mode>      " + Defaults.DEFALT_MODE);
		System.out.println("    <padding>   " + Defaults.DEFAULT_PADDING);
		System.out.println("Options:");
		System.out.println("    -keep       Keep the input file after command encryptFile or decryptFile");
		System.out.println("    -remove     Remove the input file after command encryptFile or decryptFile");
		System.out.println("    -quiet      Suppress all info logging on standard out");
	}

	private static Command getCommand(String commandKey) throws UsageException {
		for (Command command : COMMANDS) {
			if (command.getCommandKey().equals(commandKey)) return command;
		}
		throw new UsageException("Unrecognised command: " + commandKey);
	}
}
