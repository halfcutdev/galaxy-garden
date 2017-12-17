package com.halfcut.galaxygarden.level.object.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.level.object.animation.Animation;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.level.object.entity.enemy.EnemyHunter;
import com.halfcut.galaxygarden.screen.game.GameScreen;
import com.halfcut.galaxygarden.util.Shader;
import com.halfcut.galaxygarden.util.Tween;
import com.halfcut.galaxygarden.level.object.entity.interactive.Chest;
import com.halfcut.galaxygarden.level.object.entity.interactive.Valve;
import com.halfcut.galaxygarden.level.object.entity.misc.Terminal;
import com.halfcut.galaxygarden.level.object.entity.particle.Particle;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleDust;
import com.halfcut.galaxygarden.input.InputController;
import com.halfcut.galaxygarden.input.KeyboardInputController;
import com.halfcut.galaxygarden.level.map.TileCollisionModule;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.*;
import com.halfcut.galaxygarden.level.object.weapon.player.WeaponPistol;
import com.halfcut.galaxygarden.level.object.weapon.player.WeaponPlayer;

import java.util.List;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 06/09/2017
 */
public class Player extends Entity {

    static final public int WIDTH  = 6;
    static final public int HEIGHT = 10;

    static final private float MOVE_SPEED         =  1.4f;
    static final private float SPRINT_SPEED       =  2.4f;
    static final private float ACC                =  0.1f;
    static final private float DEC                =  0.2f;
    static final private float FALL_SPEED         =  0.15f;
    static final private float TERMINAL_VELOCITY  = -2.8f;
    static final private float WALL_SLIDE_SPEED   = -0.56f;
    static final private float JUMP_HEIGHT        =  2.45f;
    static final private float WALL_JUMP_HEIGHT   =  3.0f;
    static final private float WALL_JUMP_DISTANCE =  1.6f;
    static final private float HORIZONTAL_KNOCKBACK = 3f;
    static final private float VERTICAL_KNOCKBACK   = 2f;

    // movement
    private float moveSpeed = 1.0f;
    protected TileCollisionModule collision;

    // input
    private InputController input;
    private boolean left;
    private boolean right;
    private boolean fired;
    private boolean firedprev;

    // weapon
    private WeaponPlayer weapon;

    // animation
    static final private int[] N_FRAMES = { 1, 1, 9, 9 };
    static final private int A_STAND          = 0;
    static final private int A_JUMP           = 1;
    static final private int A_WALK_FORWARDS  = 2;
    static final private int A_WALK_BACKWARDS = 3;

    // sprite
    static final private int HEAD_X_OFFSET = 0;
    static final private int HEAD_Y_OFFSET = 3;
    static final private int HEAD_WIDTH  =  9;
    static final private int HEAD_HEIGHT =  9;
    static final private int LEG_WIDTH  =  9;
    static final private int LEG_HEIGHT = 12;
    private Sprite head;
    private Sprite[] heads;
    private Sprite[][] sprites;
    private Animation animation;
    private int currentAnimation;
    private boolean direction; // true/false = left/right

    // wallsliding
    static final private int MID_AIR_WALLJUMP_DELAY = 300;
    private boolean wallslideLeft;
    private boolean wallslideRight;
    private Timer wallslideParticleTimer = new Timer(75, true);
    private Timer walljumpInputDelay     = new Timer(20, false);
    private boolean justWallJumped;
    private boolean canMidAirWalljumpLeft;
    private boolean canMidAirWalljumpRight;
    private Timer midAirWalljumpTimer;

    // interaction
    private boolean inputEnabled;
    private Sprite interact;
    private boolean nextToInteractive;

    // health
    private int hearts = 2;
    private int health;
    private boolean invincible;
    private Timer invincibleTimer;
    private boolean flashing;
    private Timer damageFlashTimer;

    // fading
    static final private int FADE_DURATION = 1500;
    private enum Dir { IN, OUT, NONE }
    private Dir dir;
    private Timer fadeTimer;
    private Vector2 blackholePosition;
    private boolean fadedOut;

