package com.halfcut.galaxygarden.level.object.entity.particle;

import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.util.Palette;
import com.halfcut.galaxygarden.util.Timer;

import java.util.Random;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class ParticleLeaf extends Particle {

    static final public int GROWTH_TIME = 3000;

    private final float maxSize;
    private boolean growing;
    private boolean grown;
    private Timer timer;

    public ParticleLeaf(Level level, float x, float y, float maxSize) {

        super(level, (int) x, (int) y, 0, Shape.CIRCLE);
        this.maxSize = maxSize;

        Random r = new Random();
        int i = r.nextInt(3);
//        if(i == 0) setColour(Palette.GLADE);
//        if(i == 1) setColour(Palette.FLORA);
//        if(i == 2) setColour(Palette.MOSS);

        if(i == 0) setColour(Palette.WHITE);
        if(i == 1) setColour(Palette.MOSS);
        if(i == 2) setColour(Palette.MOLD);

        timer = new Timer(GROWTH_TIME, false);
        grow();
    }

    @Override
    public boolean update(float delta) {

        if(growing && !grown) {
            if(timer.tick(delta)) {
                grown = true;
            } else {
                float newSize = (timer.getElapsed() / (float) timer.getDelay()) * maxSize;
                width  = newSize;
                height = newSize;
            }
        }
        return super.update(delta);
    }

    public void grow() {

        if(!growing) {
            growing = true;
            timer.start();
        }
    }

}
