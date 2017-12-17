package com.halfcut.galaxygarden.level.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

/**
 * @author halfcutdev
 * @since 06/09/2017
 */
public class TileMap {

    static final public int TILE_SIZE = 8;
    static final private int N_LAYERS = 4;
    static final private int GROUND_LAYER = 0;
    static final private int BUSH_LAYER   = 1;
    static final private int THORN_LAYER  = 2;
    static final private int PIPE_LAYER   = 3;

    final public int width;
    final public int height;

    private boolean[][] blocked;
    private int[][][] tiles;
    private int[][][] masks;

    private boolean fading;
    private float[][] fade;
    private TileSet[] tilesets;

    public TileMap(TiledMap tiledMap) {

        TiledMapTileLayer groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("tiles");
        TiledMapTileLayer bushLayer   = (TiledMapTileLayer) tiledMap.getLayers().get("bushes");
        TiledMapTileLayer pipeLayer   = (TiledMapTileLayer) tiledMap.getLayers().get("pipes");
        TiledMapTileLayer thornLayer  = (TiledMapTileLayer) tiledMap.getLayers().get("thorns");

        width  = groundLayer.getWidth();
        height = groundLayer.getHeight();

        // initialise all the empty layers
        blocked = new boolean[height][width];
        fade = new float[height][width];
        for(int i=0; i<N_LAYERS; i++) {
            tiles = new int[N_LAYERS][height][width];
            masks = new int[N_LAYERS][height][width];
        }

        // initialise tiles for each layer
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {
                initTile(groundLayer, GROUND_LAYER, x, y);
                initTile(bushLayer,   BUSH_LAYER,   x, y);
                initTile(thornLayer,  THORN_LAYER,  x, y);
                initTile(pipeLayer,   PIPE_LAYER,   x, y);
                fade[y][x] = -1;
            }
        }

        // set up tile sets
        tilesets = new TileSet[6];
        tilesets[0] = new TileSet("world/tiles_purple_bleed");
        tilesets[1] = new TileSet("world/tiles_snow_2_bleed");
        tilesets[2] = new TileSet("world/tiles_pipes_bleed");
        tilesets[3] = new TileSet("world/tiles_thorns_bleed");
        tilesets[4] = new TileSet("world/tiles_bushes_bleed");
        tilesets[5] = new TileSet("world/tiles_snow_bleed");

