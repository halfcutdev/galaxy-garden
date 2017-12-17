package com.halfcut.galaxygarden.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.halfcut.galaxygarden.screen.game.GameScreen;
import com.halfcut.galaxygarden.util.Util;

import static com.halfcut.galaxygarden.util.Const.DELTA_SCALE;

/**
 * @author halfcutdev
 * @since 09/09/2017
 */
public class SmoothCamera extends OrthographicCamera {

    static final private float SMOOTHING       = 0.1f;
    static final private float MOUSE_INFLUENCE = 0.2f;

    private GameScreen game;
    private Vector2 playerPos;
    private Vector2 targetPos;
    private Vector2 lastPos;

    public SmoothCamera(GameScreen game, float x, float y, Vector2 playerPos) {
        this.game = game;

        position.x = x;
        position.y = y;
        lastPos = new Vector2(x, y);

        this.playerPos = playerPos;
        targetPos      = new Vector2(playerPos);
    }

    public void updatePosition(float delta) {
        Vector2 mouseOffset = new Vector2(
            game.mouseWorldPos().x - playerPos.x,
            game.mouseWorldPos().y - playerPos.y
        );

        targetPos = new Vector2(
            playerPos.x + mouseOffset.x * MOUSE_INFLUENCE,
            playerPos.y + mouseOffset.y * MOUSE_INFLUENCE
        );

        position.x = lastPos.x;
        position.y = lastPos.y;

        position.x += (targetPos.x - position.x) * SMOOTHING * delta * DELTA_SCALE;
        position.y += (targetPos.y - position.y) * SMOOTHING * delta * DELTA_SCALE;

        position.x = (Util.round(position.x, 2));
        position.y = (Util.round(position.y, 2));

        lastPos.x = position.x;
        lastPos.y = position.y;

        super.update();
    }

    public Vector2 getTargetPos() {

        return targetPos.cpy();
    }

    public Vector2 getLastPos() {

        return lastPos.cpy();
    }

}
