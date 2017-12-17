package com.halfcut.galaxygarden.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public abstract class InputController implements InputProcessor {

    protected boolean enabled;
    protected boolean pauseEnabled;

    abstract public void update(float delta);
    abstract public boolean left();
    abstract public boolean right();
    abstract public boolean jump();
    abstract public boolean firing();
    abstract public boolean justFired();
    abstract public boolean justActivated();

    public void enable() {
        enabled = true;
        enablePause();
    }

    public void disable() {
        enabled = false;
    }

    public void enablePause() {

        pauseEnabled = true;
        Gdx.input.setInputProcessor(this);
    }

    public void disablePause() {
        pauseEnabled = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