    // sfx
    private Sound sfxSpawn = Assets.get().getSFX("sfx/mp3/blackhole.mp3");
    private Sound sfxLand  = Assets.get().getSFX("sfx/mp3/land3.mp3");
    private Sound sfxHurt  = Assets.get().getSFX("sfx/mp3/hurt1.mp3");
    private Sound sfxDead  = Assets.get().getSFX("sfx/mp3/dead.mp3");


    // constructor

    public Player(Level level, float x, float y) {

        super(level, x, y, WIDTH, HEIGHT);

        collision = new TileCollisionModule(this, level.getTileMap());
        weapon = new WeaponPistol(level, this);
        interact = Assets.get().getAtlas().createSprite("entity/interact_bubble");
        if(!level.isDemo()) {
            this.input = new KeyboardInputController(((GameScreen) level.getScreen()));
        }

        midAirWalljumpTimer = new Timer(MID_AIR_WALLJUMP_DELAY, false);

        setDevColour(Palette.GLADE);

        // head
        heads = Util.loadCharacterHeads("entity/player_heads_christmas", HEAD_WIDTH, HEAD_HEIGHT);
        head = heads[0];

        // legs
        sprites = Util.loadSpritesheet("entity/player_legs", N_FRAMES, LEG_WIDTH, LEG_HEIGHT);
        animation = new Animation(sprites[0], 60);

        // health
        health = 2 * hearts;
        invincibleTimer = new Timer(2000, false);
        damageFlashTimer = new Timer(150, false);

        // fade
        fadeTimer = new Timer(FADE_DURATION, false);
        fadeIn();
    }


    // update

    @Override
    public boolean update(float delta) {

        handleInput(delta);
        handleMovement(delta);
        handleSprites(delta);
        handleFading(delta);

        if(weapon.update(delta)) weapon = new WeaponPistol(level, this);

        if(wallslideLeft || wallslideRight) {
            if(wallslideParticleTimer.tick(delta)) {
                emitDustParticle();
            }
        }

        if(invincibleTimer.tick(delta)) {
            invincibleTimer.reset();
            invincible = false;
        }

        if(damageFlashTimer.tick(delta)) {
            damageFlashTimer.stop();
            damageFlashTimer.reset();
            flashing = false;
        }

        return remove;
    }

    private void handleInput(float delta) {

        if(inputEnabled && dir == Dir.NONE) {
            direction = (level.mouseWorldPos().x < getCenter().x);
            handleWallJumping(delta);
            handleWeapon();
            handleInteraction();
        } else {
            nextToInteractive = false;
        }
    }

    private void handleMovement(float delta) {

        if(dir == Dir.NONE) {

            // walking
            //
            left  = input.left();
            right = input.right();

            if(!justWallJumped) {
                if(left && right) {
                    vel.x += (0 - vel.x) * DEC * delta * DELTA_SCALE;
                    if(collision.onGround()) setAnimation(A_STAND);
                } else if(left){
                    vel.x += (-MOVE_SPEED * moveSpeed - vel.x) * ACC * delta * DELTA_SCALE;
                    if(collision.onGround()) {
                        setAnimation((isFacingLeft()) ? A_WALK_FORWARDS : A_WALK_BACKWARDS);
                    }
                } else if(right) {
                    vel.x += ( MOVE_SPEED * moveSpeed - vel.x) * ACC * delta * DELTA_SCALE;
                    if(collision.onGround()) {
                        setAnimation((isFacingLeft()) ? A_WALK_BACKWARDS : A_WALK_FORWARDS);
                    }
                } else {
                    vel.x += (0 - vel.x) * DEC * delta * DELTA_SCALE;
                    if(collision.onGround()) setAnimation(A_STAND);
                }
            } else {
                if (walljumpInputDelay.tick(delta)) {
                    walljumpInputDelay.stop();
                    justWallJumped = false;
                }
            }

            // jumping
            //
            if(collision.onGround() && input.jump() ) {
                vel.y = JUMP_HEIGHT;
            }

            // gravity
            //
            vel.y -= FALL_SPEED * delta * DELTA_SCALE * moveSpeed;
            float maxFallSpeed = TERMINAL_VELOCITY;
            if(((collision.collidingLeft() && left) || (collision.collidingRight() && right))) {
                maxFallSpeed = WALL_SLIDE_SPEED * moveSpeed;
            }
            if(vel.y < maxFallSpeed * moveSpeed) vel.y = maxFallSpeed * moveSpeed;

            collision.update(delta);
            if(collision.justLanded()) {
                emitLandingParticle();
            }

            input.update(delta);
        }
    }

