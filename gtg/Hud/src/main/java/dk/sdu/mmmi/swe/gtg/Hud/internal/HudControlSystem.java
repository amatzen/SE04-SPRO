package dk.sdu.mmmi.swe.gtg.Hud.internal;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dk.sdu.mmmi.swe.gtg.common.data.GameData;
import dk.sdu.mmmi.swe.gtg.common.services.entity.IEntityProcessingService;
import dk.sdu.mmmi.swe.gtg.common.services.entity.IPostEntityProcessingService;
import dk.sdu.mmmi.swe.gtg.common.services.managers.IEngine;
import org.osgi.service.component.annotations.Component;

@Component
public class HudControlSystem implements IPostEntityProcessingService {

    // Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    // score/time Tracking Variables
    private float bullets;
    private float life;
    private float money;

    // Scene2D widgets
    private Label showBullets;
    private static Label showMoney;
    private Label bulletLabel;
    private Label showLives;
    private Label lifeLabel;
    private Label moneyLabel;

    public static final int V_WIDTH = 1600;
    public static final int V_HEIGHT = 900;
    public SpriteBatch sb;

    @Override
    public void addedToEngine(IEngine engine) {
        sb = new SpriteBatch();

        // Define our tracking variables
        life = 3;
        money = 0;
        bullets = 0;

        // Setup the HUD viewport using a new camera seperate from our gamecam
        viewport = new FitViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera());

        // Define our stage using that viewport and our games spritebatch
        stage = new Stage(viewport, sb);

        // Define a table used to organize our hud's labels
        Table table = new Table();

        // Top-Align table
        table.top();

        // Make the table fill the entire stage
        table.setFillParent(true);

        // Define our labels using the String, and a Label style consisting of a font and color
        moneyLabel = new Label("MONEY", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        showMoney = new Label(String.format("%06d", money), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        
        bulletLabel = new Label("BULLETS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        showBullets = new Label(String.format("%03d", bullets), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        
        lifeLabel = new Label("LIVES", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        showLives = new Label("3", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        // Add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(moneyLabel).expandX().padTop(10);
        table.add(lifeLabel).expandX().padTop(10);
        table.add(bulletLabel).expandX().padTop(10);

        // Creates a row under
        table.row();

        // Add a second row to our table
        table.add(showMoney).expandX();
        table.add(showLives).expandX();
        table.add(showBullets).expandX();

        // Add our table to the stage
        stage.addActor(table);

    }

    @Override
    public void process(GameData gameData) {
        sb.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
    }

    public void addScore(int value){
        money += value;
        showMoney.setText(String.format("%06d", money));
    }
}
