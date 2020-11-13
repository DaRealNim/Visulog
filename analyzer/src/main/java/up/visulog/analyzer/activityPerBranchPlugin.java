package up.visulog.analyzer;

import up.visulog.config.Configuration;

import java.util.concurrent.TimeUnit;

public class activityPerBranchPlugin extends Plugin {
    private Result result;

    public activityPerBranchPlugin(Configuration generalConfiguration) {
        super(generalConfiguration);
    }

    @Override
    public void run() {
        
    }

    @Override
    public Result getResult() {
        
        return result;
    }

    static class Result implements AnalyzerPlugin.Result {
        @Override
        public String getResultAsString() {
            return "";
        }

        @Override
        public String getResultAsHtmlDiv() {
            // StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            // for (var item : commitsPerAuthor.entrySet()) {
            // html.append("<li>").append(item.getKey()).append(":
            // ").append(item.getValue()).append("</li>");
            // }
            // html.append("</ul></div>");
            // return html.toString();

            return "<div>insérer une phrase de skyrim</div>";
        }
    }
}
