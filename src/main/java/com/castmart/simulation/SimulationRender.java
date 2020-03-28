package com.castmart.simulation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.contacts.Contact;

public class SimulationRender implements EventHandler<ActionEvent>, ContactListener {

    Timeline timeline;
    KeyFrame frame;
    Person[] people;

    public SimulationRender(Person[] people) {
        this.people = people;
        // The following register this action handler to be notified on each frame.
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Duration duration = Duration.seconds(1.0/60.0);
        frame = new KeyFrame(duration, this, null, null);
        timeline.getKeyFrames().add(frame);
        SimulationEnvironment.world.setContactListener(this);
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
        SimulationEnvironment.world.step(1.0f/60.f, 6, 3);
        long currentTime = System.currentTimeMillis();
        // Update person position.
        for (Person person : people) {
            Body body = (Body) person.node.getUserData();
            float xpos = SimulationEnvironment.toPixelPosX(body.getPosition().x);
            float ypos = SimulationEnvironment.toPixelPosY(body.getPosition().y);
            person.node.setLayoutX(xpos);
            person.node.setLayoutY(ypos);
            // Check if person has recovered
            person.checkIfRecover(currentTime);
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
