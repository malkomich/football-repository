package com.malkomich.football.repository.rest.service;

import com.malkomich.football.repository.league.domain.League;
import com.malkomich.football.repository.league.service.LeagueRepository;
import com.malkomich.football.repository.match.domain.Match;
import com.malkomich.football.repository.match.service.MatchRepository;
import com.malkomich.football.repository.rank.domain.Rank;
import com.malkomich.football.repository.rank.service.RankRepository;
import com.malkomich.football.repository.team.domain.Team;
import com.malkomich.football.repository.team.service.TeamRepository;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.stream.Collectors;

@Slf4j
@Builder
public class UpsertRestService extends RestHandler {
    public static final String LEAGUES_PATH = "/leagues/";
    public static final String RANKS_PATH = "/ranks/";
    public static final String MACHES_PATH = "/matches/";
    public static final String TEAMS_PATH = "/teams/";

    private LeagueRepository leagueRepository;
    private RankRepository rankRepository;
    private MatchRepository matchRepository;
    private TeamRepository teamRepository;

    public void insertLeagues(final RoutingContext context) {
        val request = context.getBodyAsJsonArray().stream()
            .map(JsonObject::mapFrom)
            .map(League::new)
            .collect(Collectors.toList());
        leagueRepository.saveLeagues(request, asyncResult -> genericHandler(asyncResult, context));
    }

    public void insertRanks(final RoutingContext context) {
        val request = context.getBodyAsJsonArray().stream()
            .map(JsonObject::mapFrom)
            .map(Rank::new)
            .collect(Collectors.toList());
        rankRepository.saveRanks(request, asyncResult -> genericHandler(asyncResult, context));
    }

    public void insertMatches(final RoutingContext context) {
        val request = context.getBodyAsJsonArray().stream()
            .map(JsonObject::mapFrom)
            .map(Match::new)
            .collect(Collectors.toList());
        matchRepository.saveMatches(request, asyncResult -> genericHandler(asyncResult, context));
    }

    public void insertTeams(final RoutingContext context) {
        val request = context.getBodyAsJsonArray().stream()
            .map(JsonObject::mapFrom)
            .map(Team::new)
            .collect(Collectors.toList());
        teamRepository.saveTeams(request, asyncResult -> genericHandler(asyncResult, context));
    }

    private void genericHandler(final AsyncResult<Void> asyncResult, final RoutingContext context) {
        if (asyncResult.failed()) {
            ReplyException exception = getReplyException(asyncResult.cause());
            errorResponse(exception, context);
            return;
        }
        successfulResponse(context);
    }
}
