package com.malkomich.football.repository.team.service;

import com.malkomich.football.repository.team.domain.Team;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.ProxyIgnore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;

@ProxyGen
public interface TeamRepository {

    static TeamRepository createProxy(final Vertx vertx, final String address) {
        return new TeamRepositoryVertxEBProxy(vertx, address);
    }

    @Fluent
    TeamRepository saveTeams(final List<Team> teams, final Handler<AsyncResult<Void>> handler);

    @ProxyIgnore
    void close();
}
