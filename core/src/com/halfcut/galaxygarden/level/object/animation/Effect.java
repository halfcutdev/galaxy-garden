package com.halfcut.galaxygarden.level.object.animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.util.Util;

/**
 * @author halfcutdev
 * @since 08/09/2017
 */
public class Effect extends Entity {

    protected Animation animation;

    public Effect(Level level, float x, float y, String ref, int nframes, int delay) {

        super(level, x, y, 0, 0);
        this.animation = new Animation(Util.loadAnimation(ref, nframes), delay);
    }

    @Override
    public boolean update(float delta) {

        animation.update(delta);
        animation.getCurrentSprite().setPosition(pos.x - animation.getCurrentSprite().getWidth() / 2, pos.y - animation.getCurrentSprite().getHeight() / 2);
        return animation.hasPlayedOnce();
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {


        animation.getCurrentSprite().draw(sb);

        super.draw(sb, sr);
    }

}
