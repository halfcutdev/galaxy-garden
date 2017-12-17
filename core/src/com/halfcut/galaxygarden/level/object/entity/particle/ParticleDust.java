package com.halfcut.galaxygarden.level.object.entity.particle;

import com.badlogic.gdx.graphics.Color;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class ParticleDust extends Particle {

    private Timer timer;

    public ParticleDust(Level level, float x, float y, float size) {

        this(level, x, y, 0, 0, size);
    }

    public ParticleDust(Level level, float x, float y, float dx, float dy, float size) {

        super(level, x, y, size, Shape.CIRCLE);
        timer = new Timer(600, true);
        setColour(Color.WHITE);
        vel.x = dx;
        vel.y = dy;
    }

    @Override
    public boolean update(float delta) {

        if(timer.tick(delta)) remove = true;
        colour.a = 1 - (timer.getElapsed() / (float) (timer.getDelay()));
        pos.x += vel.x * delta * DELTA_SCALE;
        pos.y += vel.y * delta * DELTA_SCALE;
        return remove;
    }

}
