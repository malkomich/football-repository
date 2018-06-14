package com.malkomich.football.repository.rank.service;

import com.malkomich.football.repository.rank.domain.Rank;
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
public class RankMongoRepository implements RankRepository {
    private static final String COLLECTION = "ranks";

    private static final String FIELD_TEAM = "team";

    private MongoClient client;

    @Override
    public RankRepository saveRanks(final List<Rank> ranks, final Handler<AsyncResult<Void>> handler) {
        val bulkOperations = updateOperations(ranks);
        client.bulkWrite(COLLECTION, bulkOperations, asyncResult -> {
            if (asyncResult.failed()) {
                log.error("Bulk operations failed");
                handler.handle(Future.failedFuture(asyncResult.cause()));
                return;
            }
            log.info("Ranks inserted/updated");
            handler.handle(Future.succeededFuture());
        });
        return this;
    }

    private List<BulkOperation> updateOperations(final List<Rank> ranks) {
        return ranks.stream().map(rank ->
            BulkOperation.createUpdate(
                findOneCode(rank.getTeam()),
                updateFields(rank),
                true,
                false))
            .collect(Collectors.toList());
    }

    private JsonObject findOneCode(final String name) {
        return new JsonObject().put(FIELD_TEAM, name);
    }

    private JsonObject updateFields(final Rank rank) {
        return new JsonObject().put("$set", rank.toJson());
    }

    @Override
    public void close() {
        client.close();
    }
}
