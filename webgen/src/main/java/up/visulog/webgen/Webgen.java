package up.visulog.webgen;

import java.awt.Color;

public class Webgen {

    public static String generateBarGraph(String name, String[] labels, int[] data, Color[] barInsideColors, Color[] barOuterColors) {
        String returnedHTMLCode = "<canvas id='" + name.replace("'", "\\'") + "'></canvas>";
        returnedHTMLCode += "<script>var ctx = document.getElementById('" + name.replace("'", "\\'") + "').getContext('2d');";
        returnedHTMLCode += "var myChart = new Chart(ctx, { type: 'bar', data: { labels: [";
        for(String label : labels) {
            returnedHTMLCode += "'"+label.replace("'", "\\'")+"', ";
        }
        returnedHTMLCode += "], datasets: [{ label: '" + name.replace("'", "\\'") + "', data: [";
        for(int height : data) {
            returnedHTMLCode += String.valueOf(height) + ", ";
        }
        returnedHTMLCode += "], backgroundColor: [";
        for(Color color : barInsideColors) {
            returnedHTMLCode += "'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", " + String.valueOf(color.getAlpha()/255.0) +")', ";
        }
        returnedHTMLCode += "], borderColor: [";
        for(Color color : barOuterColors) {
            returnedHTMLCode += "'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", " + String.valueOf(color.getAlpha()/255.0) +")', ";
        }
        returnedHTMLCode += "], borderWidth : 1 }]}, options: { scales: { yAxes: [{ ticks: { beginAtZero: true }}]}}});</script>\n";
        return returnedHTMLCode;
    }
}
