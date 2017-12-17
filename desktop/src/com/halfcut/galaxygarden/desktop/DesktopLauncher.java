package com.halfcut.galaxygarden.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.halfcut.galaxygarden.App;
import com.halfcut.galaxygarden.storage.Settings;

import static com.halfcut.galaxygarden.App.*;

public class DesktopLauncher {

    public static void main (String[] arg) {

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.title  = TITLE;
        config.width  = WIDTH * SCALE;
        config.height = HEIGHT * SCALE;
        config.resizable = false;
        System.setProperty("org.lwjgl.opengl.Window.undecorated", String.valueOf(Settings.undecorated));

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 1024;
        settings.maxHeight = 1024;
        TexturePacker.process(settings, "sprites", "packed", "textures");

        App.mode = Mode.DESKTOP;

        new LwjglApplication(new App(), config);
    }


}
