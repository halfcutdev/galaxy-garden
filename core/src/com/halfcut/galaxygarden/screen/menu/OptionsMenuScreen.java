package com.halfcut.galaxygarden.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.screen.Screen;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.ui.ButtonChangeListener;
import com.halfcut.galaxygarden.ui.elements.Button;
import com.halfcut.galaxygarden.ui.elements.GraphicButton;

/**
 * @author halfcutdev
 * @since 22/09/2017
 */
public class OptionsMenuScreen extends MenuScreen {

    static final private int MAX_VOLUME = 10;
    static final private int BUTTON_OFFSET = 40;

    private Screen previous;

    public Preferences settingsprefs;
    public int sfxVolume;
    public int musVolume;

    private TextureRegion bar;
    private TextureRegion barDark;

    private BitmapFont font;
    private GlyphLayout glayout;

    public OptionsMenuScreen(App app, Screen previous) {

        super(app, "demo_menu_options");
        this.previous = previous;
        bar     = Assets.get().getAtlas().findRegion("ui/volume_bar");
        barDark = Assets.get().getAtlas().findRegion("ui/volume_bar_dark");
        font = Assets.get().getFont("fonts/lionel8.fnt");
        glayout = new GlyphLayout();
    }

    @Override
    public void init() {

        stage = new Stage(viewport);

        settingsprefs = Gdx.app.getPreferences("settings");
        sfxVolume = Math.round(settingsprefs.getFloat("sfxvolume") * 20);
        musVolume = Math.round(settingsprefs.getFloat("musvolume") * 20);

        // sfx -
        float x = center.x - BUTTON_OFFSET;
        float y = center.y + 20;
        Button btnSFXMinus = new GraphicButton(this, x, y, Button.Size.SMALL, "ui/minus");
        btnSFXMinus.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsMenuScreen options = (OptionsMenuScreen) screen;
                options.sfxVolume--;
                if(options.sfxVolume < 0) options.sfxVolume = 0;
                Settings.sfxVolume = ((float) options.sfxVolume) * 0.05f;
            }
        });

        // sfx +
        x = center.x + BUTTON_OFFSET;
        Button btnSFXPlus = new GraphicButton(this, x, y, Button.Size.SMALL, "ui/plus");
        btnSFXPlus.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsMenuScreen options = (OptionsMenuScreen) screen;
                options.sfxVolume++;
                if(options.sfxVolume > 10) options.sfxVolume = 10;
                Settings.sfxVolume = ((float) options.sfxVolume) * 0.05f;
            }
        });

        // music -
        x = center.x - BUTTON_OFFSET;
        y = center.y - 20;
        Button btnMusicMinus = new GraphicButton(this, x, y, Button.Size.SMALL, "ui/minus");
        btnMusicMinus.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsMenuScreen options = (OptionsMenuScreen) screen;
                options.musVolume--;
                if(options.musVolume < 0) options.musVolume = 0;
                app.music.setVolume(musVolume * 0.01f);
            }
        });

        // music +
        x = center.x + BUTTON_OFFSET;
        y = center.y - 20;
        Button btnMusicPlus = new GraphicButton(this, x, y, Button.Size.SMALL, "ui/plus");
        btnMusicPlus.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                OptionsMenuScreen options = (OptionsMenuScreen) screen;
                options.musVolume++;
                if(options.musVolume > 10) options.musVolume = 10;
                app.music.setVolume(musVolume * 0.01f);
            }
        });

        // Go back to the previous screen.
        x = center.x - App.WIDTH / 2 + 20;
        y = center.y - App.HEIGHT / 2 + 20;
        Button btnBack = new GraphicButton(this, x, y, Button.Size.SMALL, "ui/arrow_left");
        btnBack.addListener(new ButtonChangeListener(this) {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                disableButtons();
                OptionsMenuScreen options = (OptionsMenuScreen) screen;

                settingsprefs.putFloat("sfxvolume", ((float) options.sfxVolume) * 0.05f);
                settingsprefs.putFloat("musvolume", ((float) options.musVolume) * 0.05f);

                settingsprefs.flush();

                Settings.sfxVolume = ((float) options.sfxVolume) * 0.05f;
                Settings.musVolume = ((float) options.musVolume) * 0.05f;
                screen.transitionToScreen(previous);
            }
        });

        buttons.add(btnSFXMinus);
        buttons.add(btnSFXPlus);
        buttons.add(btnMusicMinus);
        buttons.add(btnMusicPlus);
        buttons.add(btnBack);
        stage.addActor(btnSFXMinus);
        stage.addActor(btnSFXPlus);
        stage.addActor(btnMusicMinus);
        stage.addActor(btnMusicPlus);
        stage.addActor(btnBack);

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
        sb.begin();

            // Draw SFX bar.
            glayout.setText(font, "sfx");
            font.draw(sb, glayout, center.x - glayout.width / 2, center.y + 37);
            for(int i=0; i<MAX_VOLUME; i++) {
                float x = center.x + (-BUTTON_OFFSET + i * (bar.getRegionWidth() + 3)) + Button.WIDTH_SMALL;
                float y = center.y - bar.getRegionHeight() / 2 - 1 + 20;
                TextureRegion r = (i >= sfxVolume) ? (barDark) : (bar);
                sb.draw(r, x, y);
            }

            // Draw music volume bar.
            glayout.setText(font, "music");
            font.draw(sb, glayout, center.x - glayout.width / 2, center.y - 3);
            for(int i=0; i<MAX_VOLUME; i++) {
                float x = center.x + (-BUTTON_OFFSET + i * (bar.getRegionWidth() + 3)) + Button.WIDTH_SMALL;
                float y = center.y - bar.getRegionHeight() / 2 - 1 - 20;
                TextureRegion r = (i >= musVolume) ? (barDark) : (bar);
                sb.draw(r, x, y);
            }

        sb.end();
    }

}
