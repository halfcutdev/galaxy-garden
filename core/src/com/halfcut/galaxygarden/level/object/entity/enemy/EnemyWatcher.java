package com.halfcut.galaxygarden.level.object.entity.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.level.map.TileMap;
import com.halfcut.galaxygarden.level.object.animation.Animation;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.Shader;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.object.entity.particle.Particle;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleDust;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleLaser;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleSmoke;
import com.halfcut.galaxygarden.level.map.TileCollisionModule;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.util.Palette;
import com.halfcut.galaxygarden.util.Raycaster;
import com.halfcut.galaxygarden.util.Timer;
import com.halfcut.galaxygarden.util.Util;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @date 13/09/17
 *
 * sniper
 *
 */
public class EnemyWatcher extends GroundEnemy {

    static private int nfiring = 0;

    // general
    static final public int WIDTH  = 7;
    static final public int HEIGHT = 14;
    static final public int HEALTH = 4;

    // movement
    static final private float MOVE_SPEED         =  0.30f;
    static final private float ACC                =  0.1f;
    static final private float DEC                =  0.2f;
    static final private float JUMP_HEIGHT        =  2.0f;
    static final private float FALL_SPEED         =  0.15f;
    static final private float TERMINAL_VELOCITY  = -2.8f;
    private TileCollisionModule collision;

    // sprites
    static final private int[] FRAMES = { 1, 1, 9 };
    static final private int A_STAND          = 0;
    static final private int A_JUMP           = 1;
    static final private int A_WALK_FORWARDS  = 2;
    private Sprite head;
    private Sprite[] heads;
    private Sprite[][] sprites;
    private Animation animation;
    private int currentAnimation;

    // weapon
    static final private int FIRING_DELAY = 2000;
    static final private int FIRING_TIME  = 3000;
    private Timer laserTimer;
    private Timer firingTimer;
    private boolean firing;
    private Vector2 v1;
    private Vector2 v2;
    private Vector2 v3;
    private Vector2 l1;
    private Vector2 l2;
    private float laserAngle;

    // particles
    static final private int PARTICLE_DELAY = 60;
    private Timer particleTimer;

    private Sound sfxLaser;

    public EnemyWatcher(Level level, float x, float y, Player player) {

        super(level, x, y, WIDTH, HEIGHT, HEALTH, player);
        collision = new TileCollisionModule(this, level.getTileMap());

        // sprites
        sprites = Util.loadSpritesheet("entity/enemy_watcher_spritesheet", FRAMES, WIDTH, HEIGHT);
        animation = new Animation(sprites[0], 110);
        setAnimation(A_WALK_FORWARDS);
        vel.x = (direction) ? -MOVE_SPEED : MOVE_SPEED;

        // firing
        laserTimer  = new Timer(FIRING_TIME, false);
        firingTimer = new Timer(FIRING_DELAY, false);
        v1 = new Vector2();
        v2 = new Vector2();
        v3 = new Vector2();
        l1 = new Vector2();
        l2 = new Vector2();

        // particle
        particleTimer = new Timer(PARTICLE_DELAY, true);

        sfxLaser = Assets.get().getSFX("sfx/mp3/laser.mp3");
    }

    public boolean update(float delta) {

        if(state == State.WANDER) updateWanderState(delta);
        if(state == State.SEEK)   updateSeekState(delta);

        vel.y -= FALL_SPEED * delta * DELTA_SCALE;
        if(vel.y < TERMINAL_VELOCITY) vel.y = TERMINAL_VELOCITY;
        collision.update(delta);
        if(collision.justLanded()) {
            emitLandingParticle();
        }

        animation.update(delta);
        animation.getCurrentSprite().setPosition(pos.x, pos.y);

        return super.update(delta);
    }

    @Override
    protected Vector2 updateWanderState(float delta) {

        // change to seek state if player within radius
        Vector2 center = getCenter();
        Vector2 other  = player.getCenter();
        Vector2 diff   = center.cpy().sub(other);
        float mag = diff.len();
        if(mag < DEFAULT_ACTIVATE_RADIUS) {
            state = State.SEEK;
            firingTimer.reset();
            firingTimer.start();
        }

        setAnimation(A_WALK_FORWARDS);

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

        animation.getCurrentSprite().setFlip(vel.x < 0, false);
        return null;
    }

