package com.halfcut.galaxygarden.ui;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.assets.Text;
import com.halfcut.galaxygarden.storage.Settings;
import com.halfcut.galaxygarden.util.Timer;

/**
 * @author halfcutdev
 * @since 18/09/2017
 */
public class DialogueBox {

    static final private int WRAP_WIDTH = 140;
    static final private int Y_OFFSET = 10;
    static final private int READ_TIME = 1500;
    static final private int LETTER_TIME = 50;
    static final private String FONT_REF = "fonts/lionel8.fnt";

    private Vector2 pos;
    private String[] text;
    private int index;
    private int charindex;

    private boolean reading;
    private Timer nextTimer;
    private Timer letterTimer;

    private BitmapFont font;
    private GlyphLayout glayout;
    private Sound sfxRead;
    private float volume;

    public DialogueBox(float x, float y, String ref) {
        this.pos = new Vector2(x, y);
        Text t = Assets.get().getText(ref);
        text = t.getString().split("\n");
        index = -1;

        nextTimer   = new Timer(READ_TIME, false);
        letterTimer = new Timer(LETTER_TIME, true);

        font = Assets.get().getFont(FONT_REF);
        glayout = new GlyphLayout();
        sfxRead = Assets.get().getSFX("sfx/mp3/terminal.mp3");
    }

    public void update(float delta) {
        if(reading) {
            if(letterTimer.tick(delta)) {
                sfxRead.play(Settings.sfxVolume * 0.1f * volume);
                if(charindex < text[index].length()){
                    charindex++;
                } else {
                    nextTimer.start();
                    letterTimer.stop();
                }
            }
            if(nextTimer.tick(delta)) {
                advance();
            }
        }
    }

    public void draw(SpriteBatch sb) {
        if(reading) {
            font.setColor(Color.WHITE);
            glayout.setText(font, text[index]);

            int width = (int) Math.min(glayout.width, WRAP_WIDTH);
            int x = (int) (pos.x - width / 2);
            int y = (int) (pos.y + Y_OFFSET + glayout.height * 6);

            // ensure text is centered
            if(glayout.width > WRAP_WIDTH) {
                font.draw(sb, text[index].substring(0, charindex), x, y, WRAP_WIDTH, 1, true);
            } else {
                int oldWidth = (int) glayout.width;
                glayout.setText(font, text[index].substring(0, charindex));
                font.draw(sb, text[index].substring(0, charindex), x + (oldWidth - glayout.width) / 2, y);
            }
        }
    }

    public void interact() {
        if(!reading) {
            // if not already reading, start reading
            reading = true;
            advance();
        } else {
            if(charindex < text[index].length()) {
                // if halfway through a message, skip to the end
                charindex = text[index].length();
            } else {
                // if at the end of a message, advance to the next one
                advance();
            }
        }
    }

    private void advance() {
        index++;
        charindex = 0;

        nextTimer.reset();
        nextTimer.stop();

        letterTimer.reset();
        letterTimer.start();

        if(index >= text.length) {
            index = -1;
            reading = false;
        }
    }

    public void setVolume(float volume) {

        this.volume = volume;
    }

}
