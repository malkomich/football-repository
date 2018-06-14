package com.malkomich.football.repository.configuration.vertx;

import com.google.common.base.Preconditions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuiceVertxDeploymentManager {
    private static final int MULTI_REACTOR_PATTERN_MULTIPLIER = 4;
    private static final int WORKER_POOL_SIZE = 4;

    private final Vertx vertx;

    public GuiceVertxDeploymentManager(final Vertx vertx) {
        this.vertx = Preconditions.checkNotNull(vertx);
    }

    public Future<Void> deployVerticle(final Class verticleClazz, final JsonObject config, final Boolean worker) {
        Future<Void> done = Future.future();
        Preconditions.checkNotNull(verticleClazz);
        this.vertx.deployVerticle(getFullVerticleName(verticleClazz), deploymentOptions(config, worker), result -> {
            if (!result.succeeded()) {
                log.info("Failed to deploy verticle: " + verticleClazz + result.cause());
                done.fail(result.cause());
                return;
            }
            log.info("Successfully deployed verticle: " + verticleClazz);
            done.complete();
        });
        return done;
    }

    private static String getFullVerticleName(final Class verticleClazz) {
        return GuiceVerticleFactory.GUICE_PREFIX
            .concat(":")
            .concat(verticleClazz.getCanonicalName());
    }

    private DeploymentOptions deploymentOptions(final JsonObject config, final Boolean worker) {
        return new DeploymentOptions()
            .setConfig(config)
            .setInstances(Runtime.getRuntime().availableProcessors() * MULTI_REACTOR_PATTERN_MULTIPLIER)
            .setWorker(worker)
            .setWorkerPoolSize(WORKER_POOL_SIZE);
    }
}
