package org.fountainmc.plugin;

import net.techcable.event4j.EventBus;
import net.techcable.event4j.EventExecutor;
import org.fountainmc.api.event.Event;
import org.fountainmc.api.event.EventManager;

public class WetEventManager implements EventManager {

    EventBus<Event, Object> eventBus;

    public WetEventManager() {
        eventBus = EventBus.builder().eventClass(Event.class)
                .executorFactory(EventExecutor.Factory.ASM_LISTENER_FACTORY.get()).build();
    }

    @Override
    public void fire(Event event) {
        eventBus.fire(event);
    }

    @Override
    public void registerListener(Object listener) {
        eventBus.register(listener);
    }

}
