package com.malkomich.football.repository.league.service;

import com.malkomich.football.repository.league.domain.League;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.BulkOperation;
import io.vertx.ext.mongo.MongoClient;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Builder
public class LeagueMongoRepository implements LeagueRepository {
    private static final String COLLECTION = "leagues";

    private static final String FIELD_NAME = "name";

    private MongoClient client;

    @Override
    public LeagueRepository saveLeagues(final List<League> leagues, final Handler<AsyncResult<Void>> handler) {
        val bulkOperations = updateOperations(leagues);
        client.bulkWrite(COLLECTION, bulkOperations, asyncResult -> {
            if (asyncResult.failed()) {
                log.error("Bulk operations failed");
                handler.handle(Future.failedFuture(asyncResult.cause()));
                return;
            }
            log.info("Leagues inserted/updated");
            handler.handle(Future.succeededFuture());
        });
        return this;
    }

    private List<BulkOperation> updateOperations(final List<League> leagues) {
        return leagues.stream().map(league ->
            BulkOperation.createUpdate(
                findOneCode(league.getName()),
                updateFields(league),
                true,
                false))
            .collect(Collectors.toList());
    }

    private JsonObject findOneCode(final String name) {
        return new JsonObject().put(FIELD_NAME, name);
    }

    private JsonObject updateFields(final League league) {
        return new JsonObject().put("$set", league.toJson());
    }

    @Override
    public void close() {
        client.close();
    }
}
