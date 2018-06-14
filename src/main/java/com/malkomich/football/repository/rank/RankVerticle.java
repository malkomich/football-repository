package com.malkomich.football.repository.rank;

import com.malkomich.football.repository.rank.service.RankRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ServiceBinder;

import javax.inject.Inject;

public class RankVerticle extends AbstractVerticle {

    public static final String QUEUE = "rank.queue";

    @Inject
    private RankRepository repository;

    @Override
    public void start(final Future<Void> startFuture) {
        new ServiceBinder(vertx)
            .setAddress(QUEUE)
            .register(RankRepository.class, repository);
        startFuture.complete();
    }

    @Override
    public void stop() {
        repository.close();
    }
}
