package com.halfcut.galaxygarden.level.object.animation;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.halfcut.galaxygarden.util.Timer;

/**
 * @author halfcutdev
 * @since 08/09/2017
 */
public class Animation {

    private Sprite[] frames;
    private int currentFrame;
    private boolean playedOnce;
    public Timer timer;

    public Animation(Sprite[] frames, int delay) {

        this.frames = frames;
        this.timer  = new Timer(delay, true);
    }

    public void setFrames(Sprite[] frames) {

        this.frames = frames;
        currentFrame = 0;
        playedOnce = false;
        timer.reset();
        timer.start();
    }

    public void update(float delta) {

        // Check if it's time to change frame
        if(timer.tick(delta)) {
            this.currentFrame++;
        }
        // If at the end, loop back to the first frame.
        if(currentFrame >= frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public void setDelay(int delay) {

        timer.setDelay(delay);
    }

    public void setFrame(int i) {

        this.currentFrame = i;
    }

    public int getCurrentFrame() {

        return this.currentFrame;
    }

    public int getNFrames() {

        return this.frames.length;
    }

    public Sprite getCurrentSprite() {

        return frames[currentFrame];
    }

    public boolean hasPlayedOnce() {

        return this.playedOnce;
    }

}