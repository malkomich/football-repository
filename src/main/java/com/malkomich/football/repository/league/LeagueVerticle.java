package com.malkomich.football.repository.league;

import com.malkomich.football.repository.league.service.LeagueRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;

import javax.inject.Inject;

public class LeagueVerticle extends AbstractVerticle {

    public static final String QUEUE = "league.queue";

    @Inject
    private LeagueRepository repository;

    @Override
    public void start(final Future<Void> startFuture) {
        new ServiceBinder(vertx)
            .setAddress(QUEUE)
            .register(LeagueRepository.class, repository);
        startFuture.complete();
    }

    @Override
    public void stop() {
        repository.close();
    }
}
