package com.halfcut.galaxygarden.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.screen.game.GameScreen;
import com.halfcut.galaxygarden.ui.ButtonChangeListener;
import com.halfcut.galaxygarden.ui.elements.Button;
import com.halfcut.galaxygarden.ui.elements.ButtonListener;
import com.halfcut.galaxygarden.ui.elements.TextButton;

/**
 * @author halfcutdev
 * @since 18/09/2017
 */
public class MainMenuScreen extends MenuScreen {

    private TextureRegion title;

    public MainMenuScreen(App app) {

        super(app, "demo_menu_main");
    }

    @Override
    public void init() {

        super.init();
        title = Assets.get().getAtlas().findRegion("ui/title_red");
        stage = new Stage(viewport);

        Gdx.input.setInputProcessor(stage);
        camera.position.set(center.x, center.y, 0);
        camera.update();


        // start button
        Button btnStart = new TextButton(this, center.x, center.y - 10, Button.Size.LARGE, "start");
        btnStart.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                disableButtons();
                transitionToScreen(new GameScreen(app, 1));
            }
        });

        // level select button
        Button btnLevel = new TextButton(this, center.x, center.y - 25, Button.Size.LARGE,"level select");
        btnLevel.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                disableButtons();
                transitionToScreen(new LevelSelectMenu(app));
            }
        });

        // options button
        Button btnOptions = new TextButton(this, center.x, center.y - 40, Button.Size.LARGE,"options");
        btnOptions.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                disableButtons();
                transitionToScreen(new OptionsMenuScreen(app, screen));
            }
        });

        // options button
        Button btnQuit = new TextButton(this, center.x, center.y - 55, Button.Size.LARGE,"quit");
        btnQuit.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                disableButtons();
                System.exit(0);
            }
        });

        buttons.add(btnStart);
        buttons.add(btnLevel);
        buttons.add(btnOptions);
        buttons.add(btnQuit);
        stage.addActor(btnStart);
        stage.addActor(btnLevel);
        stage.addActor(btnOptions);
        stage.addActor(btnQuit);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        super.draw(sb, sr);

        sb.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        sb.begin();
            sb.draw(title, center.x - title.getRegionWidth(), center.y + title.getRegionHeight() / 2, title.getRegionWidth() * 2, title.getRegionHeight() * 2);
        sb.end();
    }

}
