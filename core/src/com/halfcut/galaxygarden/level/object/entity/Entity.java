package com.halfcut.galaxygarden.level.object.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.level.Level;

/**
 * @author halfcutdev
 * @since 06/09/2017
 */
public abstract class Entity {

    protected Level level;

    public Vector2 pos;
    public Vector2 vel;
    public Vector2 rot;
    public float theta;
    public float dtheta;

    public Polygon hitbox;
    public float width;
    public float height;
    public boolean remove;

    protected Color colour;
    protected Color devcolour;

    protected boolean submerged;

    public Entity(Level level, float x, float y, float width, float height) {

        this.level = level;
        pos = new Vector2(x, y);
        vel = new Vector2();
        rot = new Vector2();
        initHitbox(width, height);
        setColour(Color.WHITE);
        setDevColour(Color.WHITE);
    }


    // updating

    public boolean update(float delta) {

        updateHitbox();
        return remove;
    }


    // drawing

    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        if(App.DEV_MODE) {
            sr.begin();
                sr.setColor(devcolour);
                sr.rect(hitbox.getX(), hitbox.getY(), hitbox.getBoundingRectangle().width, hitbox.getBoundingRectangle().height);
            sr.end();
        }
    }

    public void setColour(Color colour) {

        this.colour = colour.cpy();
    }

    public void setDevColour(Color devcolour) {

        this.devcolour = devcolour.cpy();
    }


    // collision

    private void initHitbox(float width, float height) {
        this.width  = width;
        this.height = height;
        rot.x = width  / 2;
        rot.y = height / 2;
        hitbox = new Polygon(new float[]{
            0, 0,
            width, 0,
            width, height,
            0, height
        });
        hitbox.setOrigin(rot.x, rot.y);
        updateHitbox();
    }

    public void updateHitbox() {
        hitbox.setPosition(pos.x, pos.y);
        hitbox.setRotation(theta);
    }

    public boolean hit(Entity entity) {
        // do nothing
        return false;
    }

    public boolean hit(float wx, float wy) {
        // do nothing
        return false;
    }


    // getters & setters

    public Vector2 getCenter() {
        return new Vector2(pos.x+width/2, pos.y+height/2);
    }

}
