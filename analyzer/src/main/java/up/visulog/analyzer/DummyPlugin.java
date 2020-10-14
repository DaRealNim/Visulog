package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.concurrent.TimeUnit;

public class DummyPlugin implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public DummyPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    @Override
    public void run() {
        for (int i=0; i<10; i++) {
            System.out.println("DummyPlugin_1 running...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {}
        }
        result = new DummyPlugin.Result();
    }

    @Override
    public Result getResult() {
        if (result == null) run();
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
            //     html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            // }
            // html.append("</ul></div>");
            // return html.toString();

            return "<div>Stop right there, criminal scum! You have violated the law! Pay the court a fine or serve your sentence!</div>";
        }
    }
}
