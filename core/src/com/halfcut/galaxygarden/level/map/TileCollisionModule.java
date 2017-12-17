package com.halfcut.galaxygarden.level.map;

import com.halfcut.galaxygarden.level.object.entity.Entity;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 06/09/2017
 */
public class TileCollisionModule {

    static final private int DIR_UP    = 0;
    static final private int DIR_DOWN  = 1;
    static final private int DIR_LEFT  = 2;
    static final private int DIR_RIGHT = 3;

    protected Entity entity;
    protected TileMap tileMap;

    private boolean[] colliding;
    private boolean[] collidedPrev;

    public TileCollisionModule(Entity entity, TileMap tileMap) {
        this.entity = entity;
        this.tileMap = tileMap;
        colliding    = new boolean[4];
        collidedPrev = new boolean[4];
    }

    public void update(float delta) {
        if(Math.abs(entity.vel.x) > Math.abs(entity.vel.y)) {
            checkHorizontalCollision(delta);
            checkVerticalCollision(delta);
        } else {
            checkVerticalCollision(delta);
            checkHorizontalCollision(delta);
        }
    }


    // collision

    private void checkHorizontalCollision(float delta) {
        collidedPrev[DIR_LEFT]  = colliding[DIR_LEFT];
        collidedPrev[DIR_RIGHT] = colliding[DIR_RIGHT];

        colliding[DIR_LEFT]  = checkLeftCollision(delta);
        colliding[DIR_RIGHT] = checkRightCollision(delta);
        entity.pos.x += entity.vel.x * delta * DELTA_SCALE;
        entity.updateHitbox();
    }

    private void checkVerticalCollision(float delta) {
        collidedPrev[DIR_UP]   = colliding[DIR_UP];
        collidedPrev[DIR_DOWN] = colliding[DIR_DOWN];

        colliding[DIR_UP]   = checkUpCollision(delta);
        colliding[DIR_DOWN] = checkDownCollision(delta);
        entity.pos.y += entity.vel.y * delta * DELTA_SCALE;
        entity.updateHitbox();
    }

