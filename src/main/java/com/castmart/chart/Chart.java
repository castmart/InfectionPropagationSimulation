package com.castmart.chart;

import com.castmart.simulation.Health;
import com.castmart.simulation.Person;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.paint.Color;

public class Chart {
    final CategoryAxis xAxis;
    final NumberAxis yAxis;
//    final AreaChart<String, Number> lineChart;
    final LineChart<String, Number> lineChart;
    final XYChart.Series<String, Number> healthy;
    final XYChart.Series<String, Number> infected;
    final XYChart.Series<String, Number> recovered;

    int dayCount = 0;

    public Chart(LineChart<String, Number> chart) {
        xAxis = new CategoryAxis(); // we are gonna plot against time
        yAxis = new NumberAxis();
        xAxis.setLabel("Days");
        xAxis.setAnimated(false); // axis animations are removed
        yAxis.setLabel("People");
        yAxis.setAnimated(false); // axis animations are removed

        //lineChart = new AreaChart<>(xAxis, yAxis);
        lineChart = chart;
        lineChart.setAnimated(false); // disable animations
        lineChart.setLayoutX(0);
        lineChart.setLayoutY(0);
        lineChart.setMaxWidth(600);
        lineChart.setMaxHeight(180);
        lineChart.applyCss();

        //defining a series to display data
        healthy = new XYChart.Series<>();
        healthy.setName("Healthy");
        lineChart.getData().add(healthy);
//        setLineColor(healthy, Color.BLUE);
//        healthy.getNode().getStyleClass().add("series-color01");

        infected = new XYChart.Series<>();
        infected.setName("Infected");
        lineChart.getData().add(infected);
//        setLineColor(healthy, Color.RED);
//        infected.getNode().getStyleClass().add("series-color02");

        recovered = new XYChart.Series<>();
        recovered.setName("Recovered");
        lineChart.getData().add(recovered);
//        setLineColor(healthy, Color.DARKGREEN);
//        recovered.getNode().getStyleClass().add("series-color03");
    }

    private void setLineColor(XYChart.Series lineChart, Color color) {
        String rgb = String.format("%d, %d, %d",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
        Node line = lineChart.getNode().lookup(".chart-series-line");
        line.setStyle("-fx-stroke: rgba(" + rgb + ", 1.0);");

//        Color white = Color.WHITE;
//        String rgbW = String.format("%d, %d, %d",
//                (int) (white.getRed() * 255),
//                (int) (white.getGreen() * 255),
//                (int) (white.getBlue() * 255));
//        Node symbol = lineChart.getNode().lookup(".chart-line-symbol");
//        symbol.setStyle("-fx-background-color: rgba(" + rgb + ", 1.0), rgba(" + rgbW + ", 1.0);");
    }

    public void update(Person[] people) {
        long healthyCount = 0;
        int infectedCount = 0;
        int recoveredCount = 0;

        for (int i = 0; i < people.length; i++) {
            if (Health.NOT_INFECTED.equals(people[i].getPersonHealth())) {
                healthyCount++;
            } else if (Health.INFECTED.equals(people[i].getPersonHealth())) {
                infectedCount++;
            } else if (Health.RECOVERED.equals(people[i].getPersonHealth())) {
                recoveredCount++;
            }
        }

        // Update the chart
        recovered.getData().add(new XYChart.Data<>("Day "+dayCount, recoveredCount));
        healthy.getData().add(new XYChart.Data<>("Day "+dayCount, healthyCount));
        infected.getData().add(new XYChart.Data<>("Day "+dayCount, infectedCount));
        dayCount++;
    }

    public void clear() {
        dayCount = 0;
        healthy.getData().clear();
        infected.getData().clear();
        recovered.getData().clear();
    }
}
