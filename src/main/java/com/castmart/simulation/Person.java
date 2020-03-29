package com.castmart.simulation;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

import static com.castmart.simulation.SimulationEnvironment.*;

public class Person {

    private static final int RECOVER_TIME = 4500;
    // UI element for javafx.
    public Node node;
    private float posX;
    private float posY;
    private float radius;

    // Health
    private Health personHealth;
    private long infectedOn = -1;
    private long recoveredOn = -1;

    public Person(float posX, float posY, float radius) {
        this.personHealth = Health.NOT_INFECTED;
        this.posX = posX;
        this.posY = posY;
        this.radius = radius;
        this.node = create();
    }

    private Node create() {
        /////////////////////////////
        // JavaFX UI construction. //
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.setFill(personHealth.getColor());
        // Set initial position at javaFX screen.
        circle.setLayoutX(toPixelPosX(posX));
        circle.setLayoutY(toPixelPosY(posY));

        /////////////////////////////
        // Box2D Body construction //
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position.set(posX, posY);
        // Create the box2D shape.
        CircleShape circleShape = new CircleShape();
        circleShape.m_radius = radius * 0.2f;
        // Create the fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.density = 0.6f;
        fixtureDef.friction = 0.3f;
        fixtureDef.restitution = 1.0f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setLinearVelocity(
                new Vec2(( Math.random() * 10 > 5 ? 1 : -1) * (float)Math.random() * 10,
                        ( Math.random() * 10 > 5 ? 1 : -1) * (float)Math.random() * 10 )
        );
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

    public void checkIfRecover(Long currentTime) {

        if ( Health.INFECTED.equals(personHealth) && (currentTime - infectedOn) > RECOVER_TIME) {
            this.recoveredOn = System.currentTimeMillis();
            this.personHealth = Health.RECOVERED;
            ((Circle)node).setFill(this.personHealth.getColor());
        }
    }

    public void destroyBody() {
        world.destroyBody((Body)node.getUserData());
    }
}
