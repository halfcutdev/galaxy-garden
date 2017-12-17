package com.halfcut.galaxygarden.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.screen.transition.TransitionScreen;
import com.halfcut.galaxygarden.util.Util;

import static com.halfcut.galaxygarden.App.WIDTH;
import static com.halfcut.galaxygarden.App.HEIGHT;

/**
 * @author halfcutdev
 * @since 06/09/2017
 */
public abstract class Screen implements com.badlogic.gdx.Screen {

    protected App app;
    protected OrthographicCamera camera;
    protected Viewport viewport;
    private Vector2 mouseWorld;

    abstract public void init();
    abstract public void update(float delta);
    abstract public void draw(SpriteBatch sb, ShapeRenderer sr);

    public Screen(App app) {
        this.app = app;

        // set up and center the camera
        camera = new OrthographicCamera();
        camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);
        mouseWorld = new Vector2();
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw(app.sb, app.sr);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    protected void updateMouseWorldPos() {
        mouseWorld= Util.unproject(Gdx.input.getX(), Gdx.input.getY(), camera);
    }

    public Vector2 mouseWorldPos() {
        return mouseWorld;
    }

    public void transitionToScreen(Screen next) {
        app.setScreen(new TransitionScreen(app, this, next));
    }

    public App getApp() {
        return app;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    // unused
    public void pause() {}
    public void resume() {}
    public void show() {}
    public void hide() {}
    public void dispose() {}

}


