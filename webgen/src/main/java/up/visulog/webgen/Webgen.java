package up.visulog.webgen;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;

public class Webgen {

    public static Color generateRandomColor() {
        Random r = new Random();
        return new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
    }

    public static Color[] generateRandomColorArray(int size) {
        Color[] ret = new Color[size];
        for(int i=0; i<size; i++) {
            ret[i] = generateRandomColor();
        }
        return ret;
    }

    public static class Graph {
        protected String html_code;

        public String getHTML() {
            return html_code;
        }
    }

    public static final Color DEFAULT_COLOR = new Color(128,128,128);

    public static class BarGraph extends Graph {
        public BarGraph(String name, String[] dataLabels, String[] datasetsLabels, int[][] datasets, Color[][] colors) {
            if(colors == null) {
                colors = new Color[][]{new Color[dataLabels.length]};
                Arrays.fill(colors, DEFAULT_COLOR);
            }
            html_code = generateBarGraph(name, dataLabels, datasetsLabels, datasets, colors);
        }
        private String generateBarGraph(String name, String[] dataLabels, String[] datasetsLabels, int[][] datasets, Color[][] colors) {
            String returnedHTMLCode = "<canvas id='" + name.replace("'", "\\'") + "' width=\"40em\" height=\"25em\" style=\"display: block; width: 40em; height: 25em;\"></canvas>";
            returnedHTMLCode += "<script>var ctx = document.getElementById('" + name.replace("'", "\\'") + "').getContext('2d');";
            returnedHTMLCode += "var myChart = new Chart(ctx, { type: 'bar', data: { labels: [";
            for(String label : dataLabels) {
                returnedHTMLCode += "'"+label.replace("'", "\\'")+"', ";
            }
            returnedHTMLCode += "], datasets: [";
            for(int i = 0; i < datasetsLabels.length; i++) {
                returnedHTMLCode += "{ label: '" + datasetsLabels[i].replace("'", "\\'") + "',";
                returnedHTMLCode += "data: [";
                for(int height : datasets[i]) {
                    returnedHTMLCode += String.valueOf(height) + ", ";
                }
                returnedHTMLCode += "], backgroundColor: [";
                for(Color color : colors[i]) {
                    returnedHTMLCode += "'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", 0.2)', ";
                }
                returnedHTMLCode += "], borderColor: [";
                for(Color color : colors[i]) {
                    returnedHTMLCode += "'rgba(" + String.valueOf(color.getRed()) + ", " + String.valueOf(color.getGreen()) + ", " + String.valueOf(color.getBlue()) + ", 1)', ";
                }
                returnedHTMLCode += "], borderWidth : 1 },";
            }



            returnedHTMLCode += "], options: { scales: { yAxes: [{ ticks: { beginAtZero: true }}]}}}});</script>\n";
            return returnedHTMLCode;
        }
    }

    public static class LineGraph extends Graph {
        public LineGraph(String name, String[] labels, int[] data, Color color) {
            if(color == null) {
                color = DEFAULT_COLOR;
            }
            html_code = generateLineGraph(name, labels, data, color);
        }
        private String generateLineGraph(String name, String[] labels, int[] data, Color color) {
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
    }

    public static class CircularGraph extends Graph {
        public CircularGraph(String name, String[] labels, int[] data, Color[] colors, boolean isDoughnut) {
            if(colors == null) {
                colors = new Color[labels.length];
                Arrays.fill(colors, DEFAULT_COLOR);
            }
            html_code = generateCircularGraph(name, labels, data, colors, isDoughnut);
        }
        private String generateCircularGraph(String name, String[] labels, int[] data, Color[] colors, boolean isDoughnut) {
            String type = isDoughnut ? "doughnut" : "pie";
            String returnedHTMLCode = "<canvas id='" + name.replace("'", "\\'") + "' width=\"40em\" height=\"25em\" style=\"display: block; width: 40em; height: 25em;\"></canvas>";
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
    }

    public static class RadarGraph extends Graph {
        public RadarGraph(String name, String[] labels, int[] data, Color color) {
            if(color == null) {
                color = DEFAULT_COLOR;
            }
            html_code = generateRadarGraph(name, labels, data, color);
        }
        private String generateRadarGraph(String name, String[] labels, int[] data, Color color) {
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
}
