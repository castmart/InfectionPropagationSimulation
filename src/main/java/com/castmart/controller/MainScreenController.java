package com.castmart.controller;

import com.castmart.simulation.Health;
import com.castmart.simulation.Person;
import com.castmart.simulation.SimulationRender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.ResourceBundle;

import static com.castmart.simulation.SimulationEnvironment.PERSON_RADIUS;

public class MainScreenController implements Initializable {

    @FXML
    private Pane headPane;

    @FXML
    private Pane simulationPane;

    @FXML
    private Pane footPane;

    @FXML private Button start;

    SimulationRender render;
    Person[] population;
    int defaultPopulation = 10;

    public MainScreenController() {
    }

    public void initialize(URL location, ResourceBundle resources) {
        population = createPopulation(12);
        render = new SimulationRender(population);
    }

    private Person[] createPopulation(int population) {
        if (population < 1 || population > 200) {
            population = defaultPopulation;
        }
        Person[] people = new Person[population];
        for (int i = 0; i < population; i++) {
            people[i] = new Person(10+ (PERSON_RADIUS) * i, 90, PERSON_RADIUS );
            simulationPane.getChildren().add(people[i].node);
        }
        return people;
    }

    @FXML
    public void onAction(ActionEvent event) {
        population[population.length / 2].healthEvent(Health.INFECTED);
        render.startRender();
    }
}
