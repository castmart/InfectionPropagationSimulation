package com.castmart.simulation;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

public class SimulationEnvironment {

    public static World world;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final int PERSON_RADIUS = 6;

    static {
        world = new World(new Vec2(0.0f, 0.0f));
        addGround(100, 10);
        addWall(0,100, 1, 100);
        addWall(99,100, 1, 100);
        addWall(0,100, 100, 1);
    }

    public static void addGround(float width, float height) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        BodyDef bodyDef = new BodyDef();
        bodyDef.position = new Vec2(0.0f, -4.0f);
        world.createBody(bodyDef).createFixture(fixtureDef);
    }

    public static void addWall(float posX, float posY, float width, float height) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width,height);

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 1.0f;
        fd.friction = 0.3f;

        BodyDef bd = new BodyDef();
        bd.position.set(posX, posY);

        world.createBody(bd).createFixture(fd);
    }

    /**
     * JBox2d posX to screen pixels.
     * @param posX world posX
     * @return the screen x position.
     */
    public static float toPixelPosX(float posX) {
        return WIDTH * posX / 100.0f;
    }

    /**
     * Pixel position at x to World x position.
     * @param pixelX pixel x coordinate
     * @return x position at world.
     */
    public static float toWorldPosX(float pixelX) {
        return (pixelX * 100.0f * 1.0f) / WIDTH;
    }

    /**
     * JBox2d posY to screen pixels.
     * @param posY world posY
     * @return the screen x position.
     */
    public static float toPixelPosY(float posY) {
        return HEIGHT - ((1.0f * HEIGHT) * posY / 100.0f);// + OFFSET;
    }

    /**
     * Screen pixel y to World y position.
     * @param pixelY the pixel y at screen.
     * @return posY at world.
     */
    public static float toWorldPosY(float pixelY) {
        return 100.0f - ((pixelY * 100*1.0f) /HEIGHT) ;
    }

    //Convert a JBox2D width to pixel width
    public static float toPixelWidth(float width) {
        return WIDTH*width / 100.0f;
    }

    //Convert a JBox2D height to pixel height
    public static float toPixelHeight(float height) {
        return HEIGHT*height/100.0f;
    }

}
