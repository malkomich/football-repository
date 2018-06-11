package com.malkomich.football.repository.match.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Getter;

@Getter
@DataObject
public class Match {
    private String date;
    private String localTeam;
    private String visitorTeam;
    private String result;
    private Integer localGoals;
    private Integer visitorGoals;
    private String endpoint;

    public Match(final JsonObject json) {
        this.date = json.getString("date");
        this.localTeam = json.getString("localTeam");
        this.visitorTeam = json.getString("visitorTeam");
        this.result = json.getString("result");
        this.localGoals = json.getInteger("localGoals");
        this.visitorGoals = json.getInteger("visitorGoals");
        this.endpoint = json.getString("endpoint");
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
