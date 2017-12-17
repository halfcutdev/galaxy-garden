package com.halfcut.galaxygarden.level.object.entity.bullet;

import com.halfcut.galaxygarden.level.Level;

/**
 * @author halfcutdev
 * @since 14/09/2017
 */
public class BulletPlayer extends Bullet {

    public BulletPlayer(Level level, float x, float y, float dx, float dy, float theta, Size size) {
        super(level, x, y, dx, dy, theta, size, Team.PLAYER);
    }

}
