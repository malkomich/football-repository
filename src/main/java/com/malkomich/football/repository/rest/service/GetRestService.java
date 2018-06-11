package com.malkomich.football.repository.rest.service;

import io.vertx.core.eventbus.EventBus;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class GetRestService extends RestHandler {
    public static final String LEAGUES_PATH = "/leagues/";
    public static final String RANKS_PATH = "/ranks/";
    public static final String MACHES_PATH = "/matches/";
    public static final String TEAMS_PATH = "/teams/";

    private EventBus eventBus;

}
