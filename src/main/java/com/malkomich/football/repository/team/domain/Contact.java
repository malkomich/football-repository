package com.malkomich.football.repository.team.domain;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Contact {
    private String web;
    private String address;
    private String phone;

    public Contact(final JsonObject json) {
        this.web = json.getString("web");
        this.address = json.getString("address");
        this.phone = json.getString("phone");
    }
}
