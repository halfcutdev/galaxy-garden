package com.halfcut.galaxygarden.level.object.weapon.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.weapon.Weapon;

/**
 * @author halfcutdev
 * @since 14/09/2017
 */
public abstract class WeaponPlayer extends Weapon {

    protected Player player;
    protected Sound sfxFire;

    abstract public boolean canFire();

    public WeaponPlayer(Level level, Player player, float width, float height, String ref) {

        super(level, player.pos.x, player.pos.y, width, height, ref);
        this.player = player;
    }

    @Override
    public boolean update(float delta) {

        pos.x = player.pos.x + (player.width - 2) / 2 ;
        pos.y = player.pos.y + (player.height - 1.5f) / 4;

        Vector2 mpos = level.mouseWorldPos();
        float dx = (mpos.x - pos.x);
        float dy = (mpos.y - pos.y);
        theta = (float) (Math.toDegrees(Math.atan2(dy, dx)));

        super.update(delta);
        sprite.setFlip(false, player.isFacingLeft());

        return false;
    }

}
