package com.halfcut.galaxygarden.level.object.weapon.player;

import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.object.entity.bullet.Bullet;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.bullet.BulletPlayer;
import com.halfcut.galaxygarden.storage.Settings;

/**
 * @author halfcutdev
 * @since 14/09/2017
 */
public class WeaponPistol extends WeaponPlayer {

    static final protected int WIDTH  = 10;
    static final protected int HEIGHT = 4;
    static final protected String SPRITE_REF = "entity/weapon_pistol";


    public WeaponPistol(Level level, Player player) {

        super(level, player, WIDTH, HEIGHT, SPRITE_REF);

        sfxFire = Assets.get().getSFX("sfx/mp3/pistol.mp3");
    }

    @Override
    public void fire() {

        super.fire();

        float offset = (float) ((Math.random() - 0.5) * RANDOM_OFFSET);
        float angle = (float) (Math.toRadians(this.theta + offset));
        float dx = (float) (Math.cos(angle));
        float dy = (float) (Math.sin(angle));
        BulletPlayer bullet = new BulletPlayer(level, muzzleRotated.x - Bullet.WIDTH / 2, muzzleRotated.y - Bullet.HEIGHT / 2, dx, dy, this.theta, Bullet.Size.NORMAL);
        level.addPlayerBullet(bullet);

        sfxFire.play(Settings.sfxVolume * 0.6f, (float) (0.5f + Math.random()), 0.5f);
    }

    @Override
    public boolean canFire() {

        return !player.justFiredPrev();
    }

}
