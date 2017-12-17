package com.halfcut.galaxygarden.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.screen.game.GameScreen;
import com.halfcut.galaxygarden.screen.menu.PauseScreen;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class KeyboardInputController extends InputController implements InputProcessor {

    private boolean left;
    private boolean right;
    private boolean jump;
    private boolean jumpPrev;
    private boolean interact;
    private boolean interactPrev;

    private boolean mousePressed;
    private boolean mousePressedPrev;

    private GameScreen game;

    public KeyboardInputController(GameScreen game) {
        this.game = game;
    }

    @Override
    public void update(float delta) {
        left  = Gdx.input.isKeyPressed(Input.Keys.A);
        right = Gdx.input.isKeyPressed(Input.Keys.D);

        jumpPrev = jump;
        interactPrev = interact;
        mousePressedPrev = mousePressed;
    }

    @Override
    public boolean left() {
        return left;
    }

    @Override
    public boolean right() {
        return right;
    }

    @Override
    public boolean jump() {
        return jump && !jumpPrev;
    }

    @Override
    public boolean firing() {
        return mousePressed;
    }

    @Override
    public boolean justFired() {
        return mousePressed && !mousePressedPrev;
    }

    @Override
    public boolean justActivated() {
        return interact && !interactPrev;
    }



    // listener

    @Override
    public boolean keyDown(int keycode) {
        if(enabled) {
            if(keycode == Input.Keys.W || keycode == Input.Keys.SPACE) jump = true;
            if(keycode == Input.Keys.A) left  = true;
            if(keycode == Input.Keys.D) right = true;
            if(keycode == Input.Keys.E) interact = true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(enabled) {
            if(keycode == Input.Keys.W || keycode == Input.Keys.SPACE) jump = false;
            if(keycode == Input.Keys.A) left  = false;
            if(keycode == Input.Keys.D) right = false;
            if(keycode == Input.Keys.E) interact = false;
        }
        if(keycode == Input.Keys.BACKSPACE) App.DEV_MODE = !App.DEV_MODE;

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(enabled) mousePressed = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(enabled) mousePressed = false;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(character == 'p' && pauseEnabled) game.togglePause();
        return false;
    }



    // unused

    public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
    public boolean mouseMoved(int screenX, int screenY) { return false; }
    public boolean scrolled(int amount) { return false; }

}
