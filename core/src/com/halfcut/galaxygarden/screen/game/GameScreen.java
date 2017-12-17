package com.halfcut.galaxygarden.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.camera.Screenshake;
import com.halfcut.galaxygarden.camera.SmoothCamera;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.screen.Screen;
import com.halfcut.galaxygarden.screen.menu.MainMenuScreen;
import com.halfcut.galaxygarden.screen.menu.PauseScreen;
import com.halfcut.galaxygarden.ui.Cursor;
import com.halfcut.galaxygarden.util.Util;

/**
 * @author halfcutdev
 * @since 06/09/2017
 */
public class GameScreen extends Screen {

    private Level level;
    private Sprite background;
    private Screenshake screenshake;
    private boolean exiting;
    private boolean paused;

    public GameScreen(App app, int nlevel) {
        super(app);
        background = new Sprite(Assets.get().getAtlas().findRegion("world/background"));
        level = new Level(this, ("level_" + nlevel), nlevel, false);

        // set up camera
        camera = new SmoothCamera(this, level.getPlayer().pos.x, level.getPlayer().pos.y, level.getPlayer().pos);
        viewport.setCamera(camera);
        screenshake = new Screenshake();

        // update the player's progress
        Preferences progress = Gdx.app.getPreferences("progress");
        if(progress.contains("level")) {
            int maxlevel = progress.getInteger("level");
            if(nlevel > maxlevel) {
                progress.putInteger("level", nlevel);
            }
        } else {
            progress.putInteger("level", nlevel);
        }
        progress.flush();
    }

    public GameScreen(App app, String levelref) {
        super(app);
        background = new Sprite(Assets.get().getAtlas().findRegion("world/background"));
        background.setPosition(0, 0);
        level = new Level(this, levelref, -1, false);

        camera = new SmoothCamera(this, level.getPlayer().pos.x, level.getPlayer().pos.y, level.getPlayer().pos);
        viewport.setCamera(camera);
        screenshake = new Screenshake();
    }

    @Override
    public void init() {
        Cursor.set(Cursor.CursorType.CROSSHAIR);
        level.getPlayer().enablePause();
    }

    @Override
    public void update(float delta) {
        // update camera
        ((SmoothCamera) camera).updatePosition(delta);
        screenshake.update(delta, camera);
        camera.update();

        // update game if not paused
        if(!paused) {
            level.update(delta);
            updateMouseWorldPos();
        }

        // if the level isn't already exiting, but should, then begin the transition
        if(level.shouldExit() && !exiting) {
            exiting = true;
            if(level.getState() == Level.State.DEAD) {
                transitionToScreen(new GameScreen(app, level.getLevelRef()));
            } else if(level.getState() == Level.State.EXIT) {
                if(level.getNlevel() == 6) {
                    transitionToScreen(new MainMenuScreen(app));
                } else {
                    transitionToScreen(new GameScreen(app, level.getNlevel() + 1));
                }
            }
        }
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        sb.begin();
            sb.setColor(Color.WHITE);
            Vector2 bgpos = Util.unproject((int) 0, (int) App.HEIGHT *2, camera);
            background.setPosition(bgpos.x, bgpos.y);
            background.draw(sb);
            level.draw(sb, sr);
        sb.end();
    }


    // other

    public void screenshake(float power, float duration) {
        screenshake.shake(power, duration);
    }

    public void togglePause() {
        paused = !paused;
        if(paused) {
            Cursor.set(Cursor.CursorType.POINTER);
            app.setScreen(new PauseScreen(app, this));

        } else{
            Cursor.set(Cursor.CursorType.CROSSHAIR);
            app.setScreen(this);
            level.getPlayer().enableInput();
        }
    }

    @Override
    public void pause() {
        this.paused = true;
        Cursor.set(Cursor.CursorType.POINTER);
        app.setScreen(new PauseScreen(app, this));
    }

    public boolean isPaused() {
        return this.paused;
    }

    public Level getLevel() {
        return level;
    }

}
