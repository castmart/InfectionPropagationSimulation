package com.castmart.simulation;

import com.castmart.chart.Chart;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

public class SimulationRender implements EventHandler<ActionEvent>, ContactListener {

    SimulationEnvironment simulationEnvironment;
    Timeline timeline;
    KeyFrame frame;
    Person[] people;
    Chart chart;
    long prevTime = System.currentTimeMillis();

    public SimulationRender(SimulationEnvironment simulationEnvironment, Person[] people, Chart chart) {
        this.simulationEnvironment = simulationEnvironment;
        this.people = people;
        this.chart = chart;
        // The following register this action handler to be notified on each frame.
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Duration duration = Duration.seconds(1.0/60.0);
        frame = new KeyFrame(duration, this, null, null);
        timeline.getKeyFrames().add(frame);
        this.simulationEnvironment.setContactListener(this);
    }

    public void setPeople(Person[] people) {
        this.people = people;
    }

    public void startRender() {
        timeline.playFromStart();
    }

    public void stopRender() {
        timeline.pause();
    }

    /**
     * Allow us to update the render of the people.
     * @param event the end of a keyframe.
     */
    public void handle(ActionEvent event) {
        //Create time step. Set Iteration count 8 for velocity and 3 for positions
        this.simulationEnvironment.worldStep();
        long currentTime = System.currentTimeMillis();
        // Update person position.
        for (Person person : people) {
            if (person != null) {
                Body body = (Body) person.node.getUserData();
                float xpos = SimulationEnvironment.toPixelPosX(body.getPosition().x);
                float ypos = SimulationEnvironment.toPixelPosY(body.getPosition().y);
                person.node.setLayoutX(xpos);
                person.node.setLayoutY(ypos);
                // Check if person has recovered
                person.checkIfRecover(currentTime);
            }
        }
        // Run every second
        if (System.currentTimeMillis() - prevTime >= 1000) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    chart.update(people);
                }
            });

            prevTime = System.currentTimeMillis();
        }
    }

    public void beginContact(Contact contact) {
    }

    public void endContact(Contact contact) {
        Object objectA = contact.getFixtureA().getBody().getUserData();
        Object objectB = contact.getFixtureB().getBody().getUserData();

        if (objectA instanceof Person && objectB instanceof Person) {
            Person personA = (Person) objectA;
            Person personB = (Person) objectB;

            personA.healthEvent(personB.getPersonHealth());
            personB.healthEvent(personA.getPersonHealth());
        }
    }

    public void preSolve(Contact contact, Manifold manifold) {}

    public void postSolve(Contact contact, ContactImpulse contactImpulse) {}
}
