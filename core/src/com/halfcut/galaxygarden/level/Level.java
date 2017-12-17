package com.halfcut.galaxygarden.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.level.map.TileMap;
import com.halfcut.galaxygarden.level.object.animation.Effect;
import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.object.entity.Player;
import com.halfcut.galaxygarden.level.object.entity.bullet.Bullet;
import com.halfcut.galaxygarden.level.object.entity.bullet.BulletEnemy;
import com.halfcut.galaxygarden.level.object.entity.bullet.BulletPlayer;
import com.halfcut.galaxygarden.level.object.entity.enemy.*;
import com.halfcut.galaxygarden.level.object.entity.interactive.Chest;
import com.halfcut.galaxygarden.level.object.entity.interactive.Valve;
import com.halfcut.galaxygarden.level.object.entity.misc.*;
import com.halfcut.galaxygarden.level.object.entity.particle.Particle;
import com.halfcut.galaxygarden.level.object.entity.plants.Plant;
import com.halfcut.galaxygarden.level.object.entity.plants.Tree;
import com.halfcut.galaxygarden.level.object.entity.plants.Vine;
import com.halfcut.galaxygarden.screen.game.GameScreen;
import com.halfcut.galaxygarden.screen.Screen;
import com.halfcut.galaxygarden.ui.hud.HUD;

import java.util.ArrayList;
import java.util.List;

/**
 * @author halfcutdev
 * @since 18/09/2017
 */
public class Level {

    protected Screen screen;
    private String levelref;
    private int nlevel;
    private boolean isDemo;

    protected TileMap tileMap;
    protected Player player;
    protected Blackhole entrance;
    protected Blackhole exit;
    protected List<Bullet>        bulletsPlayer  = new ArrayList<Bullet>();
    protected List<Bullet>        bulletsEnemy   = new ArrayList<Bullet>();
    protected List<Enemy>         enemies        = new ArrayList<Enemy>();
    protected List<Chest>         chests         = new ArrayList<Chest>();
    protected List<Terminal>      terminals      = new ArrayList<Terminal>();
    protected List<Valve>         valves         = new ArrayList<Valve>();
    protected List<Sprinkler>     sprinklers     = new ArrayList<Sprinkler>();
    protected List<SprinklerZone> sprinklerZones = new ArrayList<SprinklerZone>();
    protected List<Plant>         plants         = new ArrayList<Plant>();
    protected List<Fruit>         fruit          = new ArrayList<Fruit>();
    protected List<Debris>        debris         = new ArrayList<Debris>();
    protected List<BulletCasing>  casings        = new ArrayList<BulletCasing>();
    protected List<Effect>        effects        = new ArrayList<Effect>();
    protected List<Particle>      particles      = new ArrayList<Particle>();
    protected List<Particle>      bgparticles    = new ArrayList<Particle>();

    public enum State { ENTER, EXIT, NORMAL, DEAD }
    protected State state;
    private boolean gameover;
    private boolean shouldExit;
    protected HUD hud;


    // init

    public Level(Screen screen, String levelref, int nlevel, boolean isDemo) {
        this.screen = screen;
        this.levelref = levelref;
        this.nlevel = nlevel;
        this.isDemo = isDemo;

        init();
        hud = new HUD(player);
        state = State.ENTER;
    }

    public void init() {
        TiledMap tm = Assets.get().getTiledMap("data/maps/" + levelref + ".tmx");
        tileMap = new TileMap(tm);

        initEntranceExit(tm);
        initPlayer();
        initEnemies(tm);
        initChests(tm);
        initTerminals(tm);
        initSprinklers(tm);
        initSprinklerZones(tm);
        initPlants(tm);
        initValves(tm);
    }

    private void initPlayer() {
        player = new Player(this, entrance.pos.x + Player.WIDTH / 2, entrance.pos.y + Player.HEIGHT / 2);
        player.fadeIn();
    }