    @Override
    protected Vector2 updateSeekState(float delta) {

        // change to wander state if player outside radius
        Vector2 center = getCenter();
        Vector2 other  = player.getCenter();
        Vector2 diff   = center.cpy().sub(other);
        float mag = diff.len();
        if(mag > DEFAULT_DEACTIVATE_RADIUS || player.isDead()) {
            state = State.WANDER;
            laserTimer.stop();
            firingTimer.stop();
            firing = false;
            nfiring--;
            if(nfiring == 0) sfxLaser.stop();
        }

        v1 = getCenter();
        v2 = player.getCenter();
        v1.y += HEIGHT / 4;

        Vector2 laserdiff = v1.cpy().sub(v2);
        float angle = (float) Math.atan2(laserdiff.y, laserdiff.x);
        float angleDifference = (angle - laserAngle);
        if     (angleDifference < -Math.PI) angleDifference = (float) (angleDifference + 2*Math.PI);
        else if(angleDifference >  Math.PI) angleDifference = (float) (angleDifference - 2*Math.PI);
        laserAngle += (angleDifference) * 0.03 * delta * DELTA_SCALE;

        float dx = -(float) (Math.cos(laserAngle) * 200);
        float dy = -(float) (Math.sin(laserAngle) * 200);
        v3 = v1.cpy().add(new Vector2(dx, dy));

        l1 = v1.cpy();
        if(isFacingLeft()) {
            v1.x -= 2;
            l1.x -= 2;
        } else {
            v1.x += 2;
            l1.x += 2;
        }

        Vector2 v4 = Raycaster.getEndPoint(v1, v3, collision.getTileMap());
        l2 = v4.cpy();

        if(firing) {
            if(particleTimer.tick(delta)) {
                emitLaserParticle();
                emitSmokeParticle();
            }
            if(Intersector.intersectSegmentPolygon(l1, l2, player.hitbox)) {
                player.hit(null);
                if(player.isDead()) {
                    firing = false;
                    laserTimer.stop();
                    firingTimer.reset();
                    firingTimer.start();
                    nfiring--;
                    if(nfiring == 0) sfxLaser.stop();
                }
            }
            if(laserTimer.tick(delta)) {
                firing = false;
                laserTimer.stop();
                firingTimer.reset();
                firingTimer.start();
                nfiring--;
                if(nfiring == 0) sfxLaser.stop();
            }
        } else {
            if(firingTimer.tick(delta)) {
                firing = true;
                laserTimer.start();
                laserTimer.reset();
                if(nfiring == 0) sfxLaser.loop(Settings.sfxVolume);
                nfiring++;
            }
        }

        TileMap tileMap = collision.getTileMap();
        if (diff.x > 0) {
            // player to the left
            if(collision.onGround() && tileMap.isBlocked(sensorLeft.x, sensorLeft.y-1)) {
                vel.x = -MOVE_SPEED;
                setAnimation(A_WALK_FORWARDS);
            } else {
                vel.x = 0;
                setAnimation(A_STAND);
            }
        } else {
            // player to the right
            if(collision.onGround() && tileMap.isBlocked(sensorRight.x, sensorRight.y-1)) {
                vel.x = MOVE_SPEED;
                setAnimation(A_WALK_FORWARDS);
            } else {
                vel.x = 0;
                setAnimation(A_STAND);
            }
        }

        setAnimation((Math.abs(vel.x) > 0) ? A_WALK_FORWARDS : A_STAND);

        animation.getCurrentSprite().setFlip(diff.x > 0, false);
        direction = (diff.x > 0);

        return diff;
    }

    public void switchDirection() {

        direction = !direction;
        vel.x = (direction) ? -MOVE_SPEED : MOVE_SPEED;
    }



    // rendering

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        sb.setShader((isHit) ? Shader._WHITE : Shader._DEFAULT);
        animation.getCurrentSprite().setFlip(direction, false);
        animation.getCurrentSprite().draw(sb);
        sb.setShader(Shader._DEFAULT);

        drawLaser(sb, sr);
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

    private void drawLaser(SpriteBatch sb, ShapeRenderer sr) {

        if(state == State.SEEK) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            sr.begin();

                if(firing) {
                    sr.setColor((Math.random() > 0.5) ? Palette.IIEM : Palette.WHITE);
                    sr.line(l1, l2);
                } else {
                    Color c1 = Palette.IIEM.cpy();
                    Color c2 = Palette.IIEM.cpy();
                    c1.a = 0.6f;
                    c2.a = 0.0f;
                    sr.line(v1.x, v1.y, v2.x, v2.y, c1, c2);
                }

            sr.end();
        }
    }

    private void emitLaserParticle() {

        float size = (float) (1 + (Math.random() * 2));
        ParticleLaser particle = new ParticleLaser(level, l2.x, l2.y, size);
        level.addParticle(particle);
    }

    private void emitSmokeParticle() {

        float x = l2.x;
        float y = l2.y;
        float dx = (float) (Math.random() - 0.5) * 0.2f;
        float dy = (float) (Math.random()) * 0.2f;
        float maxSize = 3;
        float size = (float) (3 + Math.random() * maxSize);
        ParticleSmoke smoke = new ParticleSmoke(level, x, y, dx, dy, size);
        level.addParticle(smoke);
    }

}
