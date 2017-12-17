package com.halfcut.galaxygarden.ui.elements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.screen.Screen;
import com.halfcut.galaxygarden.util.Palette;
import com.halfcut.galaxygarden.util.Timer;

/**
 * @author halfcutdev
 * @date 12/09/17
 */
public class Button extends com.badlogic.gdx.scenes.scene2d.ui.Button {

    static final public int WIDTH_SMALL   = 14;
    static final public int HEIGHT_SMALL  = 14;
    static final public int WIDTH_LARGE   = 60;
    static final public int HEIGHT_LARGE  = 10;

    public enum Size { SMALL, LARGE }

    protected Sprite sprite;
    protected Sprite outline;
    protected boolean mouseOver;

    public Button(Screen screen, float x, float y, Size size) {

        if(size == Size.SMALL) {
            sprite = Assets.get().getAtlas().createSprite("ui/button_small");
            outline = Assets.get().getAtlas().createSprite("ui/button_small_outline");
        }
        if(size == Size.LARGE) {
            sprite = Assets.get().getAtlas().createSprite("ui/button_large");
            outline = Assets.get().getAtlas().createSprite("ui/button_large_outline");
        }

        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight() / 2);
        outline.setPosition(x - outline.getWidth() / 2, y - outline.getHeight() / 2);

        setBounds(outline.getX(), outline.getY(), outline.getWidth(), outline.getHeight());
        setTouchable(Touchable.enabled);
        setPosition(outline.getX(), outline.getY());
        setColor(Palette.IRON);

        this.addListener(new ButtonListener(screen, this));
    }

    @Override
    public void act(float delta) {

        super.act(delta);
    }

    @Override
    public void draw(Batch b, float parentAlpha) {

        sprite.setColor(getColor());
        sprite.draw(b);
        if(mouseOver) outline.draw(b);
    }

    public void setMouseOver(boolean mouseOver) {

        this.mouseOver = mouseOver;
    }

    public boolean isMouseOver() {

        return this.mouseOver;
    }

}
