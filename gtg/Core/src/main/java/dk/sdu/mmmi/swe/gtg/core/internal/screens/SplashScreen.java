package dk.sdu.mmmi.swe.gtg.core.internal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dk.sdu.mmmi.swe.gtg.core.internal.main.Game;


public class SplashScreen implements Screen {
    private final Game game;

    private final SpriteBatch batch;
    private final Texture splashTexture;
    private final Sprite sprite;
    private final float MAX_COUNT = 3f; // Seconds to display splash
    private float count = 0.0f;

    public SplashScreen(Game game) {
        super();
        this.game = game;
        this.batch = new SpriteBatch();
        this.splashTexture = new Texture("assets/splash_screen_new.png");
        this.sprite = new Sprite(splashTexture);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        count = count + v;
        if (count > MAX_COUNT) {
            this.game.setScreen(new MainMenuScreen(this.game));
            return;
        }

        batch.begin();
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        sprite.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        splashTexture.dispose();
        batch.dispose();
    }
}
