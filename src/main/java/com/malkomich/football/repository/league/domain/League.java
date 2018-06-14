package com.malkomich.football.repository.league.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Getter;

@Getter
@DataObject
public class League {
    private String name;
    private String url;

    public League(final JsonObject json) {
        this.name = json.getString("name");
        this.url = json.getString("url");
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
