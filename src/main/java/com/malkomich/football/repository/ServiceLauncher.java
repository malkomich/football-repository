package com.malkomich.football.repository;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.malkomich.football.repository.configuration.mapper.MapperConfigModule;
import com.malkomich.football.repository.configuration.properties.PropertiesConfig;
import com.malkomich.football.repository.configuration.properties.PropertiesConfigModule;
import com.malkomich.football.repository.configuration.verticles.VerticlesConfigModule;
import com.malkomich.football.repository.configuration.vertx.GuiceVerticleFactory;
import com.malkomich.football.repository.configuration.vertx.GuiceVertxDeploymentManager;
import com.malkomich.football.repository.league.LeagueVerticle;
import com.malkomich.football.repository.match.MatchVerticle;
import com.malkomich.football.repository.rank.RankVerticle;
import com.malkomich.football.repository.rest.HttpVerticle;
import com.malkomich.football.repository.team.TeamVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class ServiceLauncher {

    @Inject
    private PropertiesConfig propertiesConfig;
    private Vertx vertx;

    public static void main(final String[] args) {
        new ServiceLauncher().launch();
    }

    private void launch() {
        vertx = Vertx.vertx();

        Guice.createInjector(new PropertiesConfigModule(vertx))
            .injectMembers(this);

        propertiesConfig.config(this::injectAndDeployVerticles);
    }

    private void injectAndDeployVerticles(final AsyncResult<JsonObject> asyncResult) {
        final Future<Void> done = Future.future();

        final Injector injector = dependencyModulesInjector(asyncResult.result());
        final GuiceVerticleFactory guiceVerticleFactory = new GuiceVerticleFactory(injector);
        vertx.registerVerticleFactory(guiceVerticleFactory);

        compositeFuture(asyncResult.result(), done);
    }

    private Injector dependencyModulesInjector(final JsonObject config) {
        return Guice.createInjector(
            new MapperConfigModule(),
            new VerticlesConfigModule(vertx, config)
        );
    }

    private void compositeFuture(final JsonObject config, final Future<Void> done) {
        generateCompositeFuture(config).setHandler(asyncResult -> {
            if (!asyncResult.succeeded()) {
                done.fail(asyncResult.cause());
                return;
            }
            done.complete();
        });
    }

    private CompositeFuture generateCompositeFuture(final JsonObject config) {
        GuiceVertxDeploymentManager deploymentManager = new GuiceVertxDeploymentManager(vertx);

        return CompositeFuture.all(
            deploymentManager.deployVerticle(HttpVerticle.class, config, false),
            deploymentManager.deployVerticle(LeagueVerticle.class, config, true),
            deploymentManager.deployVerticle(RankVerticle.class, config, true),
            deploymentManager.deployVerticle(MatchVerticle.class, config, true),
            deploymentManager.deployVerticle(TeamVerticle.class, config, true)
        );
    }
}
