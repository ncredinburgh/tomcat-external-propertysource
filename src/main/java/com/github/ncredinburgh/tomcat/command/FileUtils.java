package com.github.ncredinburgh.tomcat.command;

import static java.lang.String.format;
import static java.nio.file.Files.delete;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.util.Scanner;

public class FileUtils {
	
	public static void removeInputFileIfRequired(Options options, String inputFilename) throws IOException {
		boolean removeInputFile;
		
		if (options.containsKey(Options.OPT_REMOVE)) removeInputFile= true;
		else if (options.containsKey(Options.OPT_KEEP)) removeInputFile = false;
		else removeInputFile = askToRemoveInputFile(inputFilename);
		
		if (removeInputFile) {
			delete(get(inputFilename));
			System.out.println(format("Input file %s removed", inputFilename));
		} else {
			System.out.println(format("Input file %s has not been removed", inputFilename));
		} 
	}
	
	private static boolean askToRemoveInputFile(String inputFilename) {
		System.out.print(format("Remove input file %s? (Y|n) ", inputFilename));
		Scanner scanner = new Scanner(System.in);
		String response = scanner.nextLine();
		scanner.close();
		return isYes(response);
	}

	private static boolean isYes(String response) {
		return response.length() ==0 || response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("YES");
	}
}