    private void initEntranceExit(TiledMap tm) {
        MapObjects objects    = tm.getLayers().get("level").getObjects();

        MapObject entranceObj = objects.get("entrance");
        float x = Float.parseFloat(entranceObj.getProperties().get("x").toString()) - Blackhole.WIDTH / 2;
        float y = Float.parseFloat(entranceObj.getProperties().get("y").toString()) - Blackhole.HEIGHT / 2;
        entrance = new Blackhole(this, x, y);
        entrance.fadeIn();

        MapObject exitObj  = objects.get("exit");
        x = Float.parseFloat(exitObj.getProperties().get("x").toString()) - Blackhole.WIDTH / 2;
        y = Float.parseFloat(exitObj.getProperties().get("y").toString()) - Blackhole.HEIGHT / 2;
        exit = new Blackhole(this, x, y);
    }

    private void initEnemies(TiledMap tm) {
        MapObjects enemyObjects = tm.getLayers().get("enemies").getObjects();
        for(MapObject enemyObj : enemyObjects) {

            String name = enemyObj.getName();
            float x = Float.parseFloat(enemyObj.getProperties().get("x").toString());
            float y = Float.parseFloat(enemyObj.getProperties().get("y").toString());

            Enemy enemy = null;
            if(name.equals("worker"))  enemy = new EnemyWorker(this, x, y, player);
            if(name.equals("drone"))   enemy = new EnemyDrone(this, x, y, player);
            if(name.equals("soldier")) enemy = new EnemySoldier(this, x, y, player);
            if(name.equals("watcher")) enemy = new EnemyWatcher(this, x, y, player);
            if(name.equals("hunter"))  enemy = new EnemyHunter(this, x, y, player);

            addEnemy(enemy);
        }
    }

    private void initChests(TiledMap tm) {
        MapObjects chestObjects = tm.getLayers().get("chests").getObjects();

        for(MapObject chestObj : chestObjects) {
            String type = chestObj.getProperties().get("type").toString();
            float x = Float.parseFloat(chestObj.getProperties().get("x").toString());
            float y = Float.parseFloat(chestObj.getProperties().get("y").toString());
            boolean flip = Boolean.parseBoolean(chestObj.getProperties().get("dir").toString());
            Chest chest = new Chest(this, x, y, type, flip);
            chests.add(chest);
        }
    }

    private void initSprinklers(TiledMap tm) {
        MapObjects sprinklerObjects = tm.getLayers().get("sprinklers").getObjects();

        for(MapObject sprinklerObj : sprinklerObjects) {

            int   id     = Integer.parseInt(sprinklerObj.getProperties().get("sprinkler_id").toString());
            float x      = Float.parseFloat(sprinklerObj.getProperties().get("x").toString());
            float y      = Float.parseFloat(sprinklerObj.getProperties().get("y").toString());
            float width  = Float.parseFloat(sprinklerObj.getProperties().get("width").toString());
            float height = Float.parseFloat(sprinklerObj.getProperties().get("height").toString());

            Sprinkler sprinkler = new Sprinkler(this, x + (width - Sprinkler.WIDTH) / 2, y + height - Sprinkler.HEIGHT + 1, id);
            this.sprinklers.add(sprinkler);
        }
    }

    private void initSprinklerZones(TiledMap tm) {
        MapObjects sprinklerZoneObjects = tm.getLayers().get("sprinkler_zones").getObjects();

        for(MapObject sprinklerZoneObj : sprinklerZoneObjects) {

            int   id     = Integer.parseInt(sprinklerZoneObj.getProperties().get("sprinkler_id").toString());
            float x      = Float.parseFloat(sprinklerZoneObj.getProperties().get("x").toString());
            float y      = Float.parseFloat(sprinklerZoneObj.getProperties().get("y").toString());
            float width  = Float.parseFloat(sprinklerZoneObj.getProperties().get("width").toString());
            float height = Float.parseFloat(sprinklerZoneObj.getProperties().get("height").toString());

            SprinklerZone sprinklerZone = new SprinklerZone(this, x, y, width, height, id);
            this.sprinklerZones.add(sprinklerZone);
        }
    }

