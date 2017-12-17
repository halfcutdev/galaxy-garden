package com.halfcut.galaxygarden;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.screen.LoadingScreen;
import com.halfcut.galaxygarden.storage.Settings;

/**
 * @author halfcutdev
 * @since 06/09/2017
 */
public class App extends Game {

    static final public String TITLE = "Galaxy Garden";
    static final public int WIDTH  = 200;
    static final public int HEIGHT = 150;
    static final public int SCALE  = 2;
    static public boolean DEV_MODE;

    public enum Mode { DESKTOP, HTML }
    static public Mode mode;

    public SpriteBatch   sb;
    public ShapeRenderer sr;

    public SpriteBatch   sbHUD;
    public ShapeRenderer srHUD;

    private FrameBuffer fbo;
    private SpriteBatch sbFBO;

    public Music music;

    @Override
    public void create () {
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);

        sbHUD = new SpriteBatch();
        srHUD = new ShapeRenderer();
        sr.setAutoShapeType(true);

        initFBO();
        initSettings();
        setScreen(new LoadingScreen(this));
    }

    @Override
    public void render () {
        // draw the game to the frame buffer
        fbo.begin();

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            super.render();

        fbo.end();

        // render the frame buffer to the screen
        float x = (Gdx.graphics.getWidth()  - fbo.getWidth()  * SCALE) / 2;
        float y = (Gdx.graphics.getHeight() - fbo.getHeight() * SCALE) / 2;
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sbFBO.begin();
        sbFBO.draw(fbo.getColorBufferTexture(), x, y, WIDTH * SCALE, HEIGHT * SCALE, 0, 0, 1f, 1f);
        sbFBO.end();
    }

    @Override
    public void dispose () {
        sb.dispose();
        sr.dispose();

        fbo.dispose();
        sbFBO.dispose();
    }

    private void initFBO() {
        if(fbo != null) fbo.dispose();
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, WIDTH, HEIGHT, true);
        fbo.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        if(sbFBO != null) sbFBO.dispose();
        sbFBO = new SpriteBatch();
    }

    private void initSettings() {
        Preferences settingsPrefs = Gdx.app.getPreferences("settings");
        if(!settingsPrefs.contains("sfxvolume")) {
            settingsPrefs.putFloat("sfxvolume", Settings.sfxVolume);
            settingsPrefs.putFloat("musvolume", Settings.musVolume);
            settingsPrefs.flush();
        } else {
            Settings.sfxVolume = settingsPrefs.getFloat("sfxvolume");
            Settings.musVolume = settingsPrefs.getFloat("musvolume");
        }
    }

}
