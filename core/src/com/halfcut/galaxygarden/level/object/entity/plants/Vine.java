package com.halfcut.galaxygarden.level.object.entity.plants;

import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.misc.Fruit;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleLeaf;

/**
 * @author halfcutdev
 * @since 17/10/2017
 */
public class Vine extends Plant {

    static final public int WIDTH  = 7;
    static final public int HEIGHT = 16;
    static final private String SPRITE_REF = "entity/level_vine";
    static final private int GROWTH_REGION_X = 0;
    static final private int GROWTH_REGION_Y = 0;
    static final private int GROWTH_REGION_WIDTH = 7;
    static final private int GROWTH_REGION_HEIGHT = 16;

    public Vine(Level level, float x, float y, int id, String fruitType) {

        super(level, x, y, WIDTH, HEIGHT, id, fruitType, SPRITE_REF);
        initGrowthRegion(GROWTH_REGION_X, GROWTH_REGION_Y, GROWTH_REGION_WIDTH, GROWTH_REGION_HEIGHT);
    }

    @Override
    public void grow() {

        // big leaves
        for(int i=0; i<10; i++) {
            float maxSize = (float) (2 + (Math.random() * 2));
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
        for(int i=0; i<2; i++) {
            float x = (float) (growthRegion.x + (Math.random() * (growthRegion.width - Fruit.WIDTH / 2)));
            float y = (float) (growthRegion.y - Fruit.HEIGHT / 2 + i * (Fruit.HEIGHT * 1.5f));
            Fruit fruit = new Fruit(level, x, y, fruitType);
            level.addFruit(fruit);
        }
    }

}
