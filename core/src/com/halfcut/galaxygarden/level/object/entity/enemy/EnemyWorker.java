package com.halfcut.galaxygarden.level.object.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.util.Shader;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.map.TileMap;
import com.halfcut.galaxygarden.level.map.TileCollisionModule;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 07/09/2017
 *
 * husk with no AI
 *
 */
public class EnemyWorker extends GroundEnemy {

    static final private int WIDTH  = 7;
    static final private int HEIGHT = 11;
    static final private int HEALTH = 2;
    static final private String SPRITE_REF = "entity/enemy_worker";

    static final float MOVE_SPEED         =  0.3f;
    static final float FALL_SPEED         =  0.15f;
    static final float TERMINAL_VELOCITY  = -2.8f;
    static final float TARGET_THETA = 15;

    private TileCollisionModule collision;

    private Sprite sprite;

    public EnemyWorker(Level level, float x, float y, Player player) {

        super(level, x, y, WIDTH, HEIGHT, HEALTH, player);
        collision = new TileCollisionModule(this, level.getTileMap());

        dtheta = (float) ((Math.random() - 0.5f) * 3f);
        direction = (Math.random() > 0.5) ? true : false;
        vel.x = (direction) ? -MOVE_SPEED : MOVE_SPEED;

        sprite = Assets.get().getAtlas().createSprite(SPRITE_REF);
        sprite.setOrigin(0.5f * width, 0);
    }

    @Override
    public boolean update(float delta) {

        super.update(delta);

        if(collision.collidingLeft() || collision.collidingRight()) {
            switchDirection();
        }

        TileMap tileMap = collision.getTileMap();

        // check falling to the left and right
        if(direction) {
            if(collision.onGround() && !tileMap.isBlocked(sensorLeft.x, sensorLeft.y-2))   {
                switchDirection();
            }
        }
        if(!direction) {
            if(collision.onGround() && !tileMap.isBlocked(sensorRight.x, sensorRight.y-2)) {
                switchDirection();
            }
        }

        // gravity
        vel.y -= FALL_SPEED * delta * DELTA_SCALE;
        if(vel.y < TERMINAL_VELOCITY) vel.y = TERMINAL_VELOCITY;

        collision.update(delta);

        boolean movingLeft = (vel.x < 0);
        float targetAngle = (movingLeft) ? TARGET_THETA : -TARGET_THETA;
        theta += (targetAngle - theta) * 0.05f * delta * DELTA_SCALE;

        sprite.setPosition(pos.x, pos.y);
        sprite.setFlip(movingLeft, false);
        sprite.setRotation(theta);

        return super.update(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        sb.setShader((isHit) ? Shader._WHITE : Shader._DEFAULT);
        sprite.draw(sb);
        sb.setShader(Shader._DEFAULT);
        super.draw(sb, sr);
    }

    public void switchDirection() {

        direction = !direction;
        vel.x = (direction) ? -MOVE_SPEED : MOVE_SPEED;
    }

}
