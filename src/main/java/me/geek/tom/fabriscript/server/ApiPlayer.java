package me.geek.tom.fabriscript.server;

import java.util.UUID;

public class ApiPlayer {
    private final UUID uuid;
    private final String name;

    public ApiPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }
}
