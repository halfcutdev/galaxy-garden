package com.halfcut.galaxygarden.level.object.entity.enemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.level.object.animation.Effect;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.object.entity.misc.Debris;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.object.entity.particle.Particle;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleFire;
import com.halfcut.galaxygarden.level.object.entity.particle.ParticleSmoke;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.Palette;
import com.halfcut.galaxygarden.util.Timer;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 07/09/2017
 */
public abstract class Enemy extends Entity {

    static final private int HIT_DELAY = 100;

    protected Player player;
    protected int health;
    protected boolean isHit;
    protected Timer hitTimer;

    // ai
    protected enum State { WANDER, SEEK }
    protected State state;
    static final protected int DEFAULT_ACTIVATE_RADIUS =  90;
    static final protected int DEFAULT_DEACTIVATE_RADIUS = 130;
    static final protected int EXPLODE_DELAY = 3000;
    protected int activateRadius;
    protected int deactivateRadius;

    // sfx
    protected Sound sfxExplode;
    protected Sound sfxHurt;

    // particles
    static final private int SMOKE_DELAY = 100;
    static final private int FIRE_DELAY  = 25;
    protected Timer explodeTimer;
    private Timer smokeTimer;
    private Timer fireTimer;

    public Enemy(Level level, float x, float y, float width, float height, int health, Player player) {

        super(level, x, y, width, height);
        this.player = player;
        this.health = health;
        hitTimer = new Timer(HIT_DELAY, false);

        // ai
        state = State.WANDER;
        explodeTimer = new Timer((int) (EXPLODE_DELAY + (Math.random() * 2000)), false);
        activateRadius = DEFAULT_ACTIVATE_RADIUS;
        deactivateRadius = DEFAULT_DEACTIVATE_RADIUS;

        // sfx
        sfxExplode = Assets.get().getSFX("sfx/mp3/explosion2.mp3");
        sfxHurt    = Assets.get().getSFX("sfx/mp3/enemy_hurt.mp3");

        // dev
        setColour(Color.RED);
        setDevColour(Palette.FABRIC);

        // particles
        smokeTimer = new Timer(SMOKE_DELAY, false);
        fireTimer  = new Timer(FIRE_DELAY, false);
    }

    @Override
    public boolean update(float delta) {

        if(isHit && hitTimer.tick(delta)) {
            isHit = false;
        }

        if(smokeTimer.tick(delta)) {
            float x    = getCenter().x;
            float y    = getCenter().y;
            float dx   = (float) (Math.random() - 0.5f) * 0.5f;
            float dy   = (float) (Math.random()) * 0.5f;
            float size = (float) (Math.random() * 3) + 3;
            Particle particle = new ParticleSmoke(level, x, y, dx, dy, size);
            level.addParticleBG(particle);
        }

        if(fireTimer.tick(delta)) {
            float x = getCenter().x;
            float y = getCenter().y;
            Particle particle = new ParticleFire(level, x, y);
            level.addParticleBG(particle);
        }

        if(explodeTimer.tick(delta)) explode();

        return health <= 0 || remove;
    }


    // ai

    protected Vector2 updateWanderState(float delta) {
        // change to seek state if player within radius
        Vector2 center = getCenter();
        Vector2 other  = player.getCenter();
        Vector2 diff   = center.cpy().sub(other);
        float mag = diff.len();
        if(mag < activateRadius) {
            state = State.SEEK;
        }
        return diff;
    }

    protected Vector2 updateSeekState(float delta) {
        // change to wander state if player outside radius
        Vector2 center = getCenter();
        Vector2 other  = player.getCenter();
        Vector2 diff   = center.cpy().sub(other);
        float mag = diff.len();
        if(mag > deactivateRadius || player.isDead()) {
            state = State.WANDER;
        }
        return diff;
    }


    // other

    @Override
    public boolean hit(Entity entity) {

        isHit = true;
        hitTimer.reset();
        hitTimer.start();

        // if dead, explode and set to be removed
        if(--health <= 0) {
            remove = true;
            explode();
        }

        // if nearly dead, start emitting smoke
        if(health <= 1) startSmoking();

        sfxHurt.play(Settings.sfxVolume);
        return remove;
    }

    protected void drawAggroCircles(ShapeRenderer sr) {

        if(App.DEV_MODE) {
            Vector2 center = getCenter();
            sr.begin();
                sr.setColor(Palette.FABRIC);
                sr.circle(center.x, center.y, DEFAULT_ACTIVATE_RADIUS);

                sr.setColor(Palette.BLOOD);
                sr.circle(center.x, center.y, DEFAULT_DEACTIVATE_RADIUS);
            sr.end();
        }
    }

    protected void explode() {

        float x = pos.x + width  / 2;
        float y = pos.y + height / 2;

        // create smoke-spawning debris
        int ndebris = 7;
        double sector = Math.toDegrees(2*Math.PI / ndebris);
        double buffer = sector * 0.05;
        for(int i=0; i<ndebris; i++) {
            float minAngle = (float) (i * sector);
            float angle = (float) Math.toRadians(minAngle + (Math.random() * (sector - buffer)));
            float dx = (float) (Math.cos(angle) * 2);
            float dy = (float) (Math.sin(angle) * 2);
            int life = (int) (100 + Math.random() * 300);
            Debris debris = new Debris(level, x, y, dx, dy, life);
            level.addDebris(debris);
        }

        // explosion animation
        Effect explosion = new Effect(level, x, y, "entity/explosion_big", 6, 45);
        level.addEffect(explosion);

        // screenshake and sound
        level.screenshake(2, 600);
        sfxExplode.play(Settings.sfxVolume * 0.2f, (float) (1 + Math.random()), 0.5f);

        remove = true;
    }



    // getters & setters

    public Player getPlayer() {
        return player;
    }

    public void startSmoking() {
        smokeTimer.start();
    }

    public void startFire() {
        fireTimer.start();
        explodeTimer.start();
    }

}
