package com.malkomich.football.repository.rest;

import com.google.inject.Inject;
import com.malkomich.football.repository.league.service.LeagueRepository;
import com.malkomich.football.repository.match.service.MatchRepository;
import com.malkomich.football.repository.rank.service.RankRepository;
import com.malkomich.football.repository.rest.service.UpsertRestService;
import com.malkomich.football.repository.team.service.TeamRepository;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class HttpVerticle extends AbstractVerticle {
    private static final int DEFAULT_VERTX_PORT = 8080;
    private static final String VERTX_PORT = "VERTX_PORT";

    @Inject
    private LeagueRepository leagueRepository;
    @Inject
    private RankRepository rankRepository;
    @Inject
    private MatchRepository matchRepository;
    @Inject
    private TeamRepository teamRepository;

    @Override
    public void start(final Future<Void> future) {
        val insertRestService = insertRestService();
        final Router router = generateRouter(insertRestService);
        startServer(router, future);
    }

    private UpsertRestService insertRestService() {
        return UpsertRestService.builder()
            .leagueRepository(leagueRepository)
            .rankRepository(rankRepository)
            .matchRepository(matchRepository)
            .teamRepository(teamRepository)
            .build();
    }

    private Router generateRouter(final UpsertRestService upsertRestService) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        addPostEndpointHandler(router, UpsertRestService.LEAGUES_PATH, upsertRestService::insertLeagues);
        addPostEndpointHandler(router, UpsertRestService.RANKS_PATH, upsertRestService::insertRanks);
        addPostEndpointHandler(router, UpsertRestService.MACHES_PATH, upsertRestService::insertMatches);
        addPostEndpointHandler(router, UpsertRestService.TEAMS_PATH, upsertRestService::insertTeams);
        return router;
    }

    private void startServer(final Router router, final Future<Void> future) {
        final Integer port = config().getInteger(VERTX_PORT, DEFAULT_VERTX_PORT);
        vertx.createHttpServer().requestHandler(router::accept).listen(port, asyncResult -> {
            if (asyncResult.failed()) {
                log.error("Could not start a HTTP server", asyncResult.cause());
                future.fail(asyncResult.cause());
                return;
            }
            log.info("HTTP server running on port {}", port);
            future.complete();
        });
    }

    private void addPostEndpointHandler(final Router router,
                                        final String path,
                                        final Handler<RoutingContext> handlerFunction) {
        router.post(path)
            .handler(handlerFunction)
            .consumes(Headers.FORMAT);
    }
}
