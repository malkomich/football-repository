package com.malkomich.football.repository.rank.domain;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import lombok.Getter;

@Getter
@DataObject
public class Rank {
    private String team;
    private Integer position;
    private Integer score;
    private Integer playedMatches;
    private Integer wonMatches;
    private Integer drawMatches;
    private Integer lostMatches;
    private Integer goalsInFavour;
    private Integer goalsAgainst;

    public Rank(final JsonObject json) {
        this.team = json.getString("team");
        this.position = json.getInteger("position");
        this.score = json.getInteger("score");
        this.playedMatches = json.getInteger("playedMatches");
        this.wonMatches = json.getInteger("wonMatches");
        this.drawMatches = json.getInteger("drawMatches");
        this.lostMatches = json.getInteger("lostMatches");
        this.goalsInFavour = json.getInteger("goalsInFavour");
        this.goalsAgainst = json.getInteger("goalsAgainst");
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
