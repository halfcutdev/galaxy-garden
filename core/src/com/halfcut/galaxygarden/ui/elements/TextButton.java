package com.halfcut.galaxygarden.ui.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.screen.Screen;

/**
 * @author halfcutdev
 * @since 18/09/2017
 */
public class TextButton extends Button {

    final protected String text;
    protected BitmapFont font;
    protected GlyphLayout glayout;

    public TextButton(Screen screen, float x, float y, Size size, String text) {

        super(screen, x, y, size);

        this.text = text;
        font = Assets.get().getFont("fonts/lionel8.fnt");
        glayout = new GlyphLayout();
        glayout.setText(font, text);
    }

    @Override
    public void draw(Batch b, float parentAlpha) {

        super.draw(b, parentAlpha);

        float x = getX() + (getWidth() - glayout.width) / 2;
        float y = getY() + (getHeight() + glayout.height) / 2;
        font.setColor(Color.WHITE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.draw(b, text, x, y);
    }

    public String getText() {

        return this.text;
    }

}