    private void initPlants(TiledMap tm) {
        MapObjects treeObjects = tm.getLayers().get("trees").getObjects();
        for(MapObject treeObj : treeObjects) {

            int   id    = Integer.parseInt(treeObj.getProperties().get("sprinkler_id").toString());
            float x     = Float.parseFloat(treeObj.getProperties().get("x").toString());
            float y     = Float.parseFloat(treeObj.getProperties().get("y").toString());
            float width = Float.parseFloat(treeObj.getProperties().get("width").toString());
            String fruitType = treeObj.getProperties().get("fruit_type").toString();

            Tree tree = new Tree(this, x + (width - Tree.WIDTH) / 2, y, id, fruitType);
            this.plants.add(tree);
        }

        MapObjects vineObjects = tm.getLayers().get("vines").getObjects();
        for(MapObject vineObj : vineObjects) {

            int   id    = Integer.parseInt(vineObj.getProperties().get("sprinkler_id").toString());
            float x     = Float.parseFloat(vineObj.getProperties().get("x").toString());
            float y     = Float.parseFloat(vineObj.getProperties().get("y").toString());
            float width = Float.parseFloat(vineObj.getProperties().get("width").toString());
            String fruitType = vineObj.getProperties().get("fruit_type").toString();

            Vine vine = new Vine(this, x + (width - Vine.WIDTH) / 2, y, id, fruitType);
            this.plants.add(vine);
        }
    }

    private void initValves(TiledMap tm) {
        MapObjects valveObjects = tm.getLayers().get("valves").getObjects();

        for(MapObject valveObj : valveObjects) {

            int id = Integer.parseInt(valveObj.getProperties().get("sprinkler_id").toString());

            // sprinklers
            List<Sprinkler> valveSprinklers = new ArrayList<Sprinkler>();
            for(Sprinkler sprinkler : this.sprinklers) {
                if(sprinkler.getId() == id) valveSprinklers.add(sprinkler);
            }

            // sprinkler zones
            List<SprinklerZone> valveSprinklerZones = new ArrayList<SprinklerZone>();
            for(SprinklerZone sprinklerZone : this.sprinklerZones) {
                if(sprinklerZone.getId() == id) valveSprinklerZones.add(sprinklerZone);
            }

            // plants
            List<Plant> valveTrees = new ArrayList<Plant>();
            for(Plant tree : this.plants) {
                if(tree.getId() == id) valveTrees.add(tree);
            }

            float x = Float.parseFloat(valveObj.getProperties().get("x").toString());
            float y = Float.parseFloat(valveObj.getProperties().get("y").toString());
            float width  = Float.parseFloat(valveObj.getProperties().get("width").toString());
            float height = Float.parseFloat(valveObj.getProperties().get("height").toString());

            Valve valve = new Valve(this, x + (width - Valve.WIDTH) / 2, y + (height - Valve.HEIGHT) / 2, valveSprinklers, valveSprinklerZones, valveTrees);
            this.valves.add(valve);
        }
    }

    private void initTerminals(TiledMap tm) {
        MapObjects terminalObjects = tm.getLayers().get("terminals").getObjects();

        for(MapObject terminalObj : terminalObjects) {

            float x = Float.parseFloat(terminalObj.getProperties().get("x").toString());
            float y = Float.parseFloat(terminalObj.getProperties().get("y").toString());
            boolean flip = Boolean.parseBoolean(terminalObj.getProperties().get("dir").toString());
            String text = terminalObj.getProperties().get("text_ref").toString();

            Terminal terminal = new Terminal(this, x, y, flip, text);
            this.terminals.add(terminal);
        }
    }


    // update

    public void update(float delta) {
        tileMap.update(delta);
        entrance.update(delta);
        exit.update(delta);

        if(!isDemo) {
            if(state == State.ENTER)  updateEntryState(delta);
            if(state == State.EXIT)   updateExitState(delta);
            if(state == State.NORMAL) updateNormalState(delta);
            if(state == State.DEAD)   updateDeathState(delta);
        }

        updateAllEntities(delta);
        hud.update(delta);
    }

