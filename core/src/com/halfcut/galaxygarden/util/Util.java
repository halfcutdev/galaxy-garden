package com.halfcut.galaxygarden.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.halfcut.galaxygarden.assets.Assets;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author halfcutdev
 * @since 07/09/2017
 */
public class Util {

    static public Vector2 unproject(float x, float y, OrthographicCamera camera) {
        Vector3 world = camera.unproject(new Vector3(x, y, 0));
        return new Vector2(world.x, world.y);
    }

    static public Vector2 project(float x, float y, OrthographicCamera camera) {
        Vector3 screen = camera.project(new Vector3(x, y, 0));
        return new Vector2(screen.x, screen.y);
    }

    /**
     * Turns a spritesheet into a 2d array of frames, representing the different animations
     *
     * @param ref          reference to the file
     * @param animLengths  array of integers corresponding to the number of frames in each animation
     * @param fwidth       frame width
     * @param fheight      frame height
     * @return           - 2d array of frames, representing the different animations
     */
    static public Sprite[][] loadSpritesheet(String ref, int[] animLengths, int fwidth, int fheight) {
        TextureRegion spritesheet = Assets.get().getAtlas().findRegion(ref);
        int nAnimations = spritesheet.getRegionHeight() / fheight;
        Sprite[][] all = new Sprite[nAnimations][];

        for(int j=0; j<animLengths.length; j++) {
            int nframes = animLengths[j];
            Sprite[] animation = new Sprite[nframes];
            for(int i=0; i<nframes; i++) {
                int x = i * fwidth;
                int y = j * fheight;
                animation[i] = new Sprite(spritesheet, x, y, fwidth, fheight);
            }
            all[j] = animation;
        }

        return all;
    }

    static public Sprite[] loadAnimation(String ref, int nFrames) {
        TextureRegion spritesheet = Assets.get().getAtlas().findRegion(ref);
        int fwidth  = spritesheet.getRegionWidth() / nFrames;
        int fheight = spritesheet.getRegionHeight();
        Sprite[] all = new Sprite[nFrames];
        for(int i=0; i<nFrames; i++) {
            all[i] = new Sprite(spritesheet, i*fwidth, 0, fwidth, fheight);
        }

        return all;
    }

    static public Sprite[] loadCharacterHeads(String ref, int fwidth, int fheight) {
        TextureRegion spritesheet = Assets.get().getAtlas().findRegion(ref);
        Sprite[] all = new Sprite[3];
        for(int i=0; i<all.length; i++) {
            all[i] = new Sprite(spritesheet, i*fwidth, 0, fwidth, fheight);
//            all[i] = new Sprite(new TextureRegion(spritesheet, i*fwidth, 0, fwidth, fheight));
        }
        return all;
    }

    static public float clamp(float val, float min, float max) {
        if(val < min) return min;
        if(val > max) return max;
        return val;
    }

    static public float round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return (float) bd.doubleValue();
    }

}