    private void handleSprites(float delta) {

        if(dir == Dir.NONE) {
            animation.getCurrentSprite().setRotation(0);
            animation.getCurrentSprite().setScale(1);
            head.setRotation(0);
            head.setScale(1);
        }

        // legs
        animation.update(delta);
        animation.getCurrentSprite().setPosition(pos.x, pos.y);
        animation.getCurrentSprite().setFlip(isFacingLeft(), false);

        // head
        Vector2 mpos = level.mouseWorldPos();
        if      (mpos.y < pos.y - 10)          head = heads[2];
        else if (mpos.y > pos.y + height + 10) head = heads[1];
        else                                   head = heads[0];

        head.setPosition(pos.x + HEAD_X_OFFSET, pos.y + HEAD_Y_OFFSET);
        head.setFlip(isFacingLeft(), false);

        interact.setPosition(pos.x + width + 1, pos.y + height + 1);
    }

    private void handleFading(float delta) {

        if(dir == Dir.IN) {
            if(fadeTimer.tick(delta)) {
                dir = Dir.NONE;
                fadeTimer.stop();
                fadeTimer.setElapsed(fadeTimer.getDelay());
                theta = 0;
            } else {
                float tween = Tween.cubicOut(fadeTimer.percent(), 0, 1, 1);
                theta = (float) Math.toDegrees((1 - tween) * 720);

                head.setScale(fadeTimer.percent());
                head.setRotation(theta);
                animation.getCurrentSprite().setScale(fadeTimer.percent());
                animation.getCurrentSprite().setRotation(theta);
                weapon.getSprite().setRotation(theta);
                weapon.getSprite().setScale(fadeTimer.percent());
            }
        }

        if(dir == Dir.OUT) {
            if(fadeTimer.tick(delta)) {
                fadeTimer.stop();
                fadeTimer.setElapsed(fadeTimer.getDelay());
                theta = 0;
                fadedOut = true;
            } else {
                float tween = Tween.cubicIn(fadeTimer.percent(), 0, 1, 1);
                theta = (float) Math.toDegrees((tween) * 360);

                pos.x += (blackholePosition.x - pos.x) * 0.1 * delta * DELTA_SCALE;
                pos.y += (blackholePosition.y - pos.y) * 0.1 * delta * DELTA_SCALE;

                head.setScale(1 - fadeTimer.percent());
                head.setRotation(theta);
                animation.getCurrentSprite().setScale(1 - fadeTimer.percent());
                animation.getCurrentSprite().setRotation(theta);
                weapon.getSprite().setRotation(theta);
                weapon.getSprite().setScale(1 - fadeTimer.percent());
            }
        }
    }

    private void handleWeapon() {

        firedprev = fired;
        fired = input.firing();
        if(fired && weapon.canFire()) weapon.fire();
    }

    private void handleInteraction() {

        // activating valves
        List<Valve> valves = level.getValves();
        nextToInteractive = false;
        for(Valve valve : valves) {
            if(Intersector.overlapConvexPolygons(hitbox, valve.hitbox)) {
                nextToInteractive = !valve.isActivated();
                if(input.justActivated()) {
                    valve.interact();
                    if(level.allSprinklersActivated()) level.openBlackhole();
                    break;
                }
            }
        }

        // opening chests
        List<Chest> chests = level.getChests();
        for(Chest chest : chests) {
            if(Intersector.overlapConvexPolygons(hitbox, chest.hitbox)) {
                nextToInteractive = !chest.isOpen();
                if(input.justActivated()) {
                    chest.interact();
                    break;
                }
            }
        }

        // opening chests
        List<Terminal> terminals = level.getTerminals();
        for(Terminal terminal : terminals) {
            if(Intersector.overlapConvexPolygons(hitbox, terminal.hitbox)) {
                nextToInteractive = true;
                if(input.justActivated()) {
                    terminal.interact();
                    break;
                }
            }
        }
    }

