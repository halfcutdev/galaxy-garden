package com.halfcut.galaxygarden.screen.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.level.Level;
import com.halfcut.galaxygarden.level.LevelDemo;
import com.halfcut.galaxygarden.level.map.TileMap;
import com.halfcut.galaxygarden.screen.Screen;
import com.halfcut.galaxygarden.ui.Cursor;
import com.halfcut.galaxygarden.assets.Assets;
import com.halfcut.galaxygarden.ui.elements.Button;

import java.util.ArrayList;
import java.util.List;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @date 12/09/17
 */
public class MenuScreen extends Screen {

    protected Stage stage;
    protected TextureRegion background;
    protected float backgroundx;
    protected Level level;
    protected Vector2 center;

    protected List<Button> buttons;

    public MenuScreen(App app, String levelref) {
        super(app);

        level = new LevelDemo(this, levelref, -1);
        float cx = (level.getTileMap().width  * TileMap.TILE_SIZE / 2);
        float cy = (level.getTileMap().height * TileMap.TILE_SIZE / 2);
        center = new Vector2(cx, cy);
        background = Assets.get().getAtlas().findRegion("world/background");
        buttons = new ArrayList<Button>();
    }

    public MenuScreen(App app, Level level) {
        super(app);
        this.level = level;
        float cx = (level.getTileMap().width  * TileMap.TILE_SIZE / 2);
        float cy = (level.getTileMap().height * TileMap.TILE_SIZE / 2);
        center = new Vector2(cx, cy);
        background = Assets.get().getAtlas().findRegion("world/background");
    }

    @Override
    public void init() {
        Cursor.set(Cursor.CursorType.POINTER);
    }

    @Override
    public void update(float delta) {
        camera.update();

        stage.act(delta);
        backgroundx -= 0.3f * delta * DELTA_SCALE;
        if(backgroundx <= -background.getRegionWidth()) backgroundx = 0;

        level.update(delta);
    }

    @Override
    public void draw(SpriteBatch sb, ShapeRenderer sr) {
        sb.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        sb.begin();

            sb.draw(background, backgroundx, 0);
            sb.draw(background, backgroundx+background.getRegionWidth(), 0);
            level.draw(sb, sr);

            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.BLACK.cpy().sub(0, 0, 0, 0.6f));
            sr.rect(0, 0,  App.WIDTH * App.SCALE, App.HEIGHT * App.SCALE);
            sr.end();

            stage.getBatch().setProjectionMatrix(camera.combined);
            stage.draw();

        sb.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        stage.dispose();
    }

    public void enableButtons() {
        for(Button button : buttons) {
            button.setDisabled(false);
        }
    }

    protected void disableButtons() {
        for(Button button : buttons) {
            button.setDisabled(true);
        }
    }

}
