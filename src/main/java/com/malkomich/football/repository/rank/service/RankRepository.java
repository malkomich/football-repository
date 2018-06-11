package com.malkomich.football.repository.rank.service;

import com.malkomich.football.repository.rank.domain.Rank;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.ProxyIgnore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

import java.util.List;

@ProxyGen
public interface RankRepository {

    static RankRepository createProxy(final Vertx vertx, final String address) {
        return new RankRepositoryVertxEBProxy(vertx, address);
    }

    @Fluent
    RankRepository saveRanks(final List<Rank> ranks, final Handler<AsyncResult<Void>> handler);

    @ProxyIgnore
    void close();
}
