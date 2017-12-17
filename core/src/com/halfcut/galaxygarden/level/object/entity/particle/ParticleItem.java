package com.halfcut.galaxygarden.level.object.entity.particle;

import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.util.Palette;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 14/09/2017
 */
public class ParticleItem extends Particle {

    static final private int LIFE = 500;

    private Timer lifeTimer;

    public ParticleItem(Level level, float x, float y, float size) {

        super(level, x, y, size, Shape.CIRCLE);
        vel.x = (float) ((Math.random() - 0.5f) * 0.5f);
        vel.y = (float) ((Math.random() - 0.5f) * 0.5f);
        colour = (Math.random() > 0.5) ? Palette.WHITE : Palette.LIGHT;
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
