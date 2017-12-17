package com.halfcut.galaxygarden.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * @author halfcutdev
 * @since 10/09/2017
 */
public class Shader {

    static final public ShaderProgram _DEFAULT;
    static final public ShaderProgram _WHITE;

    static {
        ShaderProgram.pedantic = false;

        _DEFAULT = SpriteBatch.createDefaultShader();

        _WHITE = new ShaderProgram(
            Gdx.files.internal("shaders/white.vert").readString(),
            Gdx.files.internal("shaders/white.frag").readString()
        );

    }

}
