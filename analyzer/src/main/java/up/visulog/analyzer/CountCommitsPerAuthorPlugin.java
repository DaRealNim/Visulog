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
            ArrayList<Integer> data = new ArrayList<Integer>();
            for (var item : commitsPerAuthor.entrySet()) {
                labels.add(item.getKey());
                data.add(Integer.valueOf(item.getValue()));
            }
            String[] labelsArray = new String[labels.size()];
            int[] dataArray = new int[data.size()];
            for(int i=0; i<labelsArray.length; i++) labelsArray[i] = labels.get(i);
            for(int i=0; i<dataArray.length; i++) dataArray[i] = data.get(i);
            return new Webgen.Graph[]{
                new Webgen.BarGraph("<i class=\"fas fa-pen-fancy\"></i>","Commits per author - Bar", labelsArray, new String[]{"Commits per author - Bar"}, new int[][]{dataArray}, new Color[][]{Webgen.generateRandomColorArray(dataArray.length)}),
                new Webgen.BarGraph("<i class=\"fas fa-pen-fancy\"></i>","Commits per author 1 - Bar", labelsArray, new String[]{"Commits per author - Bar"}, new int[][]{dataArray}, new Color[][]{Webgen.generateRandomColorArray(dataArray.length)}),
                new Webgen.BarGraph("<i class=\"fas fa-pen-fancy\"></i>","Commits per author 2 - Bar", labelsArray, new String[]{"Commits per author - Bar"}, new int[][]{dataArray}, new Color[][]{Webgen.generateRandomColorArray(dataArray.length)})

            };
        }

        @Override
        public String getDisplayName() {
            return "Commits per author";
        }
    }
}