    private void handleWallJumping(float delta) {

        // wall jumping
        //
        if(!collision.onGround()) {

            if(collision.justCollidedLeft() || collision.justCollidedRight()){
                float pitch = (float) (0.5f + (Math.random() - 0.5f) * 0.2f);
                sfxLand.play(Settings.sfxVolume * 0.1f, pitch, 0.5f);

                midAirWalljumpTimer.reset();
                midAirWalljumpTimer.start();
            } else {
                wallslideLeft = false;
                wallslideRight = false;
            }

            // wall jumping left side
            wallslideLeft = collision.collidingLeft();
            if(wallslideLeft) {
                canMidAirWalljumpLeft = true;
                midAirWalljumpTimer.start();
                midAirWalljumpTimer.reset();

                if(input.jump()) {
                    wallJumpRight();
                }
            } else {
                if(canMidAirWalljumpLeft) {
                    if(midAirWalljumpTimer.tick(delta)) {
                        canMidAirWalljumpLeft = false;
                        midAirWalljumpTimer.stop();
                        midAirWalljumpTimer.reset();
                    }
                    if(right && input.jump()) {
                        wallJumpRight();
                    }
                }
            }

            wallslideRight = collision.collidingRight();
            if(wallslideRight) {
                canMidAirWalljumpRight = true;
                midAirWalljumpTimer.start();
                midAirWalljumpTimer.reset();

                if(input.jump()) {
                    wallJumpLeft();
                }
            } else {
                if(canMidAirWalljumpRight) {
                    if(midAirWalljumpTimer.tick(delta)) {
                        canMidAirWalljumpRight = false;
                        midAirWalljumpTimer.stop();
                        midAirWalljumpTimer.reset();
                    }
                    if(left && input.jump()) {
                        wallJumpLeft();
                    }
                }
            }

            setAnimation(A_JUMP);

        } else {
            wallslideLeft  = false;
            wallslideRight = false;
        }

        if(canMidAirWalljumpLeft || canMidAirWalljumpRight) {
            if(midAirWalljumpTimer.tick(delta)) {
                canMidAirWalljumpLeft  = false;
                canMidAirWalljumpRight = false;
                midAirWalljumpTimer.reset();
                midAirWalljumpTimer.stop();
            }
        }

        if(!collision.collidingLeft())  wallslideLeft  = false;
        if(!collision.collidingRight()) wallslideRight = false;
    }

    private void wallJumpLeft() {

        vel.y =  WALL_JUMP_HEIGHT;
        vel.x = -WALL_JUMP_DISTANCE;
        emitWallJumpParticle();

        justWallJumped = true;
        walljumpInputDelay.reset();
        walljumpInputDelay.start();

        midAirWalljumpTimer.reset();
        midAirWalljumpTimer.stop();
        canMidAirWalljumpRight = false;
    }

    private void wallJumpRight() {

        vel.y = WALL_JUMP_HEIGHT;
        vel.x = WALL_JUMP_DISTANCE;
        emitWallJumpParticle();

        justWallJumped = true;
        walljumpInputDelay.reset();
        walljumpInputDelay.start();

        midAirWalljumpTimer.reset();
        midAirWalljumpTimer.stop();
        canMidAirWalljumpLeft = false;
    }


    // other

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        sb.setShader((flashing) ? Shader._WHITE : Shader._DEFAULT);
        if(nextToInteractive) interact.draw(sb);
        animation.getCurrentSprite().draw(sb);
        head.draw(sb);

