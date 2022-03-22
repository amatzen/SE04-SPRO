package dk.sdu.mmmi.cbse.topdowncar.game;

import com.badlogic.gdx.math.Vector2;

public class Constants {

    /**
     * Main constructor set to private to disable object creation by the user
     */
    private Constants() {
    }

    public static final Vector2 GRAVITY = new Vector2(0, 0);
    public static final float DEFAULT_ZOOM = 6f;
    public static final float PPM = 1f;

    public static final Vector2 RESOLUTION = new Vector2(1280, 720);

    public static final int VELOCITY_ITERATION = 6;
    public static final int POSITION_ITERATION = 2;

    public static final String MAP_NAME = "demo.tmx";


}
