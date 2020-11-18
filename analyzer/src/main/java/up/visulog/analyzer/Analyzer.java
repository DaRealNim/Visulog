package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Analyzer {
    private final Configuration config;

    private AnalyzerResult result;

    public Analyzer(Configuration config) {
        this.config = config;
    }

    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>();
        for (var pluginConfigEntry : config.getPluginConfigs().entrySet()) {
            var pluginName = pluginConfigEntry.getKey();
            var pluginConfig = pluginConfigEntry.getValue();
            var plugin = makePlugin(pluginName, pluginConfig);
            plugin.ifPresent(plugins::add);
        }
        // run all the plugins
        for (var plugin : plugins) {
            PluginThread p = new PluginThread(plugin);
            new Thread(p).start();
        }

        // store the results together in an AnalyzerResult instance and return it
        return new AnalyzerResult(plugins.stream().map(AnalyzerPlugin::getResult).collect(Collectors.toList()));
    }

    private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
        try {
            Class<?> classe = Class.forName(
                    "up.visulog.analyzer." + (Character.toUpperCase(pluginName.charAt(0)) + pluginName.substring(1)));
            Constructor<?> constructor = classe.getConstructor(Configuration.class);
            Optional<Object> obj = Optional.of(constructor.newInstance(config));
            return Optional.of(AnalyzerPlugin.class.cast(obj.get()));
        } catch (Exception e) {
            System.out.println("Le nom du plugin n'existe pas.");
            return Optional.empty();
        }
    }

    private class PluginThread implements Runnable {
        AnalyzerPlugin plugin;

        public PluginThread(AnalyzerPlugin plugin) {
            this.plugin = plugin;
        }

        public void run() {
            this.plugin.run();
        }
    }

}
