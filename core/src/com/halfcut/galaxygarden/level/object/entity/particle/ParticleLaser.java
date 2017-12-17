package com.halfcut.galaxygarden.level.object.entity.particle;

import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.util.Palette;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 16/09/2017
 */
public class ParticleLaser extends Particle {

    static final private int LIFE = 800;

    private Timer lifeTimer;

    public ParticleLaser(Level level, float x, float y, float size) {

        super(level, x, y, size, Shape.CIRCLE);
        vel.x = (float) (Math.random() - 0.5f) * 0.4f;
        vel.y = (float) (Math.random() - 0.5f) * 0.4f;
        colour = (Math.random() > 0.5) ? Palette.IIEM : Palette.PEACH;
        lifeTimer = new Timer(LIFE, true);
    }

    @Override
    public boolean update(float delta) {

        if(lifeTimer.tick(delta)) remove = true;

        width = (1 - lifeTimer.percent()) * originalSize;
        colour.a = (1 - lifeTimer.percent());

        pos.add(vel.cpy().scl(delta * DELTA_SCALE));
        return super.update(delta);
    }

}
