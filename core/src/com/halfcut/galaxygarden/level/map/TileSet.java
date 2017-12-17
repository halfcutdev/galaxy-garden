package com.halfcut.galaxygarden.level.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.halfcut.galaxygarden.assets.Assets;

import java.util.HashMap;

/**
 * @author halfcutdev
 * @since 08/09/2017
 */
public class TileSet {

    static final private int PADDING = 1;
    static final private int MARGIN  = 1;
    static final private int BLEED   = 1;
    static final private int N_TILES_ACROSS = 8;
    static final private int N_TILES_DOWN   = 6;

    static final private String LOOKUP_TABLE_REF = "data/tile_lookup.csv";
    static final private HashMap<Integer, Integer> LOOKUP_TABLE;

    private TextureRegion[] textures;

    static {
        LOOKUP_TABLE = new HashMap<Integer, Integer>();
        FileHandle file = Gdx.files.internal(LOOKUP_TABLE_REF);
        String fileContents = file.readString();
        String[] allLines = fileContents.split("\n");
        String delim = ",";

        for(String line : allLines) {
            String[] values = line.split(delim);
            LOOKUP_TABLE.put(Integer.valueOf(values[0].trim()), Integer.valueOf(values[1].trim()));
        }
    }

    public TileSet(String ref) {

        TextureRegion allTiles = Assets.get().getAtlas().findRegion(ref);
        textures = new TextureRegion[N_TILES_ACROSS * N_TILES_DOWN];
        int idx = 0;
        for(int j=0; j<N_TILES_DOWN; j++) {
            for(int i=0; i<N_TILES_ACROSS; i++) {
                TextureRegion tile = new TextureRegion(
                    allTiles,
                    (MARGIN + BLEED + i * (TileMap.TILE_SIZE / PADDING + 1 + 2 * BLEED)),
                    (MARGIN + BLEED + j * (TileMap.TILE_SIZE / PADDING + 1 + 2 * BLEED)),
                    (TileMap.TILE_SIZE),
                    (TileMap.TILE_SIZE)
                );
                textures[idx++] = tile;
            }
        }
    }

    public TextureRegion getTextureFromBitmask(int bitmask) {

        if(LOOKUP_TABLE.keySet().contains(bitmask)) {
            return textures[LOOKUP_TABLE.get(bitmask)];
        } else {
            return textures[textures.length-1];
        }
    }

    public TextureRegion[] getTileTextures() {

        return this.textures;
    }

}
