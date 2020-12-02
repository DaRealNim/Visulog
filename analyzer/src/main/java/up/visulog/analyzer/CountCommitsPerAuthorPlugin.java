package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import up.visulog.webgen.Webgen;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


public class CountCommitsPerAuthorPlugin extends Plugin {
    private Result result;

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        for (var commit : gitLog) {
            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            result.commitsPerAuthor.put(commit.author, nb + 1);
        }
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
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }

        @Override
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            for (var item : commitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }

        public Webgen.Graph[] getResultAsGraphArray() {
            ArrayList<String> labels = new ArrayList<String>();
            ArrayList<int> data = new ArrayList<Integer>();
            for (var item : commitsPerAuthor.entrySet()) {
                labels.add(item.getKey());
                data.add(Integer.valueOf(item.getValue()));
            }
            return new Webgen.Graph[]{new Webgen.BarGraph("Commits per author", labels.toArray(new String[0]), data.toArray(new int[0]), null)};
        }

        @Override
        public String getDisplayName() {
            return "Commits per author";
        }
    }
}
