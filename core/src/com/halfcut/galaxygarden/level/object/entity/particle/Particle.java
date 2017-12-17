package com.halfcut.galaxygarden.level.object.entity.particle;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class Particle extends Entity {

    public enum Shape { RECTANGLE, CIRCLE }
    final protected float originalSize;
    protected Shape shape;
    protected TextureRegion texture;

    public Particle(Level level, float x, float y, float size, Shape shape) {

        super(level, x, y, size, size);
        this.originalSize = size;
        this.shape = shape;
        if(shape == Shape.RECTANGLE) texture = Assets.get().getAtlas().findRegion("entity/shape_rectangle");
        if(shape == Shape.CIRCLE)    texture = Assets.get().getAtlas().findRegion("entity/shape_circle");
    }

    @Override
    public boolean update(float delta) {

        theta += 0.5f * delta * DELTA_SCALE;
        return super.update(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sb.setColor(colour);
        sb.draw(texture, pos.x, pos.y, 0.5f, 0.5f, 1, 1, width, width, theta);
        sb.setColor(Color.WHITE);
    }

}
