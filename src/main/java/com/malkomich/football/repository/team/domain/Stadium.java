package com.malkomich.football.repository.team.domain;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Stadium {
    private String stadiumName;
    private Integer stadiumCapacity;
    private Integer stadiumBuild;

    public Stadium(final JsonObject json) {
        this.stadiumName = json.getString("stadiumName");
        this.stadiumCapacity = json.getInteger("stadiumCapacity");
        this.stadiumBuild = json.getInteger("stadiumBuild");
    }
}
