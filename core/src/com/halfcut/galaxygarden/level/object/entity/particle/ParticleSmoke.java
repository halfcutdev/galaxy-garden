package com.halfcut.galaxygarden.level.object.entity.particle;

import com.badlogic.gdx.graphics.Color;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class ParticleSmoke extends Particle {

    private Timer timer;
    private float originalSize;

    public ParticleSmoke(Level level, float x, float y, float size) {

        this(level, x, y, 0, 0, size);
    }

    public ParticleSmoke(Level level, float x, float y, float dx, float dy, float size) {

        super(level, x, y, size, Shape.CIRCLE);
        this.originalSize = size;
        timer = new Timer(2000, true);
        float rgb = (float) (0.1f + Math.random() * 0.2f);
        setColour(new Color(rgb, rgb, rgb, 1));
        vel.x = dx;
        vel.y = dy;
    }

    @Override
    public boolean update(float delta) {

        if(timer.tick(delta)) remove = true;
        float percent = (timer.getElapsed() / (float) (timer.getDelay()));

        colour.a = 1 - percent;
        width  = ((1-percent) * originalSize);
        height = ((1-percent) * originalSize);

        pos.x += vel.x * delta * DELTA_SCALE;
        pos.y += vel.y * delta * DELTA_SCALE;
        return remove;
    }

}
