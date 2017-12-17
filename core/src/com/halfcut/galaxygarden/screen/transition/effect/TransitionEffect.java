package com.halfcut.galaxygarden.screen.transition.effect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author halfcutdev
 * @since 10/09/2017
 */
public abstract class TransitionEffect {

    protected boolean finished;
    public abstract void render(float delta, SpriteBatch sb, ShapeRenderer sr);

    public boolean isFinished() {
        return this.finished;
    }

}
