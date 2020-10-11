package up.visulog.analyzer;

import java.lang.module.Configuration;

/// Les informations à savoir :
// AnalyzerPlugin est une interface qui contient une interface interne avec deux methodes à implementer dans
// chaque classe.

public interface AnalyzerPlugin {
    interface Result {
        String getResultAsString();

        String getResultAsHtmlDiv();
    }

    /**
     * run this analyzer plugin
     */
    void run();

    /**
     *
     * @return the result of this analysis. Runs the analysis first if not already
     *         done.
     */
    Result getResult();
}
