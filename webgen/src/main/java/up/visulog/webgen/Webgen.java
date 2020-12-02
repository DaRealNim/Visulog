// package up.visulog.webgen;

import java.awt.Color;

public class Webgen {

    public static String generateBarGraph(String name, String[] labels, int[] data, Color[] colors) {
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
        for(Color color : colors) {
            returnedHTMLCode += "'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", 0.2)', ";
        }
        returnedHTMLCode += "], borderColor: [";
        for(Color color : colors) {
            returnedHTMLCode += "'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", 1)', ";
        }
        returnedHTMLCode += "], borderWidth : 1 }]}, options: { scales: { yAxes: [{ ticks: { beginAtZero: true }}]}}});</script>\n";
        return returnedHTMLCode;
    }

    public static String generateLineGraph(String name, String[] labels, int[] data, Color color) {
        String returnedHTMLCode = "<canvas id='" + name.replace("'", "\\'") + "'></canvas>";
        returnedHTMLCode += "<script>var ctx = document.getElementById('" + name.replace("'", "\\'") + "').getContext('2d');";
        returnedHTMLCode += "var myChart = new Chart(ctx, { type: 'line', data: { labels: [";
        for(String label : labels) {
            returnedHTMLCode += "'"+label.replace("'", "\\'")+"', ";
        }
        returnedHTMLCode += "], datasets: [{ label: '" + name.replace("'", "\\'") + "', data: [";
        for(int height : data) {
            returnedHTMLCode += String.valueOf(height) + ", ";
        }
        returnedHTMLCode += "], backgroundColor: 'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", 0.2)'";
        returnedHTMLCode += ", borderWidth : 1 }]}, options: { scales: { yAxes: [{ ticks: { beginAtZero: true }}]}}});</script>\n";
        return returnedHTMLCode;
    }

    public static String generateCircularGraph(String name, String[] labels, int[] data, Color[] colors, boolean isDoughnut) {
        String type = isDoughnut ? "doughnut" : "pie";
        String returnedHTMLCode = "<canvas id='" + name.replace("'", "\\'") + "'></canvas>";
        returnedHTMLCode += "<script>var ctx = document.getElementById('" + name.replace("'", "\\'") + "').getContext('2d');";
        returnedHTMLCode += "var myChart = new Chart(ctx, { type: '"+type+"', data: { labels: [";
        for(String label : labels) {
            returnedHTMLCode += "'"+label.replace("'", "\\'")+"', ";
        }
        returnedHTMLCode += "], datasets: [{ label: '" + name.replace("'", "\\'") + "', data: [";
        for(int height : data) {
            returnedHTMLCode += String.valueOf(height) + ", ";
        }
        returnedHTMLCode += "], backgroundColor: [";
        for(Color color : colors) {
            returnedHTMLCode += "'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", 0.2)', ";
        }
        returnedHTMLCode += "], borderColor: [";
        for(Color color : colors) {
            returnedHTMLCode += "'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", 1)', ";
        }
        returnedHTMLCode += "], borderWidth : 1 }]}, options: {}});</script>\n";
        return returnedHTMLCode;
    }

    public static String generateRadarGraph(String name, String[] labels, int[] data, Color color) {
        String returnedHTMLCode = "<canvas id='" + name.replace("'", "\\'") + "'></canvas>";
        returnedHTMLCode += "<script>var ctx = document.getElementById('" + name.replace("'", "\\'") + "').getContext('2d');";
        returnedHTMLCode += "var myChart = new Chart(ctx, { type: 'radar', data: { labels: [";
        for(String label : labels) {
            returnedHTMLCode += "'"+label.replace("'", "\\'")+"', ";
        }
        returnedHTMLCode += "], datasets: [{ label: '" + name.replace("'", "\\'") + "', data: [";
        for(int height : data) {
            returnedHTMLCode += String.valueOf(height) + ", ";
        }
        returnedHTMLCode += "], backgroundColor: 'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", 0.2)'";
        returnedHTMLCode += ", borderWidth : 1 }]}, options: {scale: { angleLines: { display: false }, ticks: { suggestedMin: 0, suggestedMax: 100 } } }});</script>\n";
        return returnedHTMLCode;
    }
}
