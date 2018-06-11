package com.malkomich.football.repository.rest.service;

import com.malkomich.football.repository.rest.Headers;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class RestHandler {

    static final String EXCEPTION_HANDLING_ERROR = "Server exception handling error";

    void successfulResponse(final RoutingContext context,
                            final Message<Object> response) {
        final String message = (response != null) ? response.body().toString() : null;
        resolveContext(context, HttpResponseStatus.OK.code(), message);
    }

    void successfulResponse(final RoutingContext context) {
        successfulResponse(context, null);
    }

    void errorResponse(final ReplyException exception,
                       final RoutingContext context) {
        JsonObject error = new JsonObject().put("error", exception.getMessage());
        log.error(exception.getMessage());
        Integer status = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
        if (exception.failureCode() != -1) {
            status = exception.failureCode();
        }
        resolveContext(context, status, error.toString());
    }

    void resolveContext(final RoutingContext context, final Integer status, final String message) {
        final HttpServerResponse httpServerResponse = context.response()
            .setStatusCode(status)
            .putHeader(Headers.CONTENT_HEADER, Headers.FORMAT)
            .putHeader(Headers.ACCEPT_HEADER, Headers.FORMAT);
        if (message != null) {
            httpServerResponse.end(message);
            return;
        }
        httpServerResponse.end();
    }

    ReplyException getReplyException(final Throwable throwable) {
        if (throwable instanceof ReplyException) {
            return (ReplyException) throwable;
        }
        return new ReplyException(ReplyFailure.NO_HANDLERS, EXCEPTION_HANDLING_ERROR);
    }
}
