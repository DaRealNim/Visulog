package up.visulog.analyzer;

import java.util.List;

/// Les informations à saisir de cette classe :
// elle est utilisé pour stocker les resultats obtenus apres l'éxécution du plugin et elle permet soit d'avoir 
// le resultat sous deux formats : chaines de caractères ou bien balise html.

public class AnalyzerResult {
    public List<AnalyzerPlugin.Result> getSubResults() {
        return subResults;
    }

    private final List<AnalyzerPlugin.Result> subResults;

    public AnalyzerResult(List<AnalyzerPlugin.Result> subResults) {
        this.subResults = subResults;
    }

    @Override
    public String toString() {
        return subResults.stream().map(AnalyzerPlugin.Result::getResultAsString).reduce("",
                (acc, cur) -> acc + "\n" + cur);
    }

    public String toHTML() {
        return "<html><body>"
                + subResults.stream().map(AnalyzerPlugin.Result::getResultAsHtmlDiv).reduce("", (acc, cur) -> acc + cur)
                + "</body></html>";
    }
}
