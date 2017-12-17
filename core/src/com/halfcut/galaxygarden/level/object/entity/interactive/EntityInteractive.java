package com.halfcut.galaxygarden.level.object.entity.interactive;

import com.halfcut.galaxygarden.level.object.entity.Entity;
import com.halfcut.galaxygarden.level.Level;

/**
 * @author halfcutdev
 * @since 14/09/2017
 */
public abstract class EntityInteractive extends Entity {

    abstract public void interact();

    public EntityInteractive(Level level, float x, float y, float width, float height) {

        super(level, x, y, width, height);
    }

}
