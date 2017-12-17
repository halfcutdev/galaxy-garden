package com.halfcut.galaxygarden.level.object.entity.enemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.level.map.TileMap;
import com.halfcut.galaxygarden.level.object.animation.Animation;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.util.Shader;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.object.entity.particle.Particle;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleDust;
import com.halfcut.galaxygarden.level.map.TileCollisionModule;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.Timer;
import com.halfcut.galaxygarden.util.Util;
import com.halfcut.galaxygarden.level.object.weapon.enemy.WeaponEnemy;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @date 12/09/17
 *
 * soldier with basic gun
 */
public class EnemySoldier extends GroundEnemy {

    // general
    static final public int WIDTH  = 7;
    static final public int HEIGHT = 12;
    static final public int HEALTH = 3;

    // movement
    static final private float MOVE_SPEED         =  0.65f;
    static final private float ACC                =  0.1f;
    static final private float DEC                =  0.2f;
    static final private float JUMP_HEIGHT        =  2.0f;
    static final private float FALL_SPEED         =  0.15f;
    static final private float TERMINAL_VELOCITY  = -2.8f;
    static final private float MIN_DISTANCE_TO_PLAYER = 30;
    static final private int MINIMUM_JUMP_DELAY = 1000;

    private TileCollisionModule collision;
    private Timer jumpTimer;
    private float minDistanceToPlayer;

    // sprites
    static final private int[] FRAMES = { 1, 1, 9, 9 };
    static final private int A_STAND          = 0;
    static final private int A_JUMP           = 1;
    static final private int A_WALK_FORWARDS  = 2;
    private Sprite[][] sprites;
    private Animation animation;
    private int currentAnimation;

    // weapon
    static final private int DEFAULT_WEAPON_DELAY = 1000;
    protected WeaponEnemy weapon;
    protected Timer weaponTimer;

    private Sound sfxFire;

    public EnemySoldier(Level level, float x, float y, Player player) {

        super(level, x, y, WIDTH, HEIGHT, HEALTH, player);

        collision = new TileCollisionModule(this, level.getTileMap());
        jumpTimer = new Timer(MINIMUM_JUMP_DELAY, true);
        minDistanceToPlayer = (float) (MIN_DISTANCE_TO_PLAYER + (Math.random() * 20));

        // sprites
        sprites = Util.loadSpritesheet("entity/enemy_soldier_spritesheet", FRAMES, WIDTH, HEIGHT);
        animation = new Animation(sprites[0], 60);

        // weapon
        weapon = new WeaponEnemy(level, this);
        weaponTimer = new Timer(DEFAULT_WEAPON_DELAY, true);
        sfxFire = Assets.get().getSFX("sfx/mp3/pistol.mp3");
    }

    @Override
    public boolean update(float delta) {

        if(state == State.WANDER) updateWanderState(delta);
        if(state == State.SEEK)   updateSeekState(delta);

        // movement & gravity
        vel.y -= FALL_SPEED * delta * DELTA_SCALE;
        if(vel.y < TERMINAL_VELOCITY) vel.y = TERMINAL_VELOCITY;
        collision.update(delta);
        if(collision.justLanded()) {
            emitLandingParticle();
        }

        animation.update(delta);
        animation.getCurrentSprite().setPosition(pos.x, pos.y);

        weapon.update(delta);

        return super.update(delta);
    }

    @Override
    protected Vector2 updateWanderState(float delta) {

        vel.x += (0 - vel.x) * DEC * delta * DELTA_SCALE;
        if(Math.abs(vel.x) > 0.1) setAnimation(A_STAND);
        weapon.setTarget(null);
        return super.updateWanderState(delta);
    }

    @Override
    protected Vector2 updateSeekState(float delta) {

        Vector2 diff = super.updateSeekState(delta);
        direction = (diff.x > 0);
        if(state == State.WANDER) return diff;

        if(diff.len() > minDistanceToPlayer) {
            if(diff.x > 0) {
                // player to the left
                vel.x += (-MOVE_SPEED - vel.x) * ACC * delta * DELTA_SCALE;
            } else {
                // player to the right
                vel.x += ( MOVE_SPEED - vel.x) * ACC * delta * DELTA_SCALE;
            }
            setAnimation(A_WALK_FORWARDS);
        } else {
            vel.x += (0 - vel.x) * DEC * delta * DELTA_SCALE;
            setAnimation(A_STAND);
        }

        TileMap tileMap = collision.getTileMap();
        if(tileMap.isBlocked(sensorLeft.x, pos.y+2)) jump();
        if(tileMap.isBlocked(sensorRight.x, pos.y+2)) jump();

        if(jumpTimer.tick(delta) && diff.len() > minDistanceToPlayer) {
            jump();
            jumpTimer.setDelay((int) (1000 + (Math.random() * 2000)));
        }

        weapon.setTarget(player.getCenter());
        if(weaponTimer.tick(delta)) {
            weapon.fire();
            weaponTimer.setDelay((int) (DEFAULT_WEAPON_DELAY + (Math.random() - 0.5f) * 2000));
            sfxFire.play(Settings.sfxVolume * 0.4f, (float) (0.5f + Math.random()), 0.5f);
        }

        return super.updateSeekState(delta);
    }

    private void jump() {

        if(collision.onGround()) {
            vel.y = JUMP_HEIGHT;
            setAnimation(A_JUMP);
        }
    }


    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        sb.setShader((isHit) ? Shader._WHITE : Shader._DEFAULT);
            animation.getCurrentSprite().setFlip(direction, false);
            animation.getCurrentSprite().draw(sb);
        sb.setShader(Shader._DEFAULT);

        weapon.draw(sb, sr);

        super.draw(sb, sr);
        drawAggroCircles(sr);
    }

    private void emitLandingParticle() {

        for(int i=0; i<10; i++) {
            float size = (float) (1 + Math.random() * 2);
            float x = pos.x + width / 2;
            float y = pos.y;
            float dx = (float) (Math.random() - 0.5f) * 0.5f;
            float dy = (float) (Math.random() * 0.1f);
            Particle p = new ParticleDust(level, x, y, dx, dy, size);
            level.addParticle(p);
        }
    }

    private void setAnimation(int index) {

        if(index < 0 || index >= FRAMES.length) {
            System.err.println("[player] invalid animation index");
            return;
        }

        if(index != currentAnimation) {
            currentAnimation = index;
            animation.setFrames(sprites[index]);
        }
    }

}
