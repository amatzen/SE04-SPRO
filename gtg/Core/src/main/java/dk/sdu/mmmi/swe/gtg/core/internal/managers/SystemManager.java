package dk.sdu.mmmi.swe.gtg.core.internal.managers;

import dk.sdu.mmmi.swe.gtg.common.data.GameData;
import dk.sdu.mmmi.swe.gtg.common.services.entity.IEntitySystem;
import dk.sdu.mmmi.swe.gtg.common.services.entity.IPostProcessingSystem;
import dk.sdu.mmmi.swe.gtg.common.services.entity.IProcessingSystem;
import dk.sdu.mmmi.swe.gtg.common.services.managers.ISystemManager;
import org.osgi.service.component.annotations.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SystemManager implements ISystemManager {

    private final List<IProcessingSystem> entityProcessors;
    private final List<IPostProcessingSystem> entityPostProcessors;

    public SystemManager() {
        entityProcessors = new CopyOnWriteArrayList<>();
        entityPostProcessors = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addEntityProcessingService(IProcessingSystem service) {
        this.entityProcessors.add(service);
    }

    @Override
    public void removeEntityProcessingService(IProcessingSystem service) {
        this.entityProcessors.remove(service);
    }

    @Override
    public void addPostEntityProcessingService(IPostProcessingSystem service) {
        this.entityPostProcessors.add(service);
    }

    @Override
    public void removePostEntityProcessingService(IPostProcessingSystem service) {
        this.entityPostProcessors.remove(service);
    }

    @Override
    public void update(GameData gameData) {
        getEntityProcessingServices().forEach(entityProcessor -> {
            entityProcessor.process(gameData);
        });

        getPostEntityProcessingServices().forEach(entityPostProcessor -> {
            entityPostProcessor.process(gameData);
        });
    }

    @Override
    public void reset() {
        for (IEntitySystem system : entityProcessors) {
            system.addedToEngine();
        }

        for (IEntitySystem system : entityPostProcessors) {
            system.addedToEngine();
        }
    }

    private Collection<? extends IProcessingSystem> getEntityProcessingServices() {
        return entityProcessors;
    }

    private Collection<? extends IPostProcessingSystem> getPostEntityProcessingServices() {
        return entityPostProcessors;
    }
}
