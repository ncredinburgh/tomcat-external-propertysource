package com.github.ncredinburgh.tomcat.command;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Queue;

public interface Command {
	
	public String getCommandKey();
	
	public void doCommand(Options options, Queue<String> arguments) throws UsageException, IOException, GeneralSecurityException;
	
	public String getUsage();
}
