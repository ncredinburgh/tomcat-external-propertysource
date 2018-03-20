package com.github.ncredinburgh.tomcat.command;

import java.security.Provider;
import java.security.Security;
import java.util.Queue;

class ListCiphersCommand implements Command {
	private static final String COMMAND_KEY = "listCiphers";
	private static final String COMMAND_USAGE= COMMAND_KEY;
	
	@Override
	public String getCommandKey() {
		return COMMAND_KEY;
	}

	@Override
	public void doCommand(Options options, Queue<String> arguments)  {
		// No arguments to process
		listCiphers();
	}

	@Override
	public String getUsage() {
		return COMMAND_USAGE;
	}

	private void listCiphers() {
		for (Provider provider : Security.getProviders()) {
			for (Provider.Service service : provider.getServices()) {
				if (service.getType().equals("Cipher")) {
					System.out.println("Provider: " + provider.getName() + "  Algorithm: " + service.getAlgorithm());
				}
			}
		}
	}
}
