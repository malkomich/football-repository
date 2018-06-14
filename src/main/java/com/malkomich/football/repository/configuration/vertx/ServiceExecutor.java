package com.malkomich.football.repository.configuration.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public interface ServiceExecutor<T> {
    ServiceExecutor execute(final Message<JsonObject> message, final Handler<AsyncResult<T>> resultHandler);
}
