package dk.sdu.mmmi.swe.gtg.enemyai.internal;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import dk.sdu.mmmi.swe.gtg.common.data.Entity;
import dk.sdu.mmmi.swe.gtg.common.data.GameData;
import dk.sdu.mmmi.swe.gtg.common.data.entityparts.BodyPart;
import dk.sdu.mmmi.swe.gtg.common.data.entityparts.PathFollowingPart;
import dk.sdu.mmmi.swe.gtg.common.data.entityparts.SeekingPart;
import dk.sdu.mmmi.swe.gtg.common.family.Family;
import dk.sdu.mmmi.swe.gtg.common.services.entity.IProcessingSystem;
import dk.sdu.mmmi.swe.gtg.common.services.managers.IEngine;
import dk.sdu.mmmi.swe.gtg.enemyai.Node;
import dk.sdu.mmmi.swe.gtg.enemyai.PathPart;
import org.osgi.service.component.annotations.Component;

import java.util.List;

@Component
public class PathFollowingSystem implements IProcessingSystem {

    private List<? extends Entity> entities;

    @Override
    public void addedToEngine(IEngine engine) {

        entities = engine.getEntitiesFor(
                Family.builder().with(
                        BodyPart.class,
                        PathPart.class,
                        PathFollowingPart.class
                ).get()
        );

    }

    @Override
    public void process(GameData gameData) {

        followPath();

    }

    private void followPath() {
        for (Entity entity : entities) {
            Vector2 target = followPath(entity);

            if (target != null) {
                SeekingPart seekingPart = entity.getPart(SeekingPart.class);

                if (seekingPart == null) {
                    seekingPart = new SeekingPart();
                    entity.addPart(seekingPart);
                }

                seekingPart.setTarget(target);
            }
        }
    }

    private Vector2 followPath(Entity entity) {
        PathFollowingPart pathFollowingPart = entity.getPart(PathFollowingPart.class);
        PathPart pathPart = entity.getPart(PathPart.class);
        Body body = entity.getPart(BodyPart.class).getBody();

        Vector2 predict = body.getLinearVelocity().cpy();
        predict.nor();
        predict.scl(pathFollowingPart.getLookAhead());

        Vector2 predictPos = body.getPosition().cpy().add(predict);

        Vector2 normal;
        Vector2 target = null;

        float record = Float.MAX_VALUE;

        List<Node> nodes = pathPart.getPath().getNodes();

        for (int i = 0; i < nodes.size() - 1; i++) {

            Vector2 a = nodes.get(i).getState();
            Vector2 b = nodes.get(i + 1).getState();

            Vector2 normalPoint = getNormalPoint(a, b, predictPos);

            Vector2 pathSegmentDir = a.cpy().sub(b);

            if (
                    normalPoint.x < Math.min(a.x, b.x) ||
                    normalPoint.x > Math.max(a.x, b.x) ||
                    normalPoint.y < Math.min(a.y, b.y) ||
                    normalPoint.y > Math.max(a.y, b.y)
            ) {
                normalPoint = b.cpy();

                /*a = nodes.get(i + 1).getState();
                b = nodes.get(i + 2).getState();
                dir = b.cpy().sub(a);*/
            }

            float d = predict.dst(normalPoint);

            if (d < record) {
                record = d;

                normal = normalPoint;

                pathSegmentDir.nor();

                pathSegmentDir.scl(5f);

                target = normal.cpy();
                target.add(pathSegmentDir);
            }
        }

        if (record > pathPart.getPath().getRadius() && target != null) {
            return target;
        }

        return null;
    }

    /**
     * This method projects the prediction vector onto the the line segment defined by the two points a and b.
     * @param a Point a.
     * @param b Point b.
     * @param p Prediction vector.
     * @return The projection vector.
     */
    private Vector2 getNormalPoint(Vector2 a, Vector2 b, Vector2 p) {
        Vector2 ap = p.cpy().sub(a);
        Vector2 ab = b.cpy().sub(a);
        ab.nor();
        // Rotates path ab to point in the direction of the predict (i guess)
        // Finds the adjacent point to the predict (hypothenuse)
        ab.scl(ap.dot(ab));
        // Adds the adjacent point to the starting point a.
        return a.cpy().add(ab);
    }

    private Vector2 separate(Entity entity, List<? extends Entity> entities) {
        Body body = entity.getPart(BodyPart.class).getBody();

        float separationDistance = 2;

        Vector2 steering = Vector2.Zero;

        int count = 0;

        for (Entity e : entities) {
            if (e == entity) {
                continue;
            }

            Body otherBody = e.getPart(BodyPart.class).getBody();

            Vector2 toOther = otherBody.getPosition().cpy().sub(body.getPosition());

            float distance = toOther.len();

            if (distance > 0 && distance < separationDistance) {
                toOther.nor();
                toOther.scl(1 / distance);
                steering.add(toOther);

                count++;
            }
        }

        if (count > 0) {
            steering.scl(1 / count);
        }

        if (steering.len() > 0) {
            steering.nor();
            steering.scl(body.getMass() * 0.5f);
        }

        return steering;
    }

}
