package dk.sdu.mmmi.swe.gtg.core.internal.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dk.sdu.mmmi.swe.gtg.common.data.GameData;
import dk.sdu.mmmi.swe.gtg.common.services.managers.IEngine;
import dk.sdu.mmmi.swe.gtg.common.services.plugin.IPlugin;
import dk.sdu.mmmi.swe.gtg.core.internal.managers.ScreenManager;
import dk.sdu.mmmi.swe.gtg.core.internal.screens.SplashScreen;
import dk.sdu.mmmi.swe.gtg.worldmanager.services.IWorldManager;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true)
public class Game extends com.badlogic.gdx.Game implements ApplicationListener {
    public final GameData gameData = new GameData();

    private final List<IPlugin> entityPlugins = new CopyOnWriteArrayList<>();
    private final List<IPlugin> pluginsToBeInstalled = new CopyOnWriteArrayList<>();
    private final List<IPlugin> pluginsToBeUninstalled = new CopyOnWriteArrayList<>();

    @Reference
    private IEngine engine;

    @Reference
    private IWorldManager worldManager;

    @Reference
    private ScreenManager screenManager;

    public Game() {
        System.out.println("Game created");
        init();
    }

    public void init() {
        LwjglApplicationConfiguration cfg =
                new LwjglApplicationConfiguration();
        cfg.title = "Grand Theft Gørding";
        cfg.width = 1600;
        cfg.height = 900;
        cfg.resizable = false;

        new LwjglApplication(this, cfg);
    }

    @Override
    public void create() {
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());

        screenManager.setScreen(SplashScreen.class);
    }

    @Override
    public void render() {
        super.render();

        pluginsToBeInstalled.forEach(plugin -> plugin.install(engine, gameData));
        pluginsToBeInstalled.clear();

        pluginsToBeUninstalled.forEach(plugin -> plugin.uninstall(engine, gameData));
        pluginsToBeUninstalled.clear();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    @Override
    public void dispose() {
    }

    private Collection<? extends IPlugin> getPluginServices() {
        return entityPlugins;
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addGamePluginService(IPlugin plugin) {
        this.entityPlugins.add(plugin);
        this.pluginsToBeInstalled.add(plugin);
    }

    public void removeGamePluginService(IPlugin plugin) {
        this.entityPlugins.remove(plugin);
        this.pluginsToBeUninstalled.add(plugin);
    }

    public IEngine getEngine() {
        return engine;
    }

    public void setEngine(IEngine engine) {
        this.engine = engine;
    }

    public void removeEngine(IEngine systemManager) {
        this.engine = null;
    }

    public IWorldManager getWorldManager() {
        return worldManager;
    }

    public void setWorldManager(IWorldManager worldManager) {
        this.worldManager = worldManager;
    }

    public void removeWorldManager() {
        this.worldManager = null;
    }
}
