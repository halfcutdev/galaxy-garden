package com.halfcut.galaxygarden.level;

import com.halfcut.galaxygarden.screen.Screen;

/**
 * @author halfcutdev
 * @since 18/09/2017
 */
public class LevelDemo extends Level {

    public LevelDemo(Screen screen, String levelref, int nlevel) {
        super(screen, levelref, nlevel, true);
    }

    @Override
    public void update(float delta) {
        tileMap.update(delta);
        entrance.update(delta);
        exit.update(delta);
        updateAllEntities(delta);
    }

}