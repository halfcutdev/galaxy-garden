package com.halfcut.galaxygarden.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.assets.Assets;

/**
 * @author halfcutdev
 * @since 18/09/2017
 */
public class Cursor {

    public enum CursorType { POINTER, CROSSHAIR, NONE }

    static public void set(CursorType newCursor) {

        if(App.mode == App.Mode.DESKTOP) {
            Texture cursor;
            Pixmap pm, pm2;
            switch(newCursor) {
                case POINTER:
                    cursor = Assets.get().getTexture("sprites/ui/cursor_pointer.png");
                    cursor.getTextureData().prepare();
                    pm = cursor.getTextureData().consumePixmap();
                    pm2 = new Pixmap(pm.getWidth(), pm.getHeight(), Pixmap.Format.RGBA8888);
                    pm2.drawPixmap(pm, 0, 0);
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm2, 0, 0));
                    pm.dispose();
                    pm2.dispose();
                    break;
                case CROSSHAIR:
                    cursor = Assets.get().getTexture("sprites/ui/cursor_crosshair.png");
                    cursor.getTextureData().prepare();
                    pm = cursor.getTextureData().consumePixmap();
                    pm2 = new Pixmap(pm.getWidth(), pm.getHeight(), Pixmap.Format.RGBA8888);
                    pm2.drawPixmap(pm, 0, 0);
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm2, 4, 4));
                    pm.dispose();
                    pm2.dispose();
                    break;
                case NONE:
                    pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
                    Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
                    pm.dispose();
                    break;
            }
        }
    }

}
