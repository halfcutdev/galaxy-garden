package com.halfcut.galaxygarden.level.object.entity.misc;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleBlackhole;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @date 12/09/17
 */
public class Blackhole extends Entity {

    static final public float WIDTH  = 18;
    static final public float HEIGHT = 18;
    static final private int PARTICLE_DELAY = 30;
    static final private int FADE_TIME = 600;

    // appearance
    private Sprite bg;
    private Sprite fg;
    private Timer particleTimer;

    // fading
    private enum Dir { IN, OUT, NONE }
    private Dir dir;
    private boolean alive;
    private Timer fadeTimer;


    public Blackhole(Level level, float x, float y) {

        super(level, x, y, WIDTH, HEIGHT);

        // sprites
        bg = new Sprite(Assets.get().getAtlas().findRegion("entity/blackhole_bg"));
        fg = new Sprite(Assets.get().getAtlas().findRegion("entity/blackhole_fg"));
        bg.setScale(0);
        fg.setScale(0);

        // particles
        particleTimer = new Timer(PARTICLE_DELAY, true);

        // fade
        dir = Dir.NONE;
        fadeTimer = new Timer(FADE_TIME, false);
    }

    @Override
    public boolean update(float delta) {
        bg.setPosition(pos.x, pos.y);
        bg.setRotation(bg.getRotation() + (3 * delta * DELTA_SCALE));
        fg.setPosition(pos.x + (bg.getWidth() - fg.getWidth()) / 2, pos.y + (bg.getHeight() - fg.getHeight()) / 2);
        fg.setRotation(fg.getRotation() - (3 * delta * DELTA_SCALE));

        if(alive) {

            if(dir == Dir.IN) {
                if(fadeTimer.tick(delta)) {
                    fadeTimer.setElapsed(fadeTimer.getDelay());
                    fadeTimer.stop();
                    dir = Dir.NONE;
                }
                bg.setScale(fadeTimer.percent());
                fg.setScale(fadeTimer.percent());
            }

            if(dir == Dir.OUT) {
                if(fadeTimer.tick(delta)) {
                    fadeTimer.setElapsed(fadeTimer.getDelay());
                    fadeTimer.stop();
                    alive = false;
                }
                bg.setScale(1 - fadeTimer.percent());
                fg.setScale(1 - fadeTimer.percent());
            }


            if(particleTimer.tick(delta)) {
                float emitboxsize = 6;
                float x = (float) (pos.x + (width  - emitboxsize) / 2 + (Math.random() * emitboxsize));
                float y = (float) (pos.y + (height - emitboxsize) / 2 + (Math.random() * emitboxsize));
                float size = (int) (Math.random() * 6);
                if(dir == Dir.IN)  size *= fadeTimer.percent();
                if(dir == Dir.OUT) size *= (1 - fadeTimer.percent());
                ParticleBlackhole particle = new ParticleBlackhole(level, x, y, size);
                level.addParticle(particle);
            }
        }

        return super.update(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        if(alive) {

                bg.draw(sb);
                fg.draw(sb);

        }
        super.draw(sb, sr);
    }

    public void fadeIn() {
        if(dir != Dir.IN) {
            alive = true;
            dir = Dir.IN;
            fadeTimer.reset();
            fadeTimer.start();
        }
    }

    public void fadeOut() {
        if(dir != Dir.OUT) {
            alive = true;
            dir = Dir.OUT;
            fadeTimer.reset();
            fadeTimer.start();
        }
    }

    public boolean canCollide() {
        return (alive) && (dir == Dir.NONE);
    }

}
