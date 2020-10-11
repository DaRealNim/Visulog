package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Optional;

public class CLILauncher {
    /// Les informations sur la classe :
    // Elle contient la methode main qui permet de recupérer les args passé comme
    // pararmètres et de les passer à la
    // fonction makeConfigFromCommandLineArgs. Cette derniere rend une configuration
    // selon les args donnés et le resulatat
    // sera utilisé avec l'analyzer qui va faire la compilation et l'éxécution du
    // plugin.
    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
            System.out.println(results.toHTML());
        } else
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
                            // TODO: parse argument and make an instance of PluginConfig

                            // Let's just trivially do this, before the TODO is fixed:

                            if (pValue.equals("countCommits"))
                                plugins.put("countCommits", new PluginConfig() {
                                });

                            break;
                        case "--loadConfigFile":
                            // TODO (load options from a file)
                            break;
                        case "--justSaveConfigFile":
                            // TODO (save command line options to a file instead of running the analysis)
                            break;
                        default:
                            return Optional.empty();
                    }
                }
            } else {
                gitPath = FileSystems.getDefault().getPath(arg);
            }
        }
        return Optional.of(new Configuration(gitPath, plugins));
    }

    private static void displayHelpAndExit() {
        System.out.println("Wrong command...");
        // TODO: print the list of options and their syntax
        System.exit(0);
    }
}
