package com.halfcut.galaxygarden.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.screen.game.GameScreen;
import com.halfcut.galaxygarden.ui.ButtonChangeListener;
import com.halfcut.galaxygarden.ui.elements.Button;
import com.halfcut.galaxygarden.ui.elements.ButtonListener;
import com.halfcut.galaxygarden.ui.elements.GraphicButton;
import com.halfcut.galaxygarden.ui.elements.TextButton;
import com.halfcut.galaxygarden.util.Palette;

/**
 * @author halfcutdev
 * @since 18/09/2017
 */
public class LevelSelectMenu extends MenuScreen {

    static final private int N_COLUMNS = 3;
    static final private int N_ROWS    = 2;

    public LevelSelectMenu(App app) {

        super(app, "demo_menu_level_select");
    }

    public void init() {

        Preferences levelprefs = Gdx.app.getPreferences("progress");
        int maxLevel = levelprefs.getInteger("level");

        int spacing  = Button.WIDTH_SMALL + 9;
        int totalwidth  = (N_COLUMNS * spacing);
        int totalheight = (N_ROWS    * spacing);

        stage = new Stage(viewport);
        for(int i=0; i<6; i++) {

            float x = (center.x + (i%N_COLUMNS * spacing)) - totalwidth  / 2 + Button.WIDTH_SMALL;
            float y = (center.y + (totalheight / 2 - (i/N_COLUMNS * spacing)));
            final int level = i+1;
            if(level <= maxLevel) {
                Button btn = new TextButton(this, x, y, Button.Size.SMALL, String.valueOf(i+1));
                btn.addListener(new ButtonListener(this, btn));
                btn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        disableButtons();
                        transitionToScreen(new GameScreen(app, level));
                    }
                });
                stage.addActor(btn);
                buttons.add(btn);
            } else {
                Button btn = new GraphicButton(this, x, y, Button.Size.SMALL, "ui/lock");
                btn.setColor(Palette.STEEL);
                btn.removeListener(btn.getListeners().get(0));
                btn.removeListener(btn.getListeners().get(0));
                stage.addActor(btn);
                buttons.add(btn);
            }
        }

        float x = center.x - App.WIDTH / 2 + 20;
        float y = center.y - App.HEIGHT / 2 + 20;
        Button btn = new GraphicButton(this, x, y, Button.Size.SMALL, "ui/arrow_left");
        btn.addListener(new ButtonListener(this, btn));
        btn.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                disableButtons();
                transitionToScreen(new MainMenuScreen(screen.getApp()));
            }
        });
        stage.addActor(btn);
        buttons.add(btn);

        Gdx.input.setInputProcessor(stage);
        camera.position.set(center.x, center.y, 0);
        camera.update();
    }

    @Override
    public void update(float delta) {

        super.update(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        super.draw(sb, sr);
    }

}
