import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import dk.sdu.mmmi.swe.gtg.commonmap.MapSPI;
import dk.sdu.mmmi.swe.gtg.pathfinding.internal.AStarPathFinding;
import dk.sdu.mmmi.swe.gtg.pathfindingcommon.data.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class AStarPathFindingTest {

    @Test
    public void testAStar() {
        MapSPI map = new MapSPI() {
            @Override
            public List<Vector2> getATMPositions() {
                return null;
            }

            @Override
            public boolean isTileAccessibly(Vector2 position) {
                if (position.x > 8 && position.x < 10 && position.y > 4 && position.y < 16) {
                    return false;
                }
                return true;
            }

            @Override
            public Vector2 getRandomCellPosition(TiledMapTileLayer layer) {
                return null;
            }

            @Override
            public MapLayer getLayer(String layerName) {
                return null;
            }

            @Override
            public Vector2 worldCoordinatesToMapCoordinates(Vector2 worldCoordinates) {
                return worldCoordinates;
            }

            @Override
            public Vector2 tileCoordinatesToWorldCoordinates(Vector2 tileCoordinates) {
                return tileCoordinates;
            }

            @Override
            public List<Rectangle> getObstacles() {
                return null;
            }
        };

        AStarPathFinding aStar = new AStarPathFinding();

        final int resolution = 1;
        final Vector2 start = new Vector2(5, 5);
        final Vector2 end = new Vector2(15, 15);

        List<Node> res = aStar.searchNodePath(start, end, map, resolution).getNodes();

        Assert.assertTrue("Should have found a path", res.size() > 0);
        Assert.assertTrue("Should have found a path", res.get(0).getState().equals(new Vector2(5, 5)));

        System.out.println(res.stream().map(n -> n.getState().toString()).collect(Collectors.toList()));
    }

    @Test
    public void noClearPath() {
        MapSPI map = new MapSPI() {
            @Override
            public List<Vector2> getATMPositions() {
                return null;
            }

            @Override
            public boolean isTileAccessibly(Vector2 position) {
                if (position.x > 10 || position.x < -10 || position.y > 10 || position.y < -10) {
                    return false;
                }
                return true;
            }

            @Override
            public Vector2 getRandomCellPosition(TiledMapTileLayer layer) {
                return null;
            }

            @Override
            public MapLayer getLayer(String layerName) {
                return null;
            }

            @Override
            public Vector2 worldCoordinatesToMapCoordinates(Vector2 worldCoordinates) {
                return worldCoordinates;
            }

            @Override
            public Vector2 tileCoordinatesToWorldCoordinates(Vector2 tileCoordinates) {
                return tileCoordinates;
            }

            @Override
            public List<Rectangle> getObstacles() {
                return null;
            }
        };

        AStarPathFinding aStar = new AStarPathFinding();

        final int resolution = 1;
        final Vector2 start = new Vector2(5, 5);
        final Vector2 end = new Vector2(15, 15);

        List<Node> res = aStar.searchNodePath(start, end, map, resolution).getNodes();

        Assert.assertTrue("No path should be found", res.isEmpty());
    }

}
