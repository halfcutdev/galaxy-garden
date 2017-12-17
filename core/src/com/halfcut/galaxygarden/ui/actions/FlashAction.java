package com.halfcut.galaxygarden.ui.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

/**
 * @author halfcutdev
 * @since 21/09/2017
 */
public class FlashAction extends SequenceAction {

    public FlashAction() {

        for(int i=0; i<2; i++) {
            ColorAction action = new ColorAction();
            Color c = Color.WHITE.cpy();
            c.a = (i%2 == 0) ? 0 : 1;
            action.setEndColor(c);
            action.setDuration(0.075f);
            this.addAction(action);
        }
    }

}
