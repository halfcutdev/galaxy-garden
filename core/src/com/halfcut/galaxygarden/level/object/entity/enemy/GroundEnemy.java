package com.halfcut.galaxygarden.level.object.entity.enemy;

import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.Player;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 */
public class GroundEnemy extends Enemy {

    protected Vector2 sensorLeft;
    protected Vector2 sensorRight;
    protected boolean direction; // true/false = left/right

    public GroundEnemy(Level level, float x, float y, float width, float height, int health, Player player) {
        super(level, x, y, width, height, health, player);

        sensorLeft  = new Vector2();
        sensorRight = new Vector2();
        updateSensors(0);
    }

    @Override
    public boolean update(float delta) {
        updateSensors(delta);
        return super.update(delta);
    }

    protected void updateSensors(float delta) {
        sensorLeft.x = pos.x + (vel.x * delta * DELTA_SCALE);
        sensorLeft.y = pos.y;

        sensorRight.x = pos.x + width + (vel.x * delta * DELTA_SCALE);
        sensorRight.y = pos.y;
    }

    public boolean isFacingLeft() {
        return direction;
    }

}
