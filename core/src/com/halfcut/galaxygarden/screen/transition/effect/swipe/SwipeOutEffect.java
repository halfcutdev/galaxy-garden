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
public class SwipeOutEffect extends TransitionEffect {

    private Timer timer;

    public SwipeOutEffect(int duration) {
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
        float x = (percent * (App.WIDTH * App.SCALE + triangle)) - triangle;
        float width = (percent * (App.WIDTH * App.SCALE + triangle));
        float height = App.HEIGHT * App.SCALE;

        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.BLACK);
            sr.rect(-triangle, 0, width, height);
            sr.triangle(
                x, 0,
                x + triangle, 0,
                x, height
            );
        sr.end();
    }

}
