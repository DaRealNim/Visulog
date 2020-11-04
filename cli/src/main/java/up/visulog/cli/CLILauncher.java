package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.nio.file.FileSystems;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class CLILauncher {
	public static void main(String[] args) {
		
		var config = makeConfigFromCommandLineArgs(args);

		if (config.isPresent()) {
			var analyzer = new Analyzer(config.get());
			var results = analyzer.computeResults();
			System.out.println(results.toHTML());
		} 
		else 
			displayHelpAndExit();
	}

	static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
		
		var gitPath = FileSystems.getDefault().getPath(".");
		var plugins = new HashMap<String, PluginConfig>();
		
		for (var arg : args) {
			if (arg.startsWith("--")) {
				String[] parts = arg.split("=");
				if (parts.length != 2) 
					return Optional.empty();
				else {
					String pName = parts[0];
					String pValue = parts[1];
					switch (pName) {
						case "--addPlugin":
							plugins.put(pValue, new PluginConfig(){});
							break;
						case "--loadConfigFile":
							try {
								BufferedReader reader = new BufferedReader(new FileReader(pValue));
								String fileConfig = ""; 
								boolean fileNotRead = true;

								while (fileNotRead) { //Reads each argument and adds to config table
									String iterationArgument = reader.readLine();
									String iterationArgumentParts[] = iterationArgument.split("=");

									if (!(iterationArgumentParts[0] == "--loadConfigFile")) //Ignores load argument
										fileConfig += iterationArgument + " ";

									if (iterationArgument == null)
										fileNotRead = !fileNotRead;
								}

								String[] fileConfigTable = fileConfig.split(" ");
								makeConfigFromCommandLineArgs(fileConfigTable);
							}
							catch (IOException e) {
							}
							break;
						case "--justSaveConfigFile":
							try {	
								BufferedWriter writer = new BufferedWriter(new FileWriter(pValue));
								for (var cfg : args) //Iterates through options
									if (!(cfg.equals("--justSaveConfigFile=" + pValue))) //Does not write save option to file.
										writer.write(cfg + "\n");
								writer.close();
								System.exit(0); //Exits before analysis
							}
							catch (IOException ex) {
								System.out.println("Something went wrong.");
							}
							break;
						default:
							return Optional.empty();
					}
				}
			} else
					gitPath = FileSystems.getDefault().getPath(arg);
		}
		return Optional.of(new Configuration(gitPath, plugins));
	}

	//Hard coding command options seems to be the best solution, keep this list
	//updated.
	private static void displayHelpAndExit() {
		System.out.println("Command not recognized, here's a list: \n");
		System.out.println("--justSaveConfigFile=path/file.txt \n Does no analysis and saves options to path/file.txt\n");
		System.out.println("--loadConfigFile=path/file.txt \n Loads options from path/file.txt\n");
		System.out.println("--addPlugin=path/plugin \n Uses plugin");
		System.exit(0);
	}
}
