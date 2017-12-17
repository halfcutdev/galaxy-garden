package com.halfcut.galaxygarden.level.object.entity.misc;

import com.badlogic.gdx.math.Circle;
import com.halfcut.galaxygarden.level.map.TileMap;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleLeaf;

/**
 * @author halfcutdev
 * @since 10/09/2017
 */
public class SprinklerZone extends Entity {

    final private int id;
    private TileMap tileMap;

    public SprinklerZone(Level level, float x, float y, float width, float height, int id) {

        super(level, x, y, width, height);
        this.id = id;
        this.tileMap = level.getTileMap();
    }

    public void activate() {

        Circle circle = new Circle(pos.x+width/2, pos.y+width/2, width/2);
        for(float j=pos.y; j<pos.y+height; j+= TileMap.TILE_SIZE) {
            for(float i=pos.x; i<pos.x+width; i+= TileMap.TILE_SIZE) {
                if(circle.contains(i, j)) {
                    if(tileMap.isBlocked(i, j)) {
                        tileMap.setFade(i, j);
                    }
                    if(tileMap.isBush(i, j)) {

                        // block out the tile
                        tileMap.setBlocked(i, j, true);

                        // grow leaves
                        float x1 = TileMap.worldToTile(i) * TileMap.TILE_SIZE;
                        float y1 = TileMap.worldToTile(j) * TileMap.TILE_SIZE;
                        float x2 = x1 + TileMap.TILE_SIZE;
                        float y2 = y1 + TileMap.TILE_SIZE;

                        // big leaves
                        for(int k=0; k<10; k++) {
                            float maxSize = (float) (3 + (Math.random() * 2));
                            float x = x1 + (float) (Math.random() * TileMap.TILE_SIZE);
                            float y = y1 + (float) (Math.random() * TileMap.TILE_SIZE);
                            ParticleLeaf leaf = new ParticleLeaf(level, x, y, maxSize);
                            level.addParticleBG(leaf);
                        }
                        // small leaves
                        for(int k=0; k<10; k++) {
                            float maxSize = (float) (1 + (Math.random() * 2));
                            float x = x1 + (float) (Math.random() * TileMap.TILE_SIZE);
                            float y = y1 + (float) (Math.random() * TileMap.TILE_SIZE);
                            ParticleLeaf leaf = new ParticleLeaf(level, x, y, maxSize);
                            level.addParticleBG(leaf);
                        }
                    }
                }
            }
        }
        tileMap.updateTileMasks();
    }

    public int getId() {

        return id;
    }

}
