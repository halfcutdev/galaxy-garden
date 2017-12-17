package com.halfcut.galaxygarden.level.object.entity.particle;

import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.util.Palette;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @date 12/09/17
 */
public class ParticleBlackhole extends Particle {

    static final private int LIFE = 800;

    private Timer lifeTimer;

    public ParticleBlackhole(Level level, float x, float y, float size) {

        super(level, x, y, size, Shape.CIRCLE);
        vel.x = (float) (Math.random() - 0.5f) * 0.75f;
        vel.y = (float) (Math.random() - 0.5f) * 0.75f;
        colour = (Math.random() > 0.5) ? Palette.PETAL : Palette.STEEL;
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
