package com.halfcut.galaxygarden.level.object.entity.interactive;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.level.object.entity.misc.Sprinkler;
import com.halfcut.galaxygarden.level.object.entity.misc.SprinklerZone;
import com.halfcut.galaxygarden.level.object.entity.plants.Plant;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.Timer;

import java.util.List;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class Valve extends EntityInteractive {

    static final public int WIDTH  = 9;
    static final public int HEIGHT = 9;
    static final private int ACTIVATION_DELAY = 3000;

    private Sprite sprite;
    private List<Sprinkler>     sprinklers;
    private List<SprinklerZone> sprinklerZones;
    private List<Plant>          trees;
    private Timer activationTimer;
    private boolean activated;
    private boolean finished;

    // sfx
    private Sound sfxWater;

    public Valve(Level level, float x, float y, List<Sprinkler> sprinklers, List<SprinklerZone> sprinklerZones, List<Plant> trees) {

        super(level, x, y, WIDTH, HEIGHT);
        sprite = new Sprite(Assets.get().getAtlas().findRegion("entity/level_valve"));
        sprite.setPosition((int) x, (int) y);
        this.sprinklers     = sprinklers;
        this.sprinklerZones = sprinklerZones;
        this.trees          = trees;
        activationTimer = new Timer(ACTIVATION_DELAY, false);

        sfxWater = Assets.get().getSFX("sfx/mp3/water.mp3");
    }

    @Override
    public boolean update(float delta) {

        if(activated) {
            if(activationTimer.tick(delta)) {
                finished = true;
                activationTimer.stop();
            }
            if(finished) {
                theta += (0 - theta) * 0.1f * delta * DELTA_SCALE;
            } else {
                theta -= 7 * delta * DELTA_SCALE;
            }
            sprite.setRotation(theta);
        }

        return super.update(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        sprite.draw(sb);
        super.draw(sb, sr);
    }

    @Override
    public void interact() {

        if(!activated) {
            activated = true;
            activationTimer.start();

            for(Sprinkler sprinkler : sprinklers) {
                sprinkler.activate();
            }
            for(SprinklerZone sprinklerZone : sprinklerZones) {
                sprinklerZone.activate();
            }
            for(Plant tree : trees) {
                tree.grow();
            }

            sfxWater.play(Settings.sfxVolume * 0.05f, (float) (0.5 + (Math.random() * 0.5f)), 0.5f);
        }

    }

    public boolean isActivated() {

        return activated;
    }

}
