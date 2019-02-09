package com.decision.engines.exporter.event;

import com.decision.engines.exporter.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

public class UserEvent extends ApplicationEvent {

    private User user;
    private UUID id;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public UserEvent(Object source, User user, UUID id) {
        super(source);
        this.user = user;
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public UUID getId() {
        return id;
    }
}