        sb.setShader(Shader._DEFAULT);
        weapon.draw(sb, sr);
        super.draw(sb, sr);
    }

    private void setAnimation(int index) {
        if(index < 0 || index >= N_FRAMES.length) {
            System.err.println("[player] invalid animation index");
            return;
        }

        if(index != currentAnimation) {
            currentAnimation = index;
            animation.setFrames(sprites[index]);
        }
    }

    @Override
    public boolean hit(Entity entity) {

        if(entity instanceof EnemyHunter) {
            health -= 4;
        }
        if(!invincible) {
            sfxHurt.play(Settings.sfxVolume * 0.5f);
            level.screenshake(3, 300);
            level.getHud().showHealth();

            health--;

            invincible = true;
            invincibleTimer.reset();
            invincibleTimer.start();
            damageFlashTimer.start();
            flashing = true;

            if(entity != null) {
                Vector2 diff = new Vector2(
                    (pos.x + width  / 2) - (entity.pos.x + width  / 2),
                    (pos.y + height / 2) - (entity.pos.y + height / 2)
                );

                float theta = (float) Math.atan2(diff.y, diff.x);
                vel.x = (float) (Math.cos(theta) * HORIZONTAL_KNOCKBACK);
                vel.y = (float) (Math.sin(theta) * VERTICAL_KNOCKBACK);
            }
        }
        if(health <= 0) {
            sfxDead.play(Settings.sfxVolume * 0.5f);
        }
        return health <= 0;
    }

    @Override
    public boolean hit(float wx, float wy) {
        if(!invincible) {
            health--;
            sfxHurt.play(Settings.sfxVolume * 0.5f);
            level.screenshake(3, 300);
            level.getHud().showHealth();

            invincible = true;
            invincibleTimer.reset();
            invincibleTimer.start();
            damageFlashTimer.start();
            flashing = true;

            if(health <= 0) {
                sfxDead.play(Settings.sfxVolume * 0.5f);
            }

            float dx = wx - getCenter().x;
            float dy = getCenter().y - wy;

            float theta = (float) ((float) Math.atan2(dx, dy) + Math.PI / 2);
            vel.x += (float) (Math.cos(theta) * HORIZONTAL_KNOCKBACK);
            vel.y += (float) (Math.sin(theta) * VERTICAL_KNOCKBACK);
        }

        return health <= 0;
    }


    // particle

    private void emitDustParticle() {
        float size = (float) (1 + Math.random() * 3);
        float x = 0;
        float y = pos.y + height / 2;
        if(wallslideLeft)  x = pos.x;
        if(wallslideRight) x = pos.x+width;
        x += (float) (Math.random() - 0.5f) * 3;
        Particle p = new ParticleDust(level, x, y, size);
        level.addParticle(p);
    }

    private void emitWallJumpParticle() {
        for(int i=0; i<10; i++) {
            float size = (float) (1 + Math.random() * 2);
            float x = 0;
            float y = pos.y + height / 2;
            if(wallslideLeft)  x = pos.x;
            if(wallslideRight) x = pos.x+width;
            float dx = (float) (Math.random() * 0.1f);
            float dy = (float) (Math.random() - 0.5f) * 0.5f;
            if(wallslideRight) dx *= -1;
            Particle p = new ParticleDust(level, x, y, dx, dy, size);
            level.addParticle(p);
        }
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
        float pitch = (float) (0.5f + (Math.random() - 0.5f) * 0.2f);
        sfxLand.play(Settings.sfxVolume * 0.1f, pitch, 0.5f);
    }


    // fading

    public void fadeIn() {
        if(dir != Dir.IN) {
            dir = Dir.IN;
            fadeTimer.reset();
            fadeTimer.start();
        }
    }

    public void fadeOut(Vector2 blackholePosition) {

        if(dir != Dir.OUT) {
            this.blackholePosition = blackholePosition.cpy();
            dir = Dir.OUT;
            fadeTimer.reset();
            fadeTimer.start();
            sfxSpawn.play(Settings.sfxVolume * 0.5f);
        }
    }

    public boolean hasFadedIn() {
        return dir == Dir.NONE;
    }

    public boolean hasFadedOut() {
        return fadedOut;
    }


    // getters & setters

    public int getHearts() {
        return hearts;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isFacingLeft() {
        return direction;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void enableInput() {
        inputEnabled = true;
        input = new KeyboardInputController(((GameScreen) level.getScreen()));
        input.enable();
        input.enablePause();
        Gdx.input.setInputProcessor(input);
    }

    public void disableInput() {
        this.inputEnabled = false;
    }

    public boolean justFired() {
        return fired;
    }

    public boolean justFiredPrev() {
        return firedprev;
    }

    public void setWeapon(WeaponPlayer weapon) {
        this.weapon = weapon;
    }

    public WeaponPlayer getWeapon() {
        return weapon;
    }

    public void enablePause() {
        input.enablePause();
    }

    public void disablePause() {
        input.disablePause();
    }

}