    private void updateEntryState(float delta) {
        player.update(delta);
        if(player.hasFadedIn()) {
            state = State.NORMAL;
            entrance.fadeOut();
            player.enableInput();
        }
    }

    private void updateExitState(float delta) {
        player.update(delta);
        if(player.hasFadedOut() && !gameover) {
            gameover = true;
            exit.fadeOut();
            shouldExit = true;
        }
    }

    private void updateNormalState(float delta) {
        player.update(delta);

        checkPlayerBulletCollision();
        checkEnemyBulletCollision();
        checkEnemyPlayerCollision();
        checkEnemySprinklerCollision();

        if(player.isDead()) {
            state = State.DEAD;
            exit.pos.x = player.pos.x + (player.width  - Blackhole.WIDTH) / 2;
            exit.pos.y = player.pos.y + (player.height - Blackhole.HEIGHT) / 2;
            exit.fadeIn();

            float exitx = exit.pos.x + (Blackhole.WIDTH  - Player.WIDTH) / 2;
            float exity = exit.pos.y + (Blackhole.HEIGHT - Player.HEIGHT) / 2;

            player.fadeOut(new Vector2(exitx, exity));
            player.disableInput();
            return;
        }

        checkPlayerExitCollision();
    }

    private void updateDeathState(float delta) {
        player.update(delta);
        if(player.hasFadedOut() && !gameover) {
            gameover = true;
            exit.fadeOut();
            shouldExit = true;
        }
    }

    private void updateEntities(float delta, List<? extends Entity> entities) {
        for(int i=0; i<entities.size(); i++) {
            if(entities.get(i).update(delta)) {
                entities.remove(i);
                if(i > 0) i--;
            }
        }
    }

    protected void updateAllEntities(float delta) {
        updateEntities(delta, bulletsPlayer);
        updateEntities(delta, bulletsEnemy);
        updateEntities(delta, enemies);
        updateEntities(delta, chests);
        updateEntities(delta, terminals);
        updateEntities(delta, effects);
        updateEntities(delta, particles);
        updateEntities(delta, bgparticles);
        updateEntities(delta, fruit);
        updateEntities(delta, valves);
        updateEntities(delta, sprinklers);
        updateEntities(delta, plants);
        updateEntities(delta, debris);
        updateEntities(delta, casings);
        updateEntities(delta, sprinklerZones);
    }


    // collision

    private void checkPlayerBulletCollision() {
        for(int i=0; i<bulletsPlayer.size(); i++) {
            for(int j=0; j<enemies.size(); j++) {
                if(bulletsPlayer.size() == 0 || enemies.size() == 0) return;
                Bullet bullet = bulletsPlayer.get(i);
                Enemy  enemy  = enemies.get(j);
                if(Intersector.overlapConvexPolygons(bullet.hitbox, enemy.hitbox)) {

                    if(bullet.hit(enemy)) {
                        bulletsPlayer.remove(i);
                        if(i > 0) i--;
                    }
                    if(enemy.hit(bullet)) {
                        enemies.remove(j);
                        if(j > 0) j--;
                    }

                }
            }
        }
    }

    private void checkEnemyBulletCollision() {
        for(int i=0; i<bulletsEnemy.size(); i++) {
            if(bulletsEnemy.size() == 0) return;
            Bullet bullet = bulletsEnemy.get(i);
            if(Intersector.overlapConvexPolygons(player.hitbox, bullet.hitbox)) {
                bullet.hit(player);
                player.hit(bullet);
                bulletsEnemy.remove(i);
                if(i > 0) i--;
            }
        }
    }

    private void checkPlayerExitCollision() {
        if(exit.canCollide() && Intersector.overlapConvexPolygons(exit.hitbox, player.hitbox)) {
            float exitx = exit.pos.x + (Blackhole.WIDTH - Player.WIDTH) / 2;
            float exity = exit.pos.y + (Blackhole.HEIGHT - Player.HEIGHT) / 2;
            player.fadeOut(new Vector2(exitx, exity));
            player.disableInput();
            player.disablePause();
            state = State.EXIT;
        }
    }

