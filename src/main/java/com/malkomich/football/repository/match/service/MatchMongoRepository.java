package com.malkomich.football.repository.match.service;

import com.malkomich.football.repository.match.domain.Match;
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
public class MatchMongoRepository implements MatchRepository {
    private static final String COLLECTION = "matches";

    private static final String FIELD_LOCAL_TEAM = "localTeam";
    private static final String FIELD_VISITOR_TEAM = "visitorTeam";
    private static final String FIELD_DATE = "date";

    private MongoClient client;

    @Override
    public MatchRepository saveMatches(final List<Match> matches, final Handler<AsyncResult<Void>> handler) {
        val bulkOperations = updateOperations(matches);
        client.bulkWrite(COLLECTION, bulkOperations, asyncResult -> {
            if (asyncResult.failed()) {
                log.error("Bulk operations failed");
                handler.handle(Future.failedFuture(asyncResult.cause()));
                return;
            }
            log.info("Matches inserted/updated");
            handler.handle(Future.succeededFuture());
        });
        return this;
    }

    private List<BulkOperation> updateOperations(final List<Match> matches) {
        return matches.stream().map(match ->
            BulkOperation.createUpdate(
                findMatch(match),
                updateFields(match),
                true,
                false))
            .collect(Collectors.toList());
    }

    private JsonObject findMatch(final Match match) {
        return new JsonObject()
            .put(FIELD_LOCAL_TEAM, match.getLocalTeam())
            .put(FIELD_VISITOR_TEAM, match.getVisitorGoals())
            .put(FIELD_DATE, match.getDate());
    }

    private JsonObject updateFields(final Match match) {
        return new JsonObject().put("$set", match.toJson());
    }

    @Override
    public void close() {
        client.close();
    }
}
