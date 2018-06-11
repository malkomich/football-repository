package com.malkomich.football.repository.league.service;

import com.malkomich.football.repository.league.domain.League;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.ProxyIgnore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;

@ProxyGen
public interface LeagueRepository {

    static LeagueRepository createProxy(final Vertx vertx, final String address) {
        return new LeagueRepositoryVertxEBProxy(vertx, address);
    }

    @Fluent
    LeagueRepository saveLeagues(final List<League> leagues, final Handler<AsyncResult<Void>> handler);

    @ProxyIgnore
    void close();
}
