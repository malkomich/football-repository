package com.malkomich.football.repository.match.service;

import com.malkomich.football.repository.match.domain.Match;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.ProxyIgnore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;

@ProxyGen
public interface MatchRepository {

    static MatchRepository createProxy(final Vertx vertx, final String address) {
        return new MatchRepositoryVertxEBProxy(vertx, address);
    }

    @Fluent
    MatchRepository saveMatches(final List<Match> matches, final Handler<AsyncResult<Void>> handler);

    @ProxyIgnore
    void close();
}
