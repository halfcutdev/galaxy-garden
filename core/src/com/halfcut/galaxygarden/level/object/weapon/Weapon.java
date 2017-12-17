package com.halfcut.galaxygarden.level.object.weapon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.animation.Effect;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.level.object.entity.misc.BulletCasing;

/**
 * @author halfcutdev
 * @since 07/09/2017
 */
public class Weapon {

    static final protected float RANDOM_OFFSET = 10;

    protected Level level;
    protected float theta;
    protected Vector2 pos;
    protected Vector2 rot;
    final protected float width;
    final protected float height;
    final protected float originx;
    final protected float originy;
    protected Sprite sprite;
    protected int casingType;

    // center and rotation points
    protected Vector2 center;
    protected Vector2 muzzle;
    protected Vector2 muzzleRotated;

    public Weapon(Level level, float x, float y, float width, float height, String ref) {

        this.level = level;
        pos = new Vector2(x, y);
        rot = new Vector2();
        this.width = width;
        this.height = height;
        this.originx = width * 0.25f;
        this.originy = height * 0.5f;

        sprite = Assets.get().getAtlas().createSprite(ref);
        sprite.setOrigin(originx, originy);
        casingType = BulletCasing.TYPE_BULLET;

        center = new Vector2();
        muzzle = new Vector2();
        muzzleRotated = new Vector2();
    }

    public boolean update(float delta) {
        muzzle = new Vector2(pos.x + width, pos.y + originy);
        center = new Vector2(pos.x + originx, pos.y + originy);

        float angle = (float) (Math.toRadians(this.theta));
        float x  = (float) (Math.cos(angle) * (muzzle.x - center.x) - Math.sin(angle) * (muzzle.y - center.y) + center.x);
        float y  = (float) (Math.sin(angle) * (muzzle.x - center.x) + Math.cos(angle) * (muzzle.y - center.y) + center.y);
        muzzleRotated.x = x;
        muzzleRotated.y = y;

        sprite.setPosition(pos.x, pos.y);
        sprite.setRotation(theta);
        return false;
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        sprite.draw(sb);

        if(App.DEV_MODE) {
            sr.begin(ShapeRenderer.ShapeType.Filled);
                sr.setColor(Color.GRAY);
                sr.rect(muzzle.x, muzzle.y, 1, 1);

                sr.setColor(Color.WHITE);
                sr.rect(center.x, center.y, 1, 1);

                sr.setColor(Color.CYAN);
                sr.rect(muzzleRotated.x, muzzleRotated.y, 1, 1);
            sr.end();
        }
    }

    public void fire() {
        // Spawn a muzzle flash animation effect.
        Effect effect = new Effect(level, muzzleRotated.x, muzzleRotated.y, "entity/explosion_small", 4, 20);
        level.addEffect(effect);

        // Release a bullet casing.
        BulletCasing casing = new BulletCasing(level, center.x, center.y, !sprite.isFlipY(), casingType);
        level.addBulletCasing(casing);
    }

    public Sprite getSprite() {
        return this.sprite;
    }

}
