package up.visulog.analyzer;

import up.visulog.config.Configuration;

public abstract class Plugin implements AnalyzerPlugin {
    protected final Configuration configuration;

    Plugin(Configuration configuration) {
        this.configuration = configuration;
    }
}
