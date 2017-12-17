package com.halfcut.galaxygarden.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class Screenshake {

    private Random r;
    private float elapsed;
    private float duration;
    private float power;

    public Screenshake(){

        r = new Random();
    }

    public void shake(float power, float duration) {

        this.power = power;
        this.elapsed = 0;
        this.duration = duration;
    }

    public void update(float delta, OrthographicCamera camera){

        Vector2 last = ((SmoothCamera) camera).getLastPos();
        camera.position.set(last.x, last.y, 0);

        if(elapsed < duration) {
            /* Calculate the amount of shake based on how long it has been shaking already */
            elapsed += delta * 1000;
            float currentPower = power * ((duration - elapsed) / duration);
            float x = (r.nextFloat() - 0.5f) * 2 * currentPower;
            float y = (r.nextFloat() - 0.5f) * 2 * currentPower;
            camera.translate(-x, -y);
        }
    }

}
