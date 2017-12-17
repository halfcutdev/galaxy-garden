package com.halfcut.galaxygarden.ui.elements;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.screen.Screen;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.ui.actions.FlashAction;
import com.halfcut.galaxygarden.util.Palette;

/**
 * @author halfcutdev
 * @date 12/09/17
 */
public class ButtonListener extends InputListener {

    private Screen screen;
    private Button btn;

    protected boolean clicked;
    protected Sound sfxHover;
    protected Sound sfxSelect;

    public ButtonListener(Screen screen, Button btn) {

        this.screen = screen;
        this.btn = btn;
        sfxHover  = Assets.get().getSFX("sfx/mp3/blip.mp3");
        sfxSelect = Assets.get().getSFX("sfx/mp3/blip2.mp3");
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

        clicked = true;
        sfxHover.stop();
        sfxSelect.play(Settings.sfxVolume * 0.2f);
        btn.addAction(new FlashAction());
        return super.touchDown(event, x, y, pointer, button);
    }

    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

        btn.setMouseOver(true);
        btn.setColor(Palette.WHITE);
        sfxHover.stop();
        sfxHover.play(Settings.sfxVolume * 0.2f);
    }

    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {

        if(!clicked) {
            btn.setMouseOver(false);
            btn.setColor(Palette.IRON);
        } else {
            clicked = false;
        }
    }

}