        // update all the bitmasks
        updateTileMasks();
        updatePipeMasks();
        updateThornMasks();
        updateBushMasks();
    }

    private void initTile(TiledMapTileLayer layer, int layerIdx, int x, int y) {
        Cell cell = layer.getCell(x, y);
        tiles[layerIdx][y][x] = (cell == null) ? 0 : cell.getTile().getId();
        if(layerIdx ==GROUND_LAYER) {
            blocked[y][x] = cell != null;
        }
    }

    public void update(float delta) {
        boolean update = false;
        if(fading) {
            for(int j=0; j<height; j++) {
                for(int i=0; i<width; i++) {
                    if(fade[j][i] >= 0) {
                        fade[j][i] += delta * 0.75;
                        if(fade[j][i] >= 1) {
                            fade[j][i] = -1;
                            setTile(GROUND_LAYER, i, j, 2);
                            update = true;
                        }
                    }
                }
            }
            if(update) updateTileMasks();
        }
    }

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        for(int y=0; y<height; y++) {
            for(int x=0; x<width; x++) {

                // Draw each tile layer.
                drawTile(sb, x, y, PIPE_LAYER);
                drawTile(sb, x, y, THORN_LAYER);
                drawTile(sb, x, y, GROUND_LAYER);
                drawTile(sb, x, y, BUSH_LAYER);

                // Draw fading grass layer.
                if(fade[y][x] > -1) {
                    TextureRegion grassTexture = tilesets[tilesets.length-1].getTextureFromBitmask(masks[GROUND_LAYER][y][x]);
                    Sprite s = new Sprite(grassTexture);
                    s.setPosition(x * TILE_SIZE, y * TILE_SIZE);
                    s.setColor(new Color(1, 1, 1, fade[y][x]));
                    s.draw(sb);
                }

            }
        }
    }

    private void drawTile(SpriteBatch sb, int x, int y, int layerIdx) {
        if(tiles[layerIdx][y][x] > 0) {
            TextureRegion tex = tilesets[tiles[layerIdx][y][x]-1].getTextureFromBitmask(masks[layerIdx][y][x]);
            sb.draw(tex, x * TILE_SIZE, y * TILE_SIZE);
        }
    }


    // getters & setters

    public boolean isBlocked(int x, int y) {
        if(!isValid(x, y)) return true;
        return blocked[y][x];
    }

    public boolean isBlocked(float wx, float wy) {
        int tx = worldToTile(wx);
        int ty = worldToTile(wy);
        return isBlocked(tx, ty);
    }

    public boolean isHarmful(int x, int y) {
        return tiles[THORN_LAYER][y][x] > 0;
    }

    public boolean isHarmful(float wx, float wy) {
        int tx = worldToTile(wx);
        int ty = worldToTile(wy);
        return isHarmful(tx, ty);
    }

    public boolean isBush(int x, int y) {
        return tiles[BUSH_LAYER][y][x] > 0;
    }

    public boolean isBush(float wx, float wy) {
        int tx = worldToTile(wx);
        int ty = worldToTile(wy);
        return isBush(tx, ty);
    }

    public boolean isValid(int x, int y) {
        return !(x < 0 || x >= width || y < 0 || y >= height);
    }

    public void setTile(int layerIdx, float wx, float wy, int value) {
        int tx = worldToTile(wx);
        int ty = worldToTile(wy);
        setTile(layerIdx, tx, ty, value);
    }

    public void setTile(int layerIdx, int x, int y, int value) {
        tiles[layerIdx][y][x] = value;
    }

    public void setBlocked(int x, int y, boolean blocked) {
        this.blocked[y][x] = blocked;
    }

    public void setBlocked(float wx, float wy, boolean blocked) {
        int tx = worldToTile(wx);
        int ty = worldToTile(wy);
        setBlocked(tx, ty, blocked);
    }

    public void setFade(float wx, float wy) {
        int tx = worldToTile(wx);
        int ty = worldToTile(wy);
        setFade(tx, ty);
    }

    public void setFade(int x, int y) {
        fading = true;
        fade[y][x] = 0;
    }


    // masks

    private void updateMasks(int layerIdx) {
        for(int j=0; j<height; j++) {
            for(int i=0; i<width; i++) {

                boolean blocked = tiles[layerIdx][j][i] > 0;
                if(blocked) {
                    int sum = 0;
                    /* Top row */
                    if(j<height-1) {
                        if(i>0) sum += ((tiles[layerIdx][j+1][i-1] == 0) ? 1 : 0 ) * 8;       // NW
                        else sum += 8;

                        sum += ((tiles[layerIdx][j+1][i] == 0) ? 1 : 0 ) * 16;                // N

                        if(i<width-1) sum += ((tiles[layerIdx][j+1][i+1] == 0) ? 1 : 0 ) * 1; // NE
                        else sum += 1;
                    } else {
                        sum += 25;
                    }

                    /* Middle row */
                    if(i>0) sum += ((tiles[layerIdx][j][i-1] == 0) ? 1 : 0 ) * 128;           // W
                    else sum += 128;

                    if(i<width-1) sum += ((tiles[layerIdx][j][i+1] == 0) ? 1 : 0 ) * 32;      // E
                    else sum += 32;

                    /* Bottom row */
                    if(j>0) {
                        if(i>0) sum += ((tiles[layerIdx][j-1][i-1] == 0) ? 1 : 0 ) * 4;       // SW
                        else sum += 4;

                        sum += ((tiles[layerIdx][j-1][i] == 0) ? 1 : 0 ) * 64;                //  S

                        if(i<width-1) sum += ((tiles[layerIdx][j-1][i+1] == 0) ? 1 : 0 ) * 2; // SE
                        else sum += 2;
                    } else {
                        sum += 70;
                    }
                    masks[layerIdx][j][i] = sum;
                }
            }
        }
    }

    public void updateTileMasks() {
        updateMasks(GROUND_LAYER);
    }

    private void updatePipeMasks() {
        updateMasks(PIPE_LAYER);
    }

    private void updateThornMasks() {
        updateMasks(THORN_LAYER);
    }

    private void updateBushMasks() {
        updateMasks(BUSH_LAYER);
    }


    // helper

    public void print() {
        for(int j=height-1; j>0; j--) {
            for(int i=0; i<width; i++) {
                System.out.println(tiles[j][i] + " ");
            }
            System.out.println();
        }
    }

    static public int worldToTile(float w) {
        return (int) (Math.floor(w / TILE_SIZE));
    }

}
