package dk.sdu.mmmi.swe.gtg.map.internal;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import dk.sdu.mmmi.swe.gtg.common.data.GameData;
import dk.sdu.mmmi.swe.gtg.common.data.entityparts.BodyPart;
import dk.sdu.mmmi.swe.gtg.common.family.Family;
import dk.sdu.mmmi.swe.gtg.common.services.entity.IProcessingSystem;
import dk.sdu.mmmi.swe.gtg.common.services.managers.IEngine;
import dk.sdu.mmmi.swe.gtg.common.services.plugin.IPlugin;
import dk.sdu.mmmi.swe.gtg.commonmap.MapSPI;
import dk.sdu.mmmi.swe.gtg.shapefactorycommon.services.ShapeFactorySPI;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MapControlSystem implements IProcessingSystem, MapSPI, IPlugin {
    private static final String MAP_WALL = "Walls";
    private static final String ATMS = "Atm";
    private static final float OBJECT_DENSITY = 1f;
    private final float unitScale = 1 / 16f;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private BodyPart collision;

    @Reference
    private ShapeFactorySPI shapeFactory;

    @Override
    public void addedToEngine(IEngine engine) {

    }

    @Override
    public List<Vector2> getAtms() {
        ArrayList<Vector2> coordinates = new ArrayList<>();
        final Array<RectangleMapObject> atms = map.getLayers().get(ATMS).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rObject : new Array.ArrayIterator<RectangleMapObject>(atms)) {
            Rectangle rectangle = rObject.getRectangle();
            coordinates.add(rectangle.getPosition(new Vector2()).scl(unitScale));
        }
        return coordinates;
    }

    @Override
    public void process(GameData gameData) {

        renderer.setView(gameData.getCamera());
        renderer.render();
    }

    @Override
    public void install(IEngine engine, GameData gameData) {
        map = new TmxMapLoader().load("maps/GTG-Map_v5.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, unitScale);

        final Array<RectangleMapObject> walls = map.getLayers().get(MAP_WALL).getObjects().getByType(RectangleMapObject.class);
        for (RectangleMapObject rObject : new Array.ArrayIterator<RectangleMapObject>(walls)) {
            Rectangle rectangle = rObject.getRectangle();
            Wall wall = new Wall();
            collision = new BodyPart(shapeFactory.createRectangle(
                    new Vector2(rectangle.getX() + rectangle.getWidth() / 2, rectangle.getY() + rectangle.getHeight() / 2).scl(unitScale), // position
                    new Vector2(rectangle.getWidth(), rectangle.getHeight()).scl(unitScale), // size
                    BodyDef.BodyType.StaticBody, OBJECT_DENSITY, false));
            collision.getBody().setUserData(wall);
            wall.addPart(collision);
            engine.addEntity(wall);
        }
    }

    @Override
    public void uninstall(IEngine engine, GameData gameData) {
        engine.getEntitiesFor(Family.builder().forEntities(Wall.class).get()).forEach(entity -> {
            engine.removeEntity(entity);
        });
    }
}