    private void checkEnemyPlayerCollision() {

        for(int i=0; i<enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if(Intersector.overlapConvexPolygons(player.hitbox, enemy.hitbox)) {
//                screenshake(4, 500);
                player.hit(enemy);
            }
        }
    }

    private void checkEnemySprinklerCollision() {

        for(int i=0; i<enemies.size(); i++) {

            Enemy enemy = enemies.get(i);
            if(enemy == null) continue;

            for(int j=0; j<sprinklers.size(); j++) {

                Sprinkler sprinkler = sprinklers.get(j);
                if(sprinkler == null) continue;

                if(sprinkler.isActivated() && !sprinkler.isFinished()) {
                    if(Intersector.overlapConvexPolygons(enemy.hitbox, sprinkler.getWaterHitbox())) {
                        enemy.startFire();
                        enemy.startSmoking();
                    }
                }

            }
        }
    }


    // entities

    public void addPlayerBullet(BulletPlayer bullet) {
        bulletsPlayer.add(bullet);
    }

    public void addEnemyBullet(BulletEnemy bullet) {
        bulletsEnemy.add(bullet);
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.add(enemy);
    }

    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }

    public void addParticle(Particle particle) {
        this.particles.add(particle);
    }

    public void addParticleBG(Particle particle) {
        this.bgparticles.add(particle);
    }

    public void addDebris(Debris debris) {
        this.debris.add(debris);
    }

    public void addFruit(Fruit fruit) {
        this.fruit.add(fruit);
    }

    public void addBulletCasing(BulletCasing casing) {
        this.casings.add(casing);
    }

    public void openBlackhole() {
        exit.fadeIn();
    }


    // rendering

    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        tileMap.draw(sb, sr);

        drawEntities(sb, sr, valves);
        drawEntities(sb, sr, sprinklers);
        drawEntities(sb, sr, plants);
        drawEntities(sb, sr, debris);
        drawEntities(sb, sr, bgparticles);
        drawEntities(sb, sr, fruit);
        drawEntities(sb, sr, chests);
        drawEntities(sb, sr, terminals);
        drawEntities(sb, sr, bulletsPlayer);
        drawEntities(sb, sr, bulletsEnemy);
        drawEntities(sb, sr, enemies);
        drawEntities(sb, sr, sprinklerZones);

        entrance.draw(sb, sr);
        exit.draw(sb, sr);

        if(!gameover && !isDemo) player.draw(sb, sr);

        drawEntities(sb, sr, casings);
        drawEntities(sb, sr, particles);
        drawEntities(sb, sr, effects);

        hud.draw(sb, sr);
    }

    private void drawEntities(SpriteBatch sb, ShapeRenderer sr, List<? extends Entity> entities) {
        for(Entity entity : entities) {
            entity.draw(sb, sr);
        }
    }


    // other

    public void screenshake(float power, float duration) {
        ((GameScreen) screen).screenshake(power, duration);
    }


    // getters & setters

    public int getNlevel() {
        return nlevel;
    }

    public String getLevelRef() {
        return levelref;
    }

    public TileMap getTileMap() {
        return tileMap;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Valve> getValves() {
        return valves;
    }

    public List<Chest> getChests() {
        return this.chests;
    }

    public List<Terminal> getTerminals() {
        return this.terminals;
    }

    public boolean allSprinklersActivated() {
        for(Valve valve : valves) {
            if(!valve.isActivated()) return false;
        }
        return true;
    }

    public boolean shouldExit() {
        return shouldExit;
    }

    public State getState() {
        return state;
    }

    public Vector2 mouseWorldPos() {
        return screen.mouseWorldPos();
    }

    public HUD getHud() {
        return hud;
    }

    public Screen getScreen() {
        return screen;
    }

    public boolean isDemo() {
        return isDemo;
    }

}
