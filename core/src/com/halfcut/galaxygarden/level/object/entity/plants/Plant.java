package com.halfcut.galaxygarden.level.object.entity.plants;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.object.entity.misc.Fruit;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleLeaf;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class Plant extends Entity {

    final private int id;
    protected Sprite sprite;
    protected String fruitType;
    protected Rectangle growthRegion;

    public Plant(Level level, float x, float y, float width, float height, int id, String fruitType, String spriteRef) {

        super(level, x, y, width, height);
        this.id = id;
        sprite = new Sprite(Assets.get().getAtlas().findRegion(spriteRef));
        sprite.setPosition((int) x, (int) y);
        sprite.setFlip((Math.random() > 0.5), false);
        this.fruitType = fruitType;
    }

    protected void initGrowthRegion(int x, int y, int width, int height) {

        growthRegion = new Rectangle(pos.x + x, pos.y + y, width, height);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        sprite.draw(sb);
        super.draw(sb, sr);
    }

    public void grow() {

        // big leaves
        for(int i=0; i<10; i++) {
            float maxSize = (float) (3 + (Math.random() * 4));
            float x = growthRegion.x + (float) (Math.random() * growthRegion.width);
            float y = growthRegion.y + (float) (Math.random() * growthRegion.height);
            ParticleLeaf leaf = new ParticleLeaf(level, x, y, maxSize);
            level.addParticleBG(leaf);
        }
        // small leaves
        for(int i=0; i<15; i++) {
            float maxSize = (float) (1 + (Math.random() * 2));
            float x = growthRegion.x + (float) (Math.random() * growthRegion.width);
            float y = growthRegion.y + (float) (Math.random() * growthRegion.height);
            ParticleLeaf leaf = new ParticleLeaf(level, x, y, maxSize);
            level.addParticleBG(leaf);
        }
    }

    public int getId() {

        return id;
    }

}
