package com.halfcut.galaxygarden.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

/**
 * @author halfcutdev
 * @since 16/09/2017
 */
public class TextLoader extends AsynchronousAssetLoader<Text, TextLoader.TextParameter> {

    private Text text;

    public TextLoader(FileHandleResolver resolver) {

        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextParameter parameter) {

        this.text = null;
        this.text = new Text(file);
    }

    @Override
    public Text loadSync(AssetManager manager, String fileName, FileHandle file, TextParameter parameter) {

        Text text = this.text;
        this.text = null;
        return text;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextParameter parameter) {

        return null;
    }

    static class TextParameter extends AssetLoaderParameters<Text> {

    }

}