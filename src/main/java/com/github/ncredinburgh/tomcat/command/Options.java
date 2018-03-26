package com.github.ncredinburgh.tomcat.command;

import java.util.HashMap;

public class Options extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;

	public static final String OPT = "-";
	public static final String OPT_KEEP = OPT + "keep";
	public static final String OPT_REMOVE = OPT + "remove";
	private static final String OPT_QUIET = OPT + "quiet";

	public boolean isQuiet() {
		return containsKey(OPT_QUIET);
	}
}
