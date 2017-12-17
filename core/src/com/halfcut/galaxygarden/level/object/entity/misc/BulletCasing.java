package com.halfcut.galaxygarden.level.object.entity.misc;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.map.TileCollisionModule;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.util.Timer;

/**
 * @author halfcutdev
 * @since 30/10/2017
 */
public class BulletCasing extends Entity {

    static final protected int WIDTH  = 2;
    static final protected int HEIGHT = 1;
    static final public int TYPE_BULLET = 0;
    static final public int TYPE_SHELL  = 1;
    static final private String REF_BULLET = "entity/casing_bullet";
    static final private String REF_SHELL  = "entity/casing_shell";
    static final protected int LIFE_TIME = 1500;

    private Sprite sprite;
    private TileCollisionModule collision;
    private Timer lifeTimer;

    public BulletCasing(Level level, float x, float y, boolean direction, int type) {

        super(level, x, y, WIDTH, HEIGHT);
        if(type == TYPE_BULLET) sprite = Assets.get().getAtlas().createSprite(REF_BULLET);
        if(type == TYPE_SHELL)  sprite = Assets.get().getAtlas().createSprite(REF_SHELL);

        vel.x = (float) ((0.4f + Math.random() * 0.2f) * ((direction) ? -1 : 1));
        vel.y = (float) (0.5f + Math.random() * 0.3f);
        dtheta = (float) (Math.random() * 20 * ((direction) ? 1 : -1));

        collision = new TileCollisionModule(this, level.getTileMap());
        lifeTimer = new Timer(LIFE_TIME, true);
    }

    @Override
    public boolean update(float delta) {

        vel.x += (0 - vel.x) * 0.03f;
        vel.y -= 0.04f;
        if(vel.y < -1) vel.y = -1;

        dtheta += (0 - dtheta) * 0.01f;
        theta += dtheta;

        collision.update(delta);
        sprite.setPosition(pos.x, pos.y);
        sprite.setRotation(theta);
        super.update(delta);
        return lifeTimer.tick(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        float alpha = (1 - (lifeTimer.percent()));
        sprite.setColor(1, 1, 1, alpha);
        sprite.draw(sb);
        super.draw(sb, sr);
    }

}
