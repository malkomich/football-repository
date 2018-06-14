package com.malkomich.football.repository.team;

import com.malkomich.football.repository.team.service.TeamRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;

import javax.inject.Inject;

public class TeamVerticle extends AbstractVerticle {

    public static final String QUEUE = "team.queue";

    @Inject
    private TeamRepository repository;

    @Override
    public void start(final Future<Void> startFuture) {
        new ServiceBinder(vertx)
            .setAddress(QUEUE)
            .register(TeamRepository.class, repository);
        startFuture.complete();
    }

    @Override
    public void stop() {
        repository.close();
    }
}
