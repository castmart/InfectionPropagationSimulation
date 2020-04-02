package com.castmart.simulation;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import static com.castmart.simulation.SimulationEnvironment.*;

public class Person {
    // Some unit tests depend on this value.
    // If you make this larger, then tests are going to take longer.
    protected static final int RECOVER_TIME = 5000; // 5 seconds.
    // UI element for javafx.
    public Node node;
    private float posX;
    private float posY;
    private float radius;
    // Health
    private Health personHealth;
    private long infectedOn = -1;
    private long recoveredOn = -1;
    // Can recover
    private boolean canRecover = true;
    private boolean canDie = false;

    SimulationEnvironment simulationEnvironment;

    public Person(SimulationEnvironment simulationEnvironment, BodyType bodyType, float posX, float posY, float radius) {
        this.simulationEnvironment = simulationEnvironment;
        this.personHealth = Health.NOT_INFECTED;
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.node = create(bodyType);
    }

    private Node create(BodyType bodyType) {
        /////////////////////////////
        // JavaFX UI construction. //
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.setFill(personHealth.getColor());
        // Set initial position at javaFX screen.
        circle.setLayoutX(toPixelPosX(posX));
        circle.setLayoutY(toPixelPosY(posY));
        ////////////////////
        // Get world body //
        Body body = this.simulationEnvironment.createCircleShapeOnWorld(bodyType, posX, posY, PERSON_RADIUS);
        // Set the ball as user data to the world body.
        body.setUserData(this);
        // Set body as user data of the ui
        circle.setUserData(body);
        return circle;
    }

    public Health getPersonHealth() {
        return personHealth;
    }

    public void healthEvent(Health health) {
        switch (health) {
            case INFECTED:
                getInfected();
                break;
            case NOT_INFECTED:
            case RECOVERED:
            default: break;
        }
    }

    private void getInfected() {
        if (Health.NOT_INFECTED.equals(this.personHealth)) {
            // Only infect people NOT infected
            this.personHealth = Health.INFECTED;
            infectedOn = System.currentTimeMillis();
            ((Circle) node).setFill(this.personHealth.getColor());
        }
    }

    public void setCanRecover(boolean canRecover) {
        this.canRecover = canRecover;
    }

    public void setCanDie(boolean canDie) {
        this.canDie = canDie;
    }

    public void checkIfRecover(Long currentTime) {
        if (Health.INFECTED.equals(personHealth) && (currentTime - infectedOn) > RECOVER_TIME) {
            if (canRecover) {
                boolean isDead = false;
                if (canDie) {
                    // 10% probability to die
                    isDead = (int)(Math.random() * 10) == 1 ? true : false;
                }
                this.recoveredOn = System.currentTimeMillis();
                this.personHealth = Health.RECOVERED;
                ((Circle) node).setFill(this.personHealth.getColor());
            } else {
                this.personHealth = Health.DEAD;
                ((Circle) node).setFill(this.personHealth.getColor());
            }
        }
    }

    public void destroyBody() {
        simulationEnvironment.destroyBody((Body)node.getUserData());
    }
}