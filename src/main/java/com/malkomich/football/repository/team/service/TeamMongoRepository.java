package com.malkomich.football.repository.team.service;

import com.malkomich.football.repository.team.domain.Team;
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
public class TeamMongoRepository implements TeamRepository {
    private static final String COLLECTION = "teams";

    private static final String FIELD_NAME = "name";

    private MongoClient client;

    @Override
    public TeamRepository saveTeams(final List<Team> teams, final Handler<AsyncResult<Void>> handler) {
        val bulkOperations = updateOperations(teams);
        client.bulkWrite(COLLECTION, bulkOperations, asyncResult -> {
            if (asyncResult.failed()) {
                log.error("Bulk operations failed");
                handler.handle(Future.failedFuture(asyncResult.cause()));
                return;
            }
            log.info("Teams inserted/updated");
            handler.handle(Future.succeededFuture());
        });
        return this;
    }

    private List<BulkOperation> updateOperations(final List<Team> teams) {
        return teams.stream().map(team ->
            BulkOperation.createUpdate(
                findOneCode(team.getName()),
                updateFields(team),
                true,
                false))
            .collect(Collectors.toList());
    }

    private JsonObject findOneCode(final String name) {
        return new JsonObject().put(FIELD_NAME, name);
    }

    private JsonObject updateFields(final Team team) {
        return new JsonObject().put("$set", team.toJson());
    }

    @Override
    public void close() {
        client.close();
    }
}
