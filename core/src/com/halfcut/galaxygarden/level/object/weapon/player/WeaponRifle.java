package com.halfcut.galaxygarden.level.object.weapon.player;

import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.object.entity.bullet.Bullet;
import com.halfcut.galaxygarden.level.object.entity.bullet.BulletPlayer;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.Timer;

/**
 * @author halfcutdev
 * @since 14/09/2017
 */
public class WeaponRifle extends WeaponPlayer {

    static final protected int WIDTH  = 13;
    static final protected int HEIGHT = 4;
    static final protected String SPRITE_REF = "entity/weapon_rifle";
    static final public int STARTING_AMMO = 32;

    static final protected int FIRE_RATE = 90;

    private Timer firingTimer;
    private boolean ready;
    private int ammo;

    public WeaponRifle(Level level, Player player) {
        super(level, player, WIDTH, HEIGHT, SPRITE_REF);
        firingTimer = new Timer(FIRE_RATE, false);
        ready = true;
        ammo = STARTING_AMMO;
        sfxFire = Assets.get().getSFX("sfx/mp3/pistol.mp3");
    }

    @Override
    public boolean update(float delta) {
        if(!ready && firingTimer.tick(delta)) {
            firingTimer.stop();
            ready = true;
        }
        super.update(delta);
        return ammo <= 0;
    }

    @Override
    public boolean canFire() {
        return ready;
    }

    @Override
    public void fire() {
        super.fire();

        ready = false;
        firingTimer.reset();
        firingTimer.start();

        float offset = (float) ((Math.random() - 0.5) * RANDOM_OFFSET);
        float angle = (float) (Math.toRadians(this.theta + offset));
        float dx = (float) (Math.cos(angle));
        float dy = (float) (Math.sin(angle));
        BulletPlayer bullet = new BulletPlayer(level, muzzleRotated.x - Bullet.WIDTH / 2, muzzleRotated.y - Bullet.HEIGHT / 2, dx, dy, this.theta, Bullet.Size.SMALL);
        level.addPlayerBullet(bullet);

        ammo--;
        level.screenshake(1.5f, 200);
        sfxFire.play(Settings.sfxVolume * 0.6f, (float) (0.4f + Math.random() * 0.05f), 0.5f);

    }

    public int getAmmo() {

        return ammo;
    }

}
