package com.halfcut.galaxygarden.level.object.entity.misc;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.util.Timer;

/**
 * @author halfcutdev
 * @since 15/09/2017
 */
public class Fruit extends Entity {

    static final public float WIDTH  = 5;
    static final public float HEIGHT = 5;

    static final private int GROWTH_TIME = 1000;
    private enum State { GROWING, GROWN }
    private State state;
    private Sprite sprite;
    private Timer growthTimer;

    public Fruit(Level level, float x, float y, String ref) {

        super(level, x, y, WIDTH, HEIGHT);
        sprite = new Sprite(Assets.get().getAtlas().findRegion("entity/fruit_" + ref + "_xmas"));
        sprite.setPosition((int) x, (int) y);
        sprite.setRotation((float) ((Math.random() - 0.5f) * 80));
        sprite.setScale(0);

        state = State.GROWING;
        growthTimer = new Timer(GROWTH_TIME, true);
    }

    @Override
    public boolean update(float delta) {

        if(state == State.GROWING) {
            if(growthTimer.tick(delta)) {
                state = State.GROWN;
                sprite.setScale(1);
            } else {
                sprite.setScale(growthTimer.percent());
            }
        }
        return super.update(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        sprite.draw(sb);
    }

}
