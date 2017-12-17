package com.halfcut.galaxygarden.ui;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.halfcut.galaxygarden.screen.Screen;

/**
 * @author halfcutdev
 * @since 13/11/2017
 */
public abstract class ButtonChangeListener extends ChangeListener {

    public Screen screen;

    protected ButtonChangeListener(Screen screen) {

        this.screen = screen;
    }

}
