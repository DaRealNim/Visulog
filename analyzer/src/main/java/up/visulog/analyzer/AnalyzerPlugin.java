package up.visulog.analyzer;
import up.visulog.webgen.Webgen;

public interface AnalyzerPlugin {
    interface Result {
        String getResultAsString();

        String getResultAsHtmlDiv();

        Webgen.Graph[] getResultAsGraphArray();

        String getDisplayName();
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
