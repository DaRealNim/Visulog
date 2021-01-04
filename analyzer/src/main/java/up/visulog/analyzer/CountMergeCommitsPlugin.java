package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.Webgen;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.IntStream;
import java.awt.Color;

public class CountMergeCommitsPlugin extends Plugin {

    /**
     * here we count merge commits only
     * 
     */

    private Result result;

    public CountMergeCommitsPlugin(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    static Result processLog(List<Commit> gitLog) {
        // taking the total number of commits then, substracting merge commits
        var result = new Result();
        int numberOfCommits = 0;
        int numberOfMergeCommits = 0;
        for (var commit : gitLog) {
            numberOfCommits++;
            if (commit.mergedFrom != null)
                numberOfMergeCommits++;
        }
        numberOfCommits -= numberOfMergeCommits;
        result.mergeCommits.put("Regular commits", numberOfCommits);
        result.mergeCommits.put("Merge commits", numberOfMergeCommits);
        return result;
    }

    @Override
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
        if (result == null)
            run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> mergeCommits = new HashMap<>();

        @Override
        public String getResultAsString() {
            return mergeCommits.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Number of merge commits vs regular commits: <ul>");
            for (var item : mergeCommits.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }

        public Webgen.Graph[] getResultAsGraphArray() {
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<Double> data = new ArrayList<Double>();
            for (var item : mergeCommits.entrySet()) {
                labels.add(item.getKey());
                data.add(Double.valueOf(item.getValue()));
            }
            String[] labelsArray = new String[labels.size()];
            double[] dataArray = new double[data.size()];
            for (int i = 0; i < labelsArray.length; i++)
                labelsArray[i] = labels.get(i);
            for (int i = 0; i < dataArray.length; i++)
                dataArray[i] = data.get(i);
            return new Webgen.Graph[] {
                    new Webgen.CircularGraph("<i class=\"fas fa-sitemap\"></i>", "Merge vs non-merge commits",
                            labelsArray, dataArray, Webgen.generateRandomColorArray(dataArray.length), true) };
        }

        @Override
        public String getDisplayName() {
            return "Merge vs regular commits";
        }
    }
}
