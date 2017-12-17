package com.halfcut.galaxygarden.level.object.entity.misc;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.halfcut.galaxygarden.level.object.entity.interactive.EntityInteractive;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.ui.DialogueBox;

/**
 * @author halfcutdev
 * @since 16/09/2017
 */
public class Terminal extends EntityInteractive {

    static final public float WIDTH  = 10;
    static final public float HEIGHT = 11;
    static final private String SPRITE_REF_UNREAD = "entity/terminal_unread";
    static final private String SPRITE_REF_READ   = "entity/terminal_read";
    static final private float SOUND_DISTANCE = 120;

    private Sprite spriteUnread;
    private Sprite spriteRead;
    private DialogueBox dialogue;
    private boolean read;
    private boolean volume;

    public Terminal(Level level, float x, float y, boolean flip, String ref) {

        super(level, x, y, WIDTH, HEIGHT);

        dialogue = new DialogueBox(getCenter().x, getCenter().y, ref);

        spriteUnread = new Sprite(Assets.get().getAtlas().findRegion(SPRITE_REF_UNREAD));
        spriteUnread.setPosition((int) x, (int) y);
        spriteUnread.setFlip(flip, false);

        spriteRead = new Sprite(Assets.get().getAtlas().findRegion(SPRITE_REF_READ));
        spriteRead.setPosition((int) x, (int) y);
        spriteRead.setFlip(flip, false);
    }

    @Override
    public boolean update(float delta) {

        dialogue.update(delta);

        float distance = level.getPlayer().pos.cpy().sub(getCenter()).len();
        float volume;
        if(distance > SOUND_DISTANCE) volume = 0;
        else                          volume = 1 - distance / SOUND_DISTANCE;
        dialogue.setVolume(volume);

        return super.update(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {

        if(read) spriteRead.draw(sb);
        else     spriteUnread.draw(sb);
        dialogue.draw(sb);
        super.draw(sb, sr);
    }

    @Override
    public void interact() {

        dialogue.interact();
        read = true;
    }

}
