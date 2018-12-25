package com.company.tomek;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 *  Class drawing graph for solution using JavaFX
 */
public class Graph extends Application {

    // not good coding practices
    private static XYChart.Series series1 = new XYChart.Series();
    private static XYChart.Series series2 = new XYChart.Series();
    public static String chartTile = "";

    public static void addToSeeries1(double x, double y) {
        series1.getData().add(new XYChart.Data(x, y));
    }

    public static void addToSeeries2(double x, double y) {
        series2.getData().add(new XYChart.Data(x, y));
    }

    public static void clearSeries() {
        series1 = new XYChart.Series();
        series2 = new XYChart.Series();
    }

    public Graph() { }

    @Override
    public void start(Stage stage) {
        stage.setTitle(chartTile);
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X, Y");
        yAxis.setLabel("value");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle(chartTile);
        //defining a series
        series1.setName("X");
        series2.setName("Y");

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series1);
        lineChart.getData().add(series2);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}