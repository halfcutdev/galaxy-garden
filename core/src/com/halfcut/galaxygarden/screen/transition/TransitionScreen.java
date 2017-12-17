package com.halfcut.galaxygarden.screen.transition;

import com.badlogic.gdx.Gdx;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.screen.Screen;
import com.halfcut.galaxygarden.screen.transition.effect.TransitionEffect;
import com.halfcut.galaxygarden.screen.transition.effect.swipe.SwipeInEffect;
import com.halfcut.galaxygarden.screen.transition.effect.swipe.SwipeOutEffect;

/**
 * @author halfcutdev
 * @since 10/09/2017
 */
public class TransitionScreen implements com.badlogic.gdx.Screen {

    private App app;
    private Screen prevScreen;
    private Screen nextScreen;
    private TransitionEffect outEffect;
    private TransitionEffect inEffect;

    private enum TransitionState {IN, OUT}
    private TransitionState currentState;

    public TransitionScreen(App app, Screen prevScreen, Screen nextScreen) {
        this.app = app;
        this.prevScreen = prevScreen;
        this.nextScreen = nextScreen;
        this.outEffect = new SwipeOutEffect(600);
        this.inEffect  = new SwipeInEffect(600);
        this.currentState = TransitionState.OUT;
    }

    @Override
    public void render(float delta) {
        switch(currentState) {
            case OUT:
                renderOutTransition(delta);
                break;
            case IN:
                renderInTransition(delta);
                break;
        }
    }

    private void renderOutTransition(float delta) {
        // Draw the previous screen and the effect on top of it
        prevScreen.render(delta);
        outEffect.render(delta, app.sbHUD, app.srHUD);

        // Hide the previous screen, then prepare and show the next screen
        if(outEffect.isFinished()) {
            prevScreen.hide();
            currentState = TransitionState.IN;
            nextScreen.init();
            nextScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            nextScreen.show();
        }
    }

    private void renderInTransition(float delta) {
        // Draw the next screen and the effect on top of it
        nextScreen.render(delta);
        inEffect.render(delta, app.sbHUD, app.srHUD);

        if(inEffect.isFinished()) {
            app.setScreen(nextScreen);
        }
    }

    // Unused
    public void resize(int width, int height) {}
    public void pause() {}
    public void resume() {}
    public void show() {}
    public void hide() {}
    public void dispose() {}

}
