package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.concurrent.TimeUnit;

public class DummyPlugin2 implements AnalyzerPlugin {
    private final Configuration configuration;
    private Result result;

    public DummyPlugin2(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    @Override
    public void run() {
        for (int i=0; i<10; i++) {
            System.out.println("DummyPlugin_2 running...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {}
        }
        result = new DummyPlugin2.Result();
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

            return "<div>Hey you, you're finally awake. You were trying to cross the border? Walked right into that imperial ambush,like us and that thief over there.</div>";
        }
    }
}
