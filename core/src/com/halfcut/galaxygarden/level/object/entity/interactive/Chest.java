package com.halfcut.galaxygarden.level.object.entity.interactive;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.util.Tween;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleItem;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.Timer;
import com.halfcut.galaxygarden.level.object.weapon.player.WeaponRifle;
import com.halfcut.galaxygarden.level.object.weapon.player.WeaponShotgun;

/**
 * @author halfcutdev
 * @since 14/09/2017
 */
public class Chest extends EntityInteractive {

    static final public float WIDTH  = 12;
    static final public float HEIGHT = 7;
    static final private int FLOAT_DURATION = 2000;
    static final private int LIFE_TIME      = 3000;

    private boolean opened;
    private Sprite open;
    private Sprite closed;
    private String type;
    private Sprite typeSprite;

    // state
    private enum State { UNOPENED, RISING, DEAD }
    private State state;
    private Timer risingTimer;
    private Timer lifeTimer;

    // particles
    static final private int PARTICLE_DELAY = 50;
    private Timer particleTimer;

    // sfx
    private Sound sfxOpen;

    public Chest(Level level, float x, float y, String type, boolean flip) {

        super(level, x, y, WIDTH, HEIGHT);

        if(type.equals("health")) open = new Sprite(Assets.get().getAtlas().findRegion("entity/chest_open_health"));
        else                      open = new Sprite(Assets.get().getAtlas().findRegion("entity/chest_open"));
        open.setPosition((int) x, (int) y);
        open.setFlip(flip, false);

        if(type.equals("health")) closed = new Sprite(Assets.get().getAtlas().findRegion("entity/chest_closed_health"));
        else                      closed = new Sprite(Assets.get().getAtlas().findRegion("entity/chest_closed"));
        closed.setPosition((int) x, (int) y);
        closed.setFlip(flip, false);

        this.type = type;
        if(type.equals("health"))  typeSprite = new Sprite(Assets.get().getAtlas().findRegion("hud/heart_full"));
        if(type.equals("rifle"))   typeSprite = new Sprite(Assets.get().getAtlas().findRegion("entity/weapon_rifle"));
        if(type.equals("shotgun")) typeSprite = new Sprite(Assets.get().getAtlas().findRegion("entity/weapon_shotgun"));

        // state
        state = State.UNOPENED;
        risingTimer = new Timer(FLOAT_DURATION, false);
        lifeTimer = new Timer(LIFE_TIME, false);

        // particles
        particleTimer = new Timer(PARTICLE_DELAY, true);

        // sfx
        sfxOpen = Assets.get().getSFX("sfx/mp3/chest1.mp3");
    }

    @Override
    public boolean update(float delta) {

        if(state == State.RISING)  updateRisingState(delta);
        return super.update(delta);
    }

    private void updateRisingState(float delta) {

        if(risingTimer.tick(delta)) {
            state = State.DEAD;
            risingTimer.stop();
            lifeTimer.start();
            return;
        }
        float percent = Tween.cubicOut(risingTimer.percent(), 0, 1,1);
        float x = pos.x + (width - typeSprite.getWidth()) / 2;
        float y = pos.y + percent * (height + 8);
        typeSprite.setPosition(x, y);

        if(particleTimer.tick(delta)) {
            emitParticle();
        }
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        if(opened) open.draw(sb);
        else       closed.draw(sb);
        if(state == State.RISING) typeSprite.draw(sb);
        super.draw(sb, sr);
    }

    @Override
    public void interact() {

        if(!opened) {
            sfxOpen.play(Settings.sfxVolume * 0.5f);
            opened = true;
            state = State.RISING;
            risingTimer.start();

            if(type.equals("health")) {
                level.getPlayer().setHealth(level.getPlayer().getHearts() * 2);
                level.getHud().showHealth();
            }
            if(type.equals("rifle")) {
                level.getPlayer().setWeapon(new WeaponRifle(level, level.getPlayer()));
            }
            if(type.equals("shotgun")) {
                level.getPlayer().setWeapon(new WeaponShotgun(level, level.getPlayer()));
            }
        }
    }

    private void emitParticle() {

        float size = (float) (3 + (Math.random() * 3));
        float x = typeSprite.getX() + (typeSprite.getWidth()) / 2;
        float y = typeSprite.getY() + (typeSprite.getHeight()) / 2;
        ParticleItem particle = new ParticleItem(level, x, y, size);
        level.addParticleBG(particle);
    }

    public boolean isOpen() {

        return this.opened;
    }

}
