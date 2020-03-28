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

    SimulationRender render;
    Person[] population;
    int defaultPopulation = 320;

    public MainScreenController() {
    }

    public void initialize(URL location, ResourceBundle resources) {
        population = createPopulation(defaultPopulation);
        render = new SimulationRender(population);
    }

    private Person[] createPopulation(int population) {
        Person[] people = new Person[population];
        uniformPopulation(people);
        return people;
    }

    private void uniformPopulation(Person[] people) {
//        int personsLimit = 256;
        int numPersons = people.length;
        if (numPersons <= 20) {
            for (int i = 0; i < numPersons; i++) {
                people[i] = new Person(PERSON_RADIUS+ (PERSON_RADIUS * (float)Math.random() * i), PERSON_RADIUS+ (PERSON_RADIUS * (float)Math.random() * i), PERSON_RADIUS );
                simulationPane.getChildren().add(people[i].node);
            }
        } else {
            int numRows = numPersons/20;
            for (int row = 0; row < numRows; row++) {
                for (int i = 0; i < 20; i++) {
                    int index = (row * 20) + i;
                    if (index < numPersons) {
                        people[index] = new Person(8 + PERSON_RADIUS + (PERSON_RADIUS * i), 8 + PERSON_RADIUS + (PERSON_RADIUS * row), PERSON_RADIUS);
                        simulationPane.getChildren().add(people[index].node);
                    }
                }
            }
        }
    }

    @FXML
    public void onAction(ActionEvent event) {
        population[population.length / 2].healthEvent(Health.INFECTED);
        render.startRender();
    }

    @FXML
    public void onPause(ActionEvent event) {
        render.stopRender();
        destroyPopulation();
    }

    private void destroyPopulation() {
        for (Person person : population) {
            if (person != null) {
                person.distroyBody();
                simulationPane.getChildren().remove(person.node);
            }
        }
        // New population
        population = createPopulation(defaultPopulation);
        render.setPeople(population);
    }
}
