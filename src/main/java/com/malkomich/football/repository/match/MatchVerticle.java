package com.malkomich.football.repository.match;

import com.malkomich.football.repository.match.service.MatchRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;

import javax.inject.Inject;

public class MatchVerticle extends AbstractVerticle {

    public static final String QUEUE = "match.queue";

    @Inject
    private MatchRepository repository;

    @Override
    public void start(final Future<Void> startFuture) {
        new ServiceBinder(vertx)
            .setAddress(QUEUE)
            .register(MatchRepository.class, repository);
        startFuture.complete();
    }

    @Override
    public void stop() {
        repository.close();
    }
}
