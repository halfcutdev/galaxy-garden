package com.halfcut.galaxygarden.ui.elements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.screen.Screen;

/**
 * @author halfcutdev
 * @since 18/09/2017
 */
public class GraphicButton extends Button {

    private Sprite sprite;

    public GraphicButton(Screen screen, float x, float y, Size size, String ref) {

        super(screen, x, y, size);
        sprite = Assets.get().getAtlas().createSprite(ref);
        sprite.setPosition((int) (x - sprite.getWidth() / 2), (int) (y - sprite.getHeight() / 2));
    }

    @Override
    public void draw(Batch b, float parentAlpha) {

        super.draw(b, parentAlpha);
        sprite.draw(b);
    }

}