    private boolean checkLeftCollision(float delta) {
        if(entity.vel.x < 0) {
            float dx = entity.vel.x * delta * DELTA_SCALE;
            boolean blocked = false;
            boolean alreadyBlocked = false;
            boolean harmful = true;

            float x1 = entity.hitbox.getX();
            float x2 = x1 + dx;
            int tx1 = TileMap.worldToTile(x1);
            int tx2 = TileMap.worldToTile(x2);
            for(int x=tx1; x>= tx2; x--) {
                float y1 = entity.hitbox.getY();
                float y2 = y1 + entity.height-1;
                int ty1 = TileMap.worldToTile(y1);
                int ty2 = TileMap.worldToTile(y2);
                for(int y=ty1; y<=ty2; y++) {
                    if(!alreadyBlocked) {
                        blocked = tileMap.isBlocked(x, y) || tileMap.isHarmful(x, y);
                        if(blocked) {
                            alreadyBlocked = true;
                            entity.vel.x = 0;
                            entity.pos.x = (x+1) * TileMap.TILE_SIZE;
                        }
                    }
                    if(tileMap.isBlocked(x, y)) {
                        harmful = false;
                    }
                }
                if(blocked) {
                    if(harmful) entity.hit(entity.pos.x, entity.getCenter().y);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkRightCollision(float delta) {
        if(entity.vel.x > 0) {
            float dx = entity.vel.x * delta * DELTA_SCALE;
            boolean blocked = false;
            boolean alreadyBlocked = false;
            boolean harmful = true;

            float x1 = entity.hitbox.getX() + entity.width;
            float x2 = x1 + dx;
            int tx1 = TileMap.worldToTile(x1);
            int tx2 = TileMap.worldToTile(x2);
            for(int x=tx1; x<= tx2; x++) {
                float y1 = entity.hitbox.getY();
                float y2 = y1 + entity.height-1;
                int ty1 = TileMap.worldToTile(y1);
                int ty2 = TileMap.worldToTile(y2);
                for(int y=ty1; y<=ty2; y++) {
                    if(!alreadyBlocked) {
                        blocked = tileMap.isBlocked(x, y) || tileMap.isHarmful(x, y);
                        if(blocked) {
                            alreadyBlocked = true;
                            entity.vel.x = 0;
                            entity.pos.x = x * TileMap.TILE_SIZE - entity.width;
                        }
                    }
                    if(tileMap.isBlocked(x, y)) {
                        harmful = false;
                    }
                }
                if(blocked) {
                    if(harmful) entity.hit(entity.pos.x + entity.height, entity.getCenter().y);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDownCollision(float delta) {
        if(entity.vel.y < 0) {
            float dy = entity.vel.y * delta * DELTA_SCALE;
            boolean blocked = false;
            boolean alreadyBlocked = false;
            boolean harmful = true;

            float y1 = entity.hitbox.getY();
            float y2 = y1 + dy;
            int ty1 = TileMap.worldToTile(y1);
            int ty2 = TileMap.worldToTile(y2);
            for(int y=ty1; y>=ty2; y--) {
                float x1 = entity.hitbox.getX();
                float x2 = x1 + entity.width-1;
                int tx1 = TileMap.worldToTile(x1);
                int tx2 = TileMap.worldToTile(x2);
                for(int x=tx1; x<=tx2; x++) {
                    if(!alreadyBlocked) {
                        blocked = tileMap.isBlocked(x, y) || tileMap.isHarmful(x, y);
                        if(blocked) {
                            alreadyBlocked = true;
                            entity.vel.y = 0;
                            entity.pos.y = (y+1) * TileMap.TILE_SIZE;
                        }
                    }
                    if(tileMap.isBlocked(x, y)) {
                        harmful = false;
                    }
                }
                if(blocked) {
                    if(harmful) entity.hit(entity.getCenter().x, entity.pos.y);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkUpCollision(float delta) {
        if(entity.vel.y > 0) {
            float dy = entity.vel.y * delta * DELTA_SCALE;
            boolean blocked = false;
            boolean alreadyBlocked = false;
            boolean harmful = true;

            float y1 = entity.hitbox.getY() + entity.height;
            float y2 = y1 + entity.vel.y + dy;
            int ty1 = TileMap.worldToTile(y1);
            int ty2 = TileMap.worldToTile(y2);

            for(int y=ty1; y<=ty2; y++) {

                float x1 = entity.hitbox.getX();
                float x2 = x1 + entity.width-1;
                int tx1 = TileMap.worldToTile(x1);
                int tx2 = TileMap.worldToTile(x2);

                for(int x=tx1; x<=tx2; x++) {
                    if(!alreadyBlocked) {
                        blocked = tileMap.isBlocked(x, y) || tileMap.isHarmful(x, y);
                        if(blocked) {
                            alreadyBlocked = true;
                            entity.vel.y = 0;
                            entity.pos.y = y * TileMap.TILE_SIZE - entity.height;
                        }
                    }
                    if(tileMap.isBlocked(x, y)) {
                        harmful = false;
                    }
                }
                if(blocked) {
                    if(harmful) entity.hit(entity.getCenter().x, entity.pos.y + entity.height);
                    return true;
                }
            }
        }
        return false;
    }


    // getters & setters

    public boolean onGround() {
        return colliding[DIR_DOWN];
    }

    public boolean justLanded() {
        return colliding[DIR_DOWN] && !collidedPrev[DIR_DOWN];
    }

    public boolean collidingLeft() {
        return colliding[DIR_LEFT];
    }

    public boolean justCollidedLeft() {
        return colliding[DIR_LEFT] && !collidedPrev[DIR_LEFT];
    }

    public boolean collidingRight() {
        return colliding[DIR_RIGHT];
    }

    public boolean justCollidedRight() {
        return colliding[DIR_RIGHT] && !collidedPrev[DIR_RIGHT];
    }

    public TileMap getTileMap() {
        return this.tileMap;
    }

    public boolean isColliding() {
        for(boolean isSideColliding : colliding) {
            if(isSideColliding) return true;
        }
        return false;
    }

}