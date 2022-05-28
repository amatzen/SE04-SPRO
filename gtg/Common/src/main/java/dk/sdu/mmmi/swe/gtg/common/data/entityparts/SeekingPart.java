package dk.sdu.mmmi.swe.gtg.common.data.entityparts;

import com.badlogic.gdx.math.Vector2;

public class SeekingPart implements IEntityPart {

    private Vector2 target;

    public Vector2 getTarget() {
        return target;
    }

    public void setTarget(Vector2 target) {
        this.target = target;
    }

    @Override
    public void destroy() {

    }
}
