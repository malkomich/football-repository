package com.malkomich.football.repository.team.domain;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Management {
    private String manager;
    private String president;
    private Integer foundationYear;
    private Integer playersNumber;
    private Double annualBudget;

    public Management(final JsonObject json) {
        this.manager = json.getString("manager");
        this.president = json.getString("president");
        this.foundationYear = json.getInteger("foundationYear");
        this.playersNumber = json.getInteger("playersNumber");
        this.annualBudget = json.getDouble("annualBudget");
    }
}
