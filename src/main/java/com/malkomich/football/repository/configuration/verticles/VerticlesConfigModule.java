package com.malkomich.football.repository.configuration.verticles;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.malkomich.football.repository.league.LeagueVerticle;
import com.malkomich.football.repository.league.service.LeagueMongoRepository;
import com.malkomich.football.repository.league.service.LeagueRepository;
import com.malkomich.football.repository.match.MatchVerticle;
import com.malkomich.football.repository.match.service.MatchMongoRepository;
import com.malkomich.football.repository.match.service.MatchRepository;
import com.malkomich.football.repository.rank.RankVerticle;
import com.malkomich.football.repository.rank.service.RankMongoRepository;
import com.malkomich.football.repository.rank.service.RankRepository;
import com.malkomich.football.repository.team.TeamVerticle;
import com.malkomich.football.repository.team.service.TeamMongoRepository;
import com.malkomich.football.repository.team.service.TeamRepository;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
public class VerticlesConfigModule extends AbstractModule {

    private static final String MONGO = "MONGO";

    private Vertx vertx;
    private JsonObject config;

    @Override
    protected void configure() {
        System.setProperty("org.mongodb.async.type", "netty");
        bind(MongoClient.class).toInstance(mongoClient());
    }

    @Singleton
    @Provides
    public LeagueRepository leagueRepository(final MongoClient mongoClient) {
        LeagueRepository.createProxy(vertx, LeagueVerticle.QUEUE);
        return LeagueMongoRepository.builder()
            .client(mongoClient)
            .build();
    }

    @Singleton
    @Provides
    public RankRepository rankRepository(final MongoClient mongoClient) {
        RankRepository.createProxy(vertx, RankVerticle.QUEUE);
        return RankMongoRepository.builder()
            .client(mongoClient)
            .build();
    }

    @Singleton
    @Provides
    public TeamRepository teamRepository(final MongoClient mongoClient) {
        TeamRepository.createProxy(vertx, TeamVerticle.QUEUE);
        return TeamMongoRepository.builder()
            .client(mongoClient)
            .build();
    }

    @Singleton
    @Provides
    public MatchRepository matchRepository(final MongoClient mongoClient) {
        MatchRepository.createProxy(vertx, MatchVerticle.QUEUE);
        return MatchMongoRepository.builder()
            .client(mongoClient)
            .build();
    }

    private MongoClient mongoClient() {
        val mongoConfig = new JsonObject().put("connection_string", config.getString(MONGO));
        return MongoClient.createShared(vertx, mongoConfig);
    }
}
