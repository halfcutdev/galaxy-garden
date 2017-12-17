package com.halfcut.galaxygarden.level.object.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.util.Shader;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;

import static com.halfcut.galaxygarden.level.object.entity.enemy.Enemy.State.SEEK;
import static com.halfcut.galaxygarden.level.object.entity.enemy.Enemy.State.WANDER;
import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 07/09/2017
 */
public class EnemyDrone extends Enemy {

    static final private int WIDTH  = 6;
    static final private int HEIGHT = 8;
    static final private int HEALTH = 1;
    static final private String SPRITE_REF_GREEN = "entity/enemy_drone_green";
    static final private String SPRITE_REF_RED   = "entity/enemy_drone_red";
    static final private int FLOAT_DISTANCE = 2;
    static final private float SEEKING_TARGET_THETA = 30;

    private float floatTime;
    private float yoffset;
    private float yoffsetprev;

    private Sprite sprite;
    private Sprite spriteGreen;
    private Sprite spriteRed;


    public EnemyDrone(Level level, float x, float y, Player player) {

        super(level, x, y, WIDTH, HEIGHT, HEALTH, player);
        dtheta = (float) ((Math.random() - 0.5f) * 3f);
        floatTime = (float) (Math.random() * (Math.PI*2));

        spriteGreen = Assets.get().getAtlas().createSprite(SPRITE_REF_GREEN);
        spriteRed   = Assets.get().getAtlas().createSprite(SPRITE_REF_RED);
        sprite = spriteGreen;
    }

    @Override
    public boolean update(float delta) {

        // float up and down
        floatTime += 0.1 * delta * DELTA_SCALE;
        if(floatTime > Math.PI) floatTime = (float) (floatTime - 2*Math.PI);

        yoffsetprev = yoffset;
        yoffset = (float) (Math.cos(floatTime)) * FLOAT_DISTANCE;
        pos.y += yoffset - yoffsetprev;

        // ai
        if(state == WANDER) updateWanderState(delta);
        if(state == SEEK)   updateSeekState(delta);

        // position and sprite
        pos.x += vel.x * delta * DELTA_SCALE;
        pos.y += vel.y * delta * DELTA_SCALE;
        sprite.setPosition(pos.x, pos.y);
        sprite.setRotation(theta);
        updateHitbox();

        return super.update(delta);
    }

    @Override
    protected Vector2 updateWanderState(float delta) {

        if(!sprite.equals(spriteGreen)) sprite = spriteGreen;

        vel.x = 0;
        vel.y = 0;
        theta += (0 - theta) * 0.05f * delta * DELTA_SCALE;

        return super.updateWanderState(delta);
    }

    @Override
    protected Vector2 updateSeekState(float delta) {

        if(!sprite.equals(spriteRed)) sprite = spriteRed;

        // move towards player
        Vector2 diff   = super.updateSeekState(delta);
        float angle = (float) Math.atan2(diff.y, diff.x);
        vel.x = (float) (Math.cos(angle) * -0.5f);
        vel.y = (float) (Math.sin(angle) * -0.5f);

        float targetAngle = (diff.x > 0) ? SEEKING_TARGET_THETA : -SEEKING_TARGET_THETA;
        targetAngle *= (diff.len() / DEFAULT_DEACTIVATE_RADIUS);
        theta += (targetAngle - theta) * 0.05f * delta * DELTA_SCALE;

        return diff;
    }



    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        sb.setShader((isHit) ? Shader._WHITE : Shader._DEFAULT);
        sprite.draw(sb);
        sb.setShader(Shader._DEFAULT);

        super.draw(sb, sr);
        drawAggroCircles(sr);
    }

}
