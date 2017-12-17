package com.halfcut.galaxygarden.screen.transition.effect.fade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.screen.transition.effect.TransitionEffect;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.App.HEIGHT;
import static com.halfcut.galaxygarden.App.SCALE;
import static com.halfcut.galaxygarden.App.WIDTH;

/**
 * @author halfcutdev
 * @since 10/09/2017
 */
public class FadeInEffect extends TransitionEffect {

    private Color colour = Color.BLACK;
    private Timer timer;

    public FadeInEffect(int duration) {
        this.timer = new Timer(duration, true);
    }

    @Override
    public void render(float delta, SpriteBatch sb, ShapeRenderer sr) {
        if(timer.tick(delta)) {
            finished = true;
        }

        colour.a = (finished) ? 0 : 1 - timer.getElapsed() / (float) timer.getDelay();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(colour);
            sr.rect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
        sr.end();
    }

}
