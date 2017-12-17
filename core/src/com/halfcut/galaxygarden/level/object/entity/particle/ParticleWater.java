package com.halfcut.galaxygarden.level.object.entity.particle;

import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.util.Palette;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class ParticleWater extends Particle {

    private Timer timer;

    public ParticleWater(Level level, float x, float y, float dx, float dy, float size) {

        super(level, x, y, size, Shape.CIRCLE);
        timer = new Timer(1000, true);
        setColour(Palette.WHITE);
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
