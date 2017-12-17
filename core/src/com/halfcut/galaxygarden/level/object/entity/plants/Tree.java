package com.halfcut.galaxygarden.level.object.entity.plants;

import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.misc.Fruit;

/**
 * @author halfcutdev
 * @since 17/10/2017
 */
public class Tree extends Plant {

    static final public int WIDTH  = 16;
    static final public int HEIGHT = 20;
    static final private String SPRITE_REF = "entity/level_tree";
    static final private int GROWTH_REGION_X = 0;
    static final private int GROWTH_REGION_Y = 11;
    static final private int GROWTH_REGION_WIDTH = 16;
    static final private int GROWTH_REGION_HEIGHT = 9;

    public Tree(Level level, float x, float y, int id, String fruitType) {

        super(level, x, y, WIDTH, HEIGHT, id, fruitType, SPRITE_REF);
        initGrowthRegion(GROWTH_REGION_X, GROWTH_REGION_Y, GROWTH_REGION_WIDTH, GROWTH_REGION_HEIGHT);
    }

    @Override
    public void grow() {

        super.grow();
        // fruit
        for(int i=0; i<3; i++) {
            float x = (float) ((growthRegion.x - Fruit.WIDTH / 2) + (i * Fruit.WIDTH * 1.5f));
            float y = (float) ((growthRegion.y + (Math.random() * growthRegion.height))) - Fruit.HEIGHT / 2;
            Fruit fruit = new Fruit(level, x, y, fruitType);
            level.addFruit(fruit);
        }
    }

}
