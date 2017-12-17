package com.halfcut.galaxygarden.level.object.entity.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleWater;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.util.Timer;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class Sprinkler extends Entity {

    static final public int WIDTH  = 7;
    static final public int HEIGHT = 4;
    static final public int PARTICLE_DELAY = 30;
    static final public int ACTIVE_TIME = 4000;

    final private int id;
    private boolean activated;
    private Sprite sprite;
    private Timer particleTimer;
    private Timer activeTimer;
    private boolean finished;

    static final public int HITBOX_W = 40;
    static final public int HITBOX_H = 72;
    static final public int HITBOX_X = (WIDTH - HITBOX_W) / 2;
    static final public int HITBOX_Y = -HITBOX_H;
    private Polygon waterHitbox;


    public Sprinkler(Level level, float x, float y, int id) {

        super(level, x, y, WIDTH, HEIGHT);
        this.id = id;
        sprite = new Sprite(Assets.get().getAtlas().findRegion("entity/level_sprinkler"));
        sprite.setPosition((int) x, (int) y);
        particleTimer = new Timer(PARTICLE_DELAY, true);
        activeTimer = new Timer(ACTIVE_TIME, false);
        initWaterHitbox();
    }

    protected void initWaterHitbox() {
        waterHitbox = new Polygon(new float[]{
            0, 0,
            HITBOX_W, 0,
            HITBOX_W, HITBOX_H,
            0, HITBOX_H
        });
        waterHitbox.setPosition(pos.x - (HITBOX_W - WIDTH) / 2, pos.y - HITBOX_H);
    }

    @Override
    public boolean update(float delta) {

        if(activated) {
            if(activeTimer.tick(delta)) {
                particleTimer.stop();
                finished = true;
            }
            if(particleTimer.tick(delta)) {
                for(int i=0; i<4; i++) {
                    float size = (float) (Math.random() * 2);
                    float x = pos.x + ((float) Math.random() * width) - size / 2;
                    float y = pos.y;
                    float dx = (float) (Math.random() - 0.5f) * 1f;
                    float dy = (float) (Math.random() * -0.5f) - 1;
                    ParticleWater particle = new ParticleWater(level, x, y, dx, dy, size);
                    level.addParticle(particle);
                }
            }
        }

        return super.update(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        sprite.draw(sb);
        super.draw(sb, sr);
    }

    public void activate() {

        activated = true;
        activeTimer.start();
    }

    public int getId() {

        return id;
    }

    public boolean isActivated() {

        return activated;
    }

    public Polygon getWaterHitbox() {

        return waterHitbox;
    }

    public boolean isFinished() {

        return finished;
    }

}
