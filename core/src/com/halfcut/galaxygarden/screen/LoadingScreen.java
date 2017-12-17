package com.halfcut.galaxygarden.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.assets.Text;
import com.halfcut.galaxygarden.assets.TextLoader;
import com.halfcut.galaxygarden.screen.menu.MainMenuScreen;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.Palette;

import static com.halfcut.galaxygarden.App.HEIGHT;
import static com.halfcut.galaxygarden.App.SCALE;
import static com.halfcut.galaxygarden.App.WIDTH;

/**
 * @author halfcutdev
 * @since 06/09/2017
 */
public class LoadingScreen extends Screen {

    private AssetManager assets;
    private boolean loaded;

    public LoadingScreen(App app) {
        super(app);

        // set up viewport and camera
        camera = new OrthographicCamera();
        camera.position.set(WIDTH / 2 * SCALE, HEIGHT / 2 * SCALE, 0);
        viewport = new FitViewport(WIDTH * SCALE, HEIGHT * SCALE, camera);

        // initialise specialised loaders
        assets = new AssetManager();
        assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assets.setLoader(Text.class, new TextLoader(new InternalFileHandleResolver()));

        // load everything
        loadData();
        loadSounds();
        loadUI();
        loadUserProgress();

        // If in desktop mode, set the custom cursor
        if(App.mode == App.Mode.DESKTOP) {
            Texture cursor = new Texture(Gdx.files.internal("sprites/ui/cursor_pointer.png"));
            cursor.getTextureData().prepare();
            Pixmap pm = cursor.getTextureData().consumePixmap();
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
            pm.dispose();
        }
    }

    @Override
    public void init() {
        // do nothing
    }

    @Override
    public void update(float delta) {
        if(assets.update()) {
            Assets.get().provide(assets);
            if(!loaded) {
                loaded = true;
                app.music = Assets.get().getMusic("sfx/music.mp3");
                app.music.setLooping(true);
                app.music.play();
                app.music.setVolume(Settings.musVolume * 0.1f);
//                transitionToScreen(new GameScreen(app, "level_test_2"));
                transitionToScreen(new MainMenuScreen(app));
            }
        }
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        float x, y, width, height;
        float gap = 4;

        Matrix4 uiMatrix = camera.combined.cpy();
        uiMatrix.setToOrtho2D(0, 0, App.WIDTH * App.SCALE, App.HEIGHT * App.SCALE);
        sr.setProjectionMatrix(uiMatrix);
        sb.setProjectionMatrix(uiMatrix);

        sr.begin(ShapeRenderer.ShapeType.Filled);

            // bg
            sr.setColor(Palette.INK);
            sr.rect(0, 0, App.WIDTH * App.SCALE, App.HEIGHT * App.SCALE);

            // outer
            width  = 80;
            height = 12;
            x = (App.WIDTH * App.SCALE - width) / 2;
            y = (App.HEIGHT * App.SCALE - height) / 2;
            sr.setColor(Color.WHITE);
            sr.rect(x, y, width, height);

            // inner
            width  = width - 5;
            height = height - gap;
            x = (App.WIDTH * App.SCALE - width) / 2;
            y = (App.HEIGHT * App.SCALE - height) / 2;
            sr.setColor(Palette.INK);
            sr.rect(x, y, width, height);

            // bar
            width = width - 5;
            height = height - gap;
            x = (App.WIDTH * App.SCALE - width) / 2;
            y = (App.HEIGHT * App.SCALE - height) / 2;
            width = assets.getProgress() * width;
            sr.setColor(Color.WHITE);
            sr.rect(x, y, width+1, height);

        sr.end();
    }


    // load

    private void loadSounds() {
        assets.load("sfx/music.mp3", Music.class);
        loadSFX("sfx/mp3/terminal.mp3");
        loadSFX("sfx/mp3/land3.mp3");
        loadSFX("sfx/mp3/pistol.mp3");
        loadSFX("sfx/mp3/shotgun.mp3");
        loadSFX("sfx/mp3/hurt1.mp3");
        loadSFX("sfx/mp3/explosion1.mp3");
        loadSFX("sfx/mp3/explosion2.mp3");
        loadSFX("sfx/mp3/dead.mp3");
        loadSFX("sfx/mp3/blackhole.mp3");
        loadSFX("sfx/mp3/water.mp3");
        loadSFX("sfx/mp3/chest1.mp3");
        loadSFX("sfx/mp3/enemy_hurt.mp3");
        loadSFX("sfx/mp3/blip.mp3");
        loadSFX("sfx/mp3/blip2.mp3");
        loadSFX("sfx/mp3/laser.mp3");
    }

    private void loadData() {
        // levels
        loadMap("data/maps/demo_menu_main.tmx");
        loadMap("data/maps/demo_menu_level_select.tmx");
        loadMap("data/maps/demo_menu_options.tmx");
        loadMap("data/maps/level_1.tmx");
        loadMap("data/maps/level_2.tmx");
        loadMap("data/maps/level_3.tmx");
        loadMap("data/maps/level_4.tmx");
        loadMap("data/maps/level_5.tmx");
        loadMap("data/maps/level_6.tmx");
        loadMap("data/maps/level_test.tmx");
        loadMap("data/maps/level_test_2.tmx");


        // text
        loadText("data/terminals/1_1_interacting.txt");
        loadText("data/terminals/1_2_wall_jumping.txt");
        loadText("data/terminals/1_3_conclusion.txt");
        loadText("data/terminals/2_1_enemies.txt");
        loadText("data/terminals/2_2_enemies_more.txt");
        loadText("data/terminals/3_1_greener.txt");
        loadText("data/terminals/4_1_health.txt");
        loadText("data/terminals/4_2_weapons.txt");
        loadText("data/terminals/5_1_compliment.txt");
        loadText("data/terminals/6_1_run.txt");
        loadText("data/terminals/6_2_what.txt");
        loadText("data/terminals/6_3_secret.txt");
        loadText("data/terminals/7_1_thorns.txt");
        loadText("data/terminals/merry_christmas.txt");
    }

    private void loadUI() {
        assets.load("packed/textures.atlas", TextureAtlas.class);
        loadTexture("sprites/ui/cursor_pointer.png");
        loadTexture("sprites/ui/cursor_crosshair.png");
        loadFont("fonts/lionel8.fnt");
    }

    private void loadUserProgress() {
        Preferences progress = Gdx.app.getPreferences("progress");
        if(!progress.contains("level")) {
            progress.putInteger("level", 1);
            progress.flush();
        }
    }
    
    private void loadFont(String ref) {
        assets.load(ref, BitmapFont.class);
    }

    private void loadMap(String ref) {
        assets.load(ref, TiledMap.class);
    }

    private void loadText(String ref) {
        assets.load(ref, Text.class);
    }

    private void loadSFX(String ref) {
        assets.load(ref, Sound.class);
    }

    private void loadTexture(String ref) {
        assets.load(ref, Texture.class);
    }

}
