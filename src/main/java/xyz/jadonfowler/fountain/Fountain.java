package xyz.jadonfowler.fountain;

import java.io.File;
import net.minecraft.server.MinecraftServer;
import net.techcable.event4j.EventBus;
import net.techcable.event4j.EventExecutor;
import xyz.jadonfowler.fountain.api.event.Event;
import xyz.jadonfowler.fountain.api.event.Listener;

public class Fountain {

    private static Fountain instance;

    private EventBus<Event, Listener> eventBus;
    private PluginManager pluginManager;

    private Fountain() {
        eventBus = EventBus.builder().eventClass(Event.class).listenerClass(Listener.class)
                .executorFactory(EventExecutor.Factory.ASM_LISTENER_FACTORY.get()).build();
        pluginManager = new PluginManager();
        pluginManager.loadPlugins(new File("plugins"));
    }

    public Fountain getInstance() {
        return instance;
    }

    public void registerEvents(Listener listener) {
        eventBus.register(listener);
    }

    public static void main(String[] args) {
        instance = new Fountain();
        MinecraftServer.main(args);
    }

}
