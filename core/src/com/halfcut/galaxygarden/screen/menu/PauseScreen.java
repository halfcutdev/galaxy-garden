package com.halfcut.galaxygarden.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.screen.Screen;
import com.halfcut.galaxygarden.screen.game.GameScreen;
import com.halfcut.galaxygarden.ui.ButtonChangeListener;
import com.halfcut.galaxygarden.ui.elements.Button;
import com.halfcut.galaxygarden.ui.elements.TextButton;
import com.halfcut.galaxygarden.util.Palette;
import com.halfcut.galaxygarden.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author halfcutdev
 * @since 21/09/2017
 */
public class PauseScreen extends Screen {

    private boolean init;
    private GameScreen game;
    protected CustomStage stage;

    private Button btnResume;
    private Button btnRestart;
    private Button btnOptions;
    private Button btnQuit;
    protected List<Button> buttons;

    public PauseScreen(App app, GameScreen game) {

        super(app);
        this.game = game;
        stage = new CustomStage(viewport);
        buttons = new ArrayList<Button>();
        init();
    }

    @Override
    public void init() {

        if(!init) {
            // Resume game.
            btnResume = new TextButton(this, App.WIDTH / 2, App.HEIGHT / 2 + 27f, Button.Size.LARGE, "resume");
            btnResume.addListener(new ButtonChangeListener(this) {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    app.setScreen(game);
                    game.togglePause();
                    disableButtons();
                }
            });

            // Restart the level.
            btnRestart = new TextButton(this, App.WIDTH / 2, App.HEIGHT / 2 + 12f, Button.Size.LARGE, "restart level");
            btnRestart.addListener(new ButtonChangeListener(this) {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    disableButtons();
                    screen.transitionToScreen(new GameScreen(screen.getApp(), (game.getLevel().getLevelRef())));
                }
            });

            // Go to options menu.
            btnOptions = new TextButton(this, App.WIDTH / 2, App.HEIGHT / 2 - 3f, Button.Size.LARGE, "options");
            btnOptions.addListener(new ButtonChangeListener(this) {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    disableButtons();
                    screen.transitionToScreen(new OptionsMenuScreen(screen.getApp(), screen));
                }
            });

            // Quit to main menu.
            btnQuit = new TextButton(this, App.WIDTH / 2, App.HEIGHT / 2 - 18f, Button.Size.LARGE, "quit");
            btnQuit.addListener(new ButtonChangeListener(this) {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    disableButtons();
                    screen.transitionToScreen(new MainMenuScreen(screen.getApp()));
                }
            });

            buttons.add(btnResume);
            buttons.add(btnRestart);
            buttons.add(btnOptions);
            buttons.add(btnQuit);
            stage.addActor(btnResume);
            stage.addActor(btnRestart);
            stage.addActor(btnOptions);
            stage.addActor(btnQuit);
            init = true;

        } else {
            btnOptions.remove();
            buttons.remove(btnOptions);
            btnOptions = new TextButton(this, App.WIDTH / 2, App.HEIGHT / 2 - 3f, Button.Size.LARGE, "options");
            btnOptions.addListener(new ButtonChangeListener(this) {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    disableButtons();
                    screen.transitionToScreen(new OptionsMenuScreen(screen.getApp(), screen));
                }
            });
            buttons.add(btnOptions);
            stage.addActor(btnOptions);
            enableButtons();
        }

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        // draw the game
        game.draw(sb, sr);

        // reset the projection matrix
        Matrix4 uiMatrix = camera.combined.cpy();
        uiMatrix.setToOrtho2D(0, 0, App.WIDTH * App.SCALE, App.HEIGHT * App.SCALE);
        sr.setProjectionMatrix(uiMatrix);
        sb.setProjectionMatrix(uiMatrix);

        // draw a dark rectangle over the background
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Vector2 corner = Util.unproject(0, App.HEIGHT * App.SCALE, camera);
        sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Palette.INK.cpy().sub(0, 0, 0, 0.3f));
            sr.rect(corner.x, corner.y, App.WIDTH * App.SCALE, App.HEIGHT * App.SCALE);
        sr.end();

        // draw the stage on top of the rect
        stage.draw();
    }

    private void enableButtons() {
        for (Button button : buttons) {
            button.setDisabled(false);
        }
    }

    private void disableButtons() {
        for (Button button : buttons) {
            button.setDisabled(true);
        }
    }

    private class CustomStage extends Stage {

        public CustomStage(Viewport viewport) {
            super(viewport);
        }

        @Override
        public boolean keyTyped(char character) {
            if(character == 'p') game.togglePause();
            return super.keyTyped(character);
        }

    }

}
