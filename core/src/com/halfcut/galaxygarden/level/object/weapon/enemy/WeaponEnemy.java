package com.halfcut.galaxygarden.level.object.weapon.enemy;

import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.level.object.entity.bullet.Bullet;
import com.halfcut.galaxygarden.level.object.entity.bullet.BulletEnemy;
import com.halfcut.galaxygarden.level.object.entity.enemy.Enemy;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.enemy.GroundEnemy;
import com.halfcut.galaxygarden.level.object.weapon.Weapon;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 14/09/2017
 */
public class WeaponEnemy extends Weapon {

    static final protected int WIDTH  = 10;
    static final protected int HEIGHT = 4;
    static final protected String SPRITE_REF = "entity/weapon_pistol_enemy";

    protected GroundEnemy enemy;
    protected Vector2 target;

    public WeaponEnemy(Level level, GroundEnemy enemy) {
        super(level, enemy.pos.x, enemy.pos.y, WIDTH, HEIGHT, SPRITE_REF);
        this.enemy = enemy;
        target = enemy.getPlayer().getCenter();
        float originX = (enemy.isFacingLeft()) ? sprite.getWidth() : 0;
        sprite.setOrigin(originX, 0.5f * sprite.getHeight());
    }

    @Override
    public boolean update(float delta) {
        if(enemy.isFacingLeft()) pos.x = enemy.pos.x + (enemy.width) / 2 - 2;
        else                     pos.x = enemy.pos.x + (enemy.width) / 2 + 2;

        pos.y = enemy.pos.y + (enemy.height - sprite.getHeight()) / 2 - 2;

        if(target != null) {
            float dx = (target.x - pos.x);
            float dy = (target.y - pos.y);
            theta = (float) (Math.toDegrees(Math.atan2(dy, dx)));
        } else {
            float target = (enemy.isFacingLeft()) ? 180 : 0;
            theta += (target - theta) * 0.1f * delta * DELTA_SCALE;
        }

        sprite.setFlip(false, enemy.isFacingLeft());
        super.update(delta);
        return false;
    }

    @Override
    public void fire() {
        super.fire();

        float offset = (float) (Math.random() * RANDOM_OFFSET);
        float angle = (float) (Math.toRadians(this.theta + offset));
        float dx = (float) (Math.cos(angle));
        float dy = (float) (Math.sin(angle));
        BulletEnemy bullet = new BulletEnemy(level, muzzleRotated.x - Bullet.WIDTH / 2, muzzleRotated.y - Bullet.HEIGHT / 2, dx, dy, this.theta, Bullet.Size.NORMAL);
        level.addEnemyBullet(bullet);
    }

    // Getters & setters.

    public Vector2 getTarget() {
        return target;
    }

    public void setTarget(Vector2 target) {
        this.target = target;
    }

}
