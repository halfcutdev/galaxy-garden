package com.halfcut.galaxygarden.level.object.entity.misc;

import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.object.entity.particle.Particle;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleFire;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleSmoke;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class Debris extends Entity {

    static final private int LIFE_DURATION = 300;
    static final private int SMOKE_DELAY   = 20;

    private Timer particleTimer;
    private Timer lifeTimer;

    public Debris(Level level, float x, float y, float dx, float dy, int life) {

        super(level, x, y, 0, 0);
        vel.x = dx;
        vel.y = dy;
        lifeTimer     = new Timer(life, true);
        particleTimer = new Timer(SMOKE_DELAY, true);
    }

    @Override
    public boolean update(float delta) {

        if(lifeTimer.tick(delta)) {
            lifeTimer.stop();
            lifeTimer.setElapsed(lifeTimer.getDelay());
            remove = true;
        }

        if(particleTimer.tick(delta)) {
            emitSmokeParticle();
        }

        vel.y -= 0.05f * delta * DELTA_SCALE;
        if(vel.y < -2) vel.y = -2;

        pos.x += vel.x * delta * DELTA_SCALE;
        pos.y += vel.y * delta * DELTA_SCALE;
        return super.update(delta);
    }

    private void emitSmokeParticle() {

        float x = pos.x;
        float y = pos.y;
        float dx = (float) (Math.random() - 0.5) * 0.2f;
        float dy = (float) (Math.random() - 0.5) * 0.2f;
        float maxSize = (1 - (lifeTimer.getElapsed() / (float) lifeTimer.getDelay())) * 6;
        float size = (float) (3 + Math.random() * maxSize);
        ParticleSmoke smoke = new ParticleSmoke(level, x, y, dx, dy, size);
        level.addParticleBG(smoke);

        if(lifeTimer.percent() < 0.6f) {
            x = getCenter().x;
            y = getCenter().y;
            Particle particle = new ParticleFire(level, x, y, (1 - (lifeTimer.getElapsed() / (float) lifeTimer.getDelay())) * 5);
            particle.vel.x = (float) (Math.random() - 0.5f) * 0.75f;
            particle.vel.y = (float) (Math.random() - 0.5f) * 0.75f;
            level.addParticle(particle);
        }

    }

}
