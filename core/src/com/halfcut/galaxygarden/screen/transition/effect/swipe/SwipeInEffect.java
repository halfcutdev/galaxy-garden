package com.halfcut.galaxygarden.screen.transition.effect.swipe;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.screen.transition.effect.TransitionEffect;
import com.halfcut.galaxygarden.util.Timer;

/**
 * @author halfcutdev
 * @since 10/09/2017
 */
public class SwipeInEffect extends TransitionEffect {

    private Timer timer;

    public SwipeInEffect(int duration) {
        this.timer = new Timer(duration, true);
    }

    @Override
    public void render(float delta, SpriteBatch sb, ShapeRenderer sr) {
        if(timer.tick(delta)) {
            finished = true;
            timer.setElapsed(timer.getDelay());
        }

        float percent = timer.percent();

        float triangle = 180;
        float width = (App.WIDTH * App.SCALE) + (2 * triangle);
        float x = percent * width - triangle;
        float y = 0;
        float height = App.HEIGHT * App.SCALE;

        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.BLACK);
            sr.rect(x+triangle, y, width, height);
            sr.triangle(
                x, height,
                x+triangle, height,
                x+triangle, y
            );
        sr.end();
    }

}
