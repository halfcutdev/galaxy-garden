package com.halfcut.galaxygarden.level.object.entity.bullet;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.animation.Effect;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.map.TileCollisionModule;
import com.halfcut.galaxygarden.assets.Assets;

/**
 * @author halfcutdev
 * @since 07/09/2017
 */
public class Bullet extends Entity {

    static final public int WIDTH  = 4;
    static final public int HEIGHT = 4;
    static final protected float SPEED = 3;

    static final private String SPRITE_REF_PLAYER       = "entity/bullet_player";
    static final private String SPRITE_REF_PLAYER_SMALL = "entity/bullet_player_small";
    static final private String SPRITE_REF_ENEMY        = "entity/bullet_enemy";
    static final private String SPRITE_REF_ENEMY_SMALL  = "entity/bullet_enemy_small";

    public enum Size { NORMAL, SMALL }
    public enum Team { PLAYER, ENEMY }

    protected TileCollisionModule collision;
    protected Sprite sprite;
    protected Size size;
    protected Team team;

    public Bullet(Level level, float x, float y, float dx, float dy, float theta, Size size, Team team) {

        super(level, x, y, WIDTH, HEIGHT);
        vel.set(dx * SPEED, dy * SPEED);
        this.theta = theta;
        collision = new TileCollisionModule(this, level.getTileMap());

        // Set the bullet sprite based on the bullet's team and size.
        this.size = size;
        this.team = team;
        if(team == Team.PLAYER) {
            if(size == Size.NORMAL) sprite = new Sprite(Assets.get().getAtlas().findRegion(SPRITE_REF_PLAYER));
            if(size == Size.SMALL)  sprite = new Sprite(Assets.get().getAtlas().findRegion(SPRITE_REF_PLAYER_SMALL));
        }
        if(team == Team.ENEMY) {
            if(size == Size.NORMAL) sprite = new Sprite(Assets.get().getAtlas().findRegion(SPRITE_REF_ENEMY));
            if(size == Size.SMALL)  sprite = new Sprite(Assets.get().getAtlas().findRegion(SPRITE_REF_ENEMY_SMALL));
        }
    }

    @Override
    public boolean update(float delta) {
        collision.update(delta);
        super.update(delta);

        sprite.setPosition(pos.x, pos.y);
        sprite.setRotation(theta);

        if(collision.isColliding()) hit(null);
        return remove;
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        sprite.draw(sb);
        super.draw(sb, sr);
    }

    @Override
    public boolean hit(Entity entity) {
        Effect effect = new Effect(level, pos.x + width / 2, pos.y + height / 2, "entity/explosion_small", 4, 10);
        level.addEffect(effect);
        remove = true;
        return true;
    }

}
