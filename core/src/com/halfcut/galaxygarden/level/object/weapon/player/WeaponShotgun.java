package com.halfcut.galaxygarden.level.object.weapon.player;

import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.object.entity.bullet.Bullet;
import com.halfcut.galaxygarden.level.object.entity.bullet.BulletPlayer;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.misc.BulletCasing;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.Timer;

/**
 * @author halfcutdev
 * @since 14/09/2017
 */
public class WeaponShotgun extends WeaponPlayer {

    static final protected int WIDTH  = 13;
    static final protected int HEIGHT = 4;
    static final protected String SPRITE_REF = "entity/weapon_shotgun";
    static final public int STARTING_AMMO = 8;

    static final protected int FIRE_RATE = 500;
    static final protected int N_BULLETS = 5;
    static final protected int ANGLE_RANGE = 30;

    private Timer firingTimer;
    private boolean ready;
    private int ammo;

    public WeaponShotgun(Level level, Player player) {

        super(level, player, WIDTH, HEIGHT, SPRITE_REF);
        firingTimer = new Timer(FIRE_RATE, false);
        ready = true;
        ammo = STARTING_AMMO;
        sfxFire = Assets.get().getSFX("sfx/mp3/shotgun.mp3");
        casingType = BulletCasing.TYPE_SHELL;
    }

    @Override
    public boolean update(float delta) {

        super.update(delta);
        if(!ready && firingTimer.tick(delta)) {
            ready = true;
            firingTimer.stop();
        }
        return ammo <= 0;
    }

    @Override
    public void fire() {

        super.fire();

        ready = false;
        for(int i=0; i<N_BULLETS; i++) {
            float offset = (float) ((Math.random() - 0.5) * RANDOM_OFFSET*0.5f);
            float angle = (float) (Math.toRadians(this.theta + offset + i * (ANGLE_RANGE / N_BULLETS) - ANGLE_RANGE / 2));
            float dx = (float) (Math.cos(angle));
            float dy = (float) (Math.sin(angle));
            BulletPlayer bullet = new BulletPlayer(level, muzzleRotated.x - Bullet.WIDTH / 2, muzzleRotated.y - Bullet.HEIGHT / 2, dx, dy, (float) Math.toDegrees(angle), Bullet.Size.NORMAL);
            level.addPlayerBullet(bullet);
        }

        firingTimer.reset();
        firingTimer.start();
        ammo--;
        level.screenshake(2f, 400);
        sfxFire.play(Settings.sfxVolume * 0.5f, (float) (1 + Math.random() * 0.5f), 0.5f);
    }

    @Override
    public boolean canFire() {

        return (ready && !player.justFiredPrev());
    }

    public int getAmmo() {

        return ammo;
    }

}
