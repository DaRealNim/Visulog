package up.visulog.analyzer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.BranchCommits;
import up.visulog.webgen.Webgen;
import java.util.ArrayList;
import java.awt.Color;

public class ActivityPerBranchPlugin extends Plugin {
    /**
     * this plugin returns a list of branchCommits defined by the name and the
     * numebr of commits on each branch
     */
    private Result result;

    public ActivityPerBranchPlugin(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    @Override
    public void run() {
        result = new Result(BranchCommits.countCommitsPerBranch(configuration.getGitPath()));
    }

    @Override
    public Result getResult() {
        if (result == null)
            run();
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> nbCommitsPerBranch = new HashMap<>();

        private Result(List<BranchCommits> branchCommits) {
            for (BranchCommits branchCommit : branchCommits) {
                nbCommitsPerBranch.put(branchCommit.getNomDeLaBranche(), branchCommit.getNbCommits());
            }
        }

        public Map<String, Integer> getNbCommitsPerBranch() {
            return nbCommitsPerBranch;
        }

        @Override
        public String getResultAsString() {
            return nbCommitsPerBranch.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            // exporting data in html format
            StringBuilder html = new StringBuilder("<div>Number of Commits per branch: <ul>");
            for (var item : nbCommitsPerBranch.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }

        public Webgen.Graph[] getResultAsGraphArray() {
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<Double> data = new ArrayList<Double>();
            for (var item : nbCommitsPerBranch.entrySet()) {
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
                    new Webgen.CircularGraph("<i class=\"fas fa-code-branch\"></i>", "Number of commits per branch",
                            labelsArray, dataArray, Webgen.generateRandomColorArray(dataArray.length), false) };
        }

        @Override
        public String getDisplayName() {
            return "Activity per branch";
        }
    }
}
